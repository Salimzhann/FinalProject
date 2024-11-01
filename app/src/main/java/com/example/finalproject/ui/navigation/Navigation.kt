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
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.finalproject.ui.viewmodel.MainPageViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finalproject.domain.model.Icons
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.ui.screens.AllMoviesView
import com.example.finalproject.ui.screens.ProfileScreen
import com.example.finalproject.ui.screens.SearchScreen
import com.example.finalproject.ui.screens.SetupUI

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: MainPageViewModel = viewModel()

    Scaffold(
        bottomBar = {
            val showBottomBar = navController.currentBackStackEntryAsState().value?.destination?.route in listOf(
                "home", "search", "profile"
            )
            if (showBottomBar) MyBottomNavigation(navController)
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
                    AllMoviesView(movies, category) { navController.popBackStack() }
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
            val isSelected = currentRoute == screen.route
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
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    MainScreen()
}