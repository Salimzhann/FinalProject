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
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState


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
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { setupUI(viewModel) }
            composable(Screen.Search.route) { SearchScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}

@Composable
fun MyBottomNavigation(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Profile
    )

    // Observe the current back stack entry
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

sealed class Screen(val route: String, val iconId: Int) {
    data object Home : Screen("home", R.drawable.homeicon)
    data object Search : Screen("search", R.drawable.searchicon)
    data object Profile : Screen("profile", R.drawable.profileicon)
}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    MainScreen()
}