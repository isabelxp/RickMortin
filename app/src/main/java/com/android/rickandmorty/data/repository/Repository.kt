package com.android.rickandmorty.data.repository

import com.android.rickandmorty.data.remote.model.api.ApiService
import com.android.rickandmorty.data.remote.model.ListCharacterResponse
import javax.inject.Inject


class Repository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getListCharacters(): ListCharacterResponse {
        return api.getListCharacter()
    }
}

