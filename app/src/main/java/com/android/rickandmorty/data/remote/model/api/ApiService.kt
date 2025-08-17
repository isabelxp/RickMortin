package com.android.rickandmorty.data.remote.model.api

import com.android.rickandmorty.data.remote.model.ListCharacterResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/api/character")
    suspend fun getListCharacter(): ListCharacterResponse
}
