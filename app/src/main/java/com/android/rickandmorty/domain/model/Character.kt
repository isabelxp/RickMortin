package com.android.rickandmorty.domain.model

import com.android.rickandmorty.data.remote.model.Location
import com.android.rickandmorty.data.remote.model.Origin

data class CharacterDetail(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val url: String,
    val origin: Origin,
    val location: Location,
    val episode: List<String>
)