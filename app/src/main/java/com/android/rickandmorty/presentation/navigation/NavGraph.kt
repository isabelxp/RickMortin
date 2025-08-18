package com.android.rickandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.android.rickandmorty.domain.model.CharacterDetail
import com.android.rickandmorty.presentation.ui.list.ListCharacterScreen
import com.android.rickandmorty.presentation.viewmodel.ListMainViewModel
import com.android.rickandmorty.presentation.ui.list.listDetail.PostDetailScreen
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "post_list") {

        composable("post_list") {
            val viewModel: ListMainViewModel = hiltViewModel()
            ListCharacterScreen(
                viewModel = viewModel,
                onPostClick = { post ->
                    val json = URLEncoder.encode(
                        Gson().toJson(post),
                        StandardCharsets.UTF_8.toString()
                    )
                    navController.navigate("post_detail/$json")
                }
            )
        }

        composable(
            "post_detail/{post}",
            arguments = listOf(navArgument("post") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("post")
            val decoded = URLDecoder.decode(json, StandardCharsets.UTF_8.toString())
            val post = Gson().fromJson(decoded, CharacterDetail::class.java)
            PostDetailScreen(detail = post)
        }
    }
}

