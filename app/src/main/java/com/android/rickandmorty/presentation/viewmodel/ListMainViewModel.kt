package com.android.rickandmorty.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.rickandmorty.data.repository.Repository
import com.android.rickandmorty.domain.model.CharacterDetail
import com.android.rickandmorty.presentation.state.DetailListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class ListMainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow<DetailListState>(DetailListState.Loading)
    val state: StateFlow<DetailListState> = _state


    fun loadPosts(onStateChanged: (DetailListState) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = DetailListState.Loading
                onStateChanged(_state.value)

                val characterResult = repository.getListCharacters()

                val enrichedPosts: List<CharacterDetail> = characterResult.results.map {
                    CharacterDetail(
                        id = it.id,
                        name = it.name,
                        status = it.status,
                        species = it.species,
                        gender = it.gender,
                        image = it.image,
                        url = it.url,
                        episode = it.episode,
                        location = it.location,
                        origin = it.origin
                    )
                }
                _state.value = DetailListState.Success(enrichedPosts)
                onStateChanged(_state.value)

            } catch (e: Exception) {
                _state.value = DetailListState.Error("Error al cargar datos: ${e.message}")
                onStateChanged(_state.value)
            }
        }
    }
}
