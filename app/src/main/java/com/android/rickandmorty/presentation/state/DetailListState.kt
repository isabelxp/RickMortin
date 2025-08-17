package com.android.rickandmorty.presentation.state
import com.android.rickandmorty.domain.model.CharacterDetail

sealed class DetailListState {
    data object Loading : DetailListState()
    data class Success(val character: List<CharacterDetail>) : DetailListState()
    data class Error(val message: String) : DetailListState()
}