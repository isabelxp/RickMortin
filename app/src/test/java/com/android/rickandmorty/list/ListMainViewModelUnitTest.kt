package com.android.rickandmorty.list

import com.android.rickandmorty.MainDispatcherRule
import com.android.rickandmorty.data.remote.model.CharacterInfo
import com.android.rickandmorty.data.remote.model.Info
import com.android.rickandmorty.data.remote.model.ListCharacterResponse
import com.android.rickandmorty.data.remote.model.Location
import com.android.rickandmorty.data.remote.model.Origin
import com.android.rickandmorty.data.repository.Repository
import com.android.rickandmorty.presentation.state.DetailListState
import com.android.rickandmorty.presentation.viewmodel.ListMainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
class ListMainViewModelUnitTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Test
    fun `When the repo returns a list, the state is Success`() = runTest {
        val fakeCharacter = CharacterInfo(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "2",
            gender = "Male",
            origin = Origin("Earth", "https://rickandmortyapi.com/api/location/1"),
            location = Location("Earth", "https://rickandmortyapi.com/api/location/20"),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episode = listOf("https://rickandmortyapi.com/api/episode/1"),
            url = "https://rickandmortyapi.com/api/character/1",
            created = "2017-11-04T18:48:46.250Z"
        )

        val fakeList = ListCharacterResponse(
            info = Info(count = 1, pages = 1, next = null, prev = null),
            results = listOf(fakeCharacter)
        )

        val repositoryMock = mock<Repository>()
        whenever(repositoryMock.getListCharacters()).thenReturn(fakeList)

        val viewModel = ListMainViewModel(repositoryMock)
        viewModel.loadPosts {}
        advanceUntilIdle()

        val stateValue = viewModel.state.value
        assert(stateValue is DetailListState.Success)
        assertEquals(1, (stateValue as DetailListState.Success).character.size)
    }

    @Test
    fun `When the repo throws an error, the state is Error`() = runTest {

        val repositoryMock = mock<Repository>()

        whenever(repositoryMock.getListCharacters()).thenAnswer {
            throw IOException("Network error")
        }
        val viewModel = ListMainViewModel(repositoryMock)
        val capturedStates = mutableListOf<DetailListState>()

        // When
        viewModel.loadPosts { capturedStates.add(it) }
        advanceUntilIdle()

        // Then
        assertEquals(2, capturedStates.size)
        assertTrue(capturedStates[0] is DetailListState.Loading)
        assertTrue(capturedStates[1] is DetailListState.Error)
        assertEquals(
            "Error al cargar datos: Network error",
            (capturedStates[1] as DetailListState.Error).message
        )
    }
}