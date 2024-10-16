package com.example.finalproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.finalproject.Model.MainPageViewModel
import com.example.finalproject.R


class Navigation : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel = MainPageViewModel()
    Scaffold(
        bottomBar = { MyBottomNavigation(navController) }
    ) { innerPadding ->
        NavHost(navController, startDestination = Icons.Home.route, Modifier.padding(innerPadding)) {
            composable(Icons.Home.route) { setupUI(viewModel) }
            composable(Icons.Search.route) { SearchScreen() }
            composable(Icons.Profile.route) { ProfileScreen() }
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
    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        items.forEach { icon ->
            BottomNavigationItem(
                icon = {
                    Icon(painter = painterResource(id = icon.iconId), contentDescription = null, modifier = Modifier.size(18.dp)) },
                selected = navController.currentDestination?.route == icon.route,
                onClick = {
                    navController.navigate(icon.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false,
                selectedContentColor = Color.Blue.copy(alpha = 0.8f),
                unselectedContentColor = Color.Gray.copy(alpha = 0.4f)
            )
        }
    }
}

sealed class Icons(val route: String, val iconId: Int) {
    data object Home : Icons("home", R.drawable.homeicon)
    data object Search : Icons("search", R.drawable.searchicon)
    data object Profile : Icons("profile", R.drawable.profileicon)
}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    MainScreen()
}