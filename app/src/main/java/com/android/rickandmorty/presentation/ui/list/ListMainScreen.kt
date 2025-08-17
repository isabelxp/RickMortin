package com.android.rickandmorty.presentation.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.rickandmorty.presentation.state.DetailListState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.android.rickandmorty.R
import com.android.rickandmorty.domain.model.CharacterDetail
import com.android.rickandmorty.presentation.viewmodel.ListMainViewModel


@Composable
fun ListCharacterScreen(
    viewModel: ListMainViewModel,
    onPostClick: (CharacterDetail) -> Unit
) {

    var state by remember { mutableStateOf<DetailListState>(DetailListState.Loading) }

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadPosts { newState ->
            state = newState
        }
    }


    when (val currentState = state) {
        is DetailListState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailListState.Success -> {
            Column(
                modifier = Modifier
                    .background(colorResource(R.color.lightSkin))
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                val filteredList = currentState.character.filter {
                    it.name.contains(searchText, ignoreCase = true)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 4.dp, end = 4.dp)
                ) {
                    SearchBar(onSearchChanged = {searchText=it})
                    ItemList(listCharacter = filteredList, onPostClick = onPostClick)
                }
            }
        }

        is DetailListState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


@Composable
fun CharacterCard(
    imageUrl: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .width(160.dp)
            .padding(8.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick ={onClick()},
        colors = CardDefaults.cardColors(containerColor = Color.Yellow)
    ) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Text(
            text = title,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(8.dp)
        )
    }
}



@Composable
fun SearchBar(
    hint: String = "Buscar...",
    onSearchChanged: (String) -> Unit = {},
    onVoiceClick: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(horizontal = 2.dp, vertical = 4.dp)
            .background(
                color = Color(0xFFF1F0F4),
                shape = RoundedCornerShape(50)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(start = 8.dp),
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = Color.Gray
        )

        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = query,
            onValueChange = {
                query = it
                onSearchChanged(it)
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),

                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,

                cursorColor = MaterialTheme.colorScheme.onSurface,
                errorCursorColor = MaterialTheme.colorScheme.error
            ),
            placeholder = { Text(text = hint, color = Color.Gray) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            singleLine = true
        )

        IconButton(onClick = { onVoiceClick() }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_mic_24),
                contentDescription = "Voice Search",
                tint = Color.Gray
            )
        }

    }
}



@Composable
fun ItemList(listCharacter: List<CharacterDetail>, onPostClick: (CharacterDetail) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)) {
        items(listCharacter) { item ->
            CharacterCard(
                imageUrl = item.image,
                title = item.name,
                onClick = { onPostClick(item) })
        }
    }
}
