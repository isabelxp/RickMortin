package com.android.rickandmorty.presentation.ui.list.listDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.rickandmorty.R
import com.android.rickandmorty.domain.model.CharacterDetail

@Composable
fun PostDetailScreen(detail: CharacterDetail) {
    Column(
        modifier = Modifier
            .background(colorResource(R.color.lightSkin))
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Box {
            AsyncImage(
                model = detail.image,
                contentDescription = detail.name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(2.dp))
            )
        }
        DetailCharacter(detail)
    }
}

@Composable
fun DetailCharacter(detail: CharacterDetail){
    Column(
        modifier = Modifier
            .padding(4.dp)
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Yellow)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = detail.name, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.headlineMedium)
            Divider(
                color = Color.Green.copy(alpha = 0.3f),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "About the character", modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Gender: "+detail.gender, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Specie: "+detail.species, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Status: "+detail.status, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Origin: "+detail.origin.name, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Location: "+detail.location.name, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        //EpisodeList(detail.episode)
    }
}

@Composable
fun EpisodeList(episode: List<String>) {

    Column(
        modifier = Modifier
            .padding(4.dp)
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Yellow)
        ) {
            LazyColumn {
                items(episode) { item ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(item)
                }
            }
          }
        }

}




