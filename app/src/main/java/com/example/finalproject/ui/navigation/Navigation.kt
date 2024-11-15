package com.example.finalproject.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.finalproject.ui.viewmodel.MainPageViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finalproject.domain.model.Icons
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.ui.screens.ActorDetailScreen
import com.example.finalproject.ui.screens.AllMoviesView
import com.example.finalproject.ui.screens.FilmographyItem
import com.example.finalproject.ui.screens.FilmographyScreen
import com.example.finalproject.ui.screens.GalleryScreen
import com.example.finalproject.ui.screens.MovieDetailScreen
import com.example.finalproject.ui.screens.ProfileScreen
import com.example.finalproject.ui.screens.SearchScreen
import com.example.finalproject.ui.screens.SetupUI

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: MainPageViewModel = viewModel()

    Scaffold(
        bottomBar = {
            MyBottomNavigation(navController)
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("home") {
                SetupUI(viewModel = viewModel, navController = navController)
            }
            composable("search") {
                SearchScreen()
            }
            composable("profile") {
                ProfileScreen()
            }
            composable("allMovies/{category}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")

                val movies = when (category) {
                    "ТОП 250 ФИЛЬМОВ" -> (viewModel.screenStatePremieres.value as? ScreenState.Success)?.data ?: emptyList()
                    "Популярное" -> (viewModel.screenStatePopular.value as? ScreenState.Success)?.data ?: emptyList()
                    "ТОП 250 СЕРИАЛОВ" -> (viewModel.screenStateSeries.value as? ScreenState.Success)?.data ?: emptyList()
                    else -> emptyList()
                }

                if (category != null) {
                    AllMoviesView(movies, category, navController) { navController.popBackStack() }
                }
            }
            composable("movieDetail/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
                if (movieId != null) {
                    MovieDetailScreen(movieId, viewModel, navController)
                } else {
                    Text("Invalid movie ID")
                }
            }
            composable("actorDetail/{staffId}") { backStackEntry ->
                val staffId = backStackEntry.arguments?.getString("staffId")?.toIntOrNull()
                if (staffId != null) {
                    ActorDetailScreen(staffId, viewModel, { navController.popBackStack() }, navController)
                } else {
                    Text("Invalid Staff ID")
                }
            }
            composable("galleryScreen/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
                if (movieId != null) {
                    GalleryScreen(viewModel, movieId) { navController.popBackStack() }
                } else {
                    Text("Invalid movie ID")
                }
            }
            composable("filmography/{actorId}") { backStackEntry ->
                val actorId = backStackEntry.arguments?.getString("actorId")?.toIntOrNull()
                if (actorId != null) {
                    FilmographyScreen(actorId) { navController.popBackStack() }
                } else {
                    Text("Invalid Actor ID")
                }
            }

        }
    }
}

@Composable
fun MyBottomNavigation(navController: NavController) {
    val items = listOf(
        Icons.Home,
        Icons.Search,
        Icons.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        items.forEach { screen ->
            val isHomeRelated = currentRoute == "home" || currentRoute?.startsWith("movieDetail/") == true ||
                    currentRoute?.startsWith("allMovies/") == true || currentRoute?.startsWith("actorDetail/") == true || currentRoute?.startsWith("galleryScreen/") == true
            val isSelected = when (screen.route) {
                Icons.Home.route -> isHomeRelated
                else -> currentRoute == screen.route
            }
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (isSelected) Color.Blue.copy(alpha = 0.8f) else Color.Gray.copy(alpha = 0.4f)
                    )
                },
                selected = isSelected,
                onClick = {
                    if (screen.route == Icons.Home.route) {
                        navController.popBackStack(navController.graph.startDestinationId, inclusive = false)
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}
