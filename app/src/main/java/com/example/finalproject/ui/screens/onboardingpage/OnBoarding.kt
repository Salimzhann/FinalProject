package com.example.finalproject.ui.screens.onboardingpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.data.local.Onboarding
import com.example.finalproject.data.local.onboardingPages
import com.example.finalproject.R
import com.example.finalproject.ui.navigation.MainScreen
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.example.finalproject.ui.viewmodel.MainPageViewModel

@Composable
fun OnboardingScreen()  {
    val navController = rememberNavController()
    val viewModel: MainPageViewModel = viewModel()
    NavHost(navController, startDestination = "onboarding") {
        composable("onboarding") {
            HorizontalPager(navController, viewModel)
        }
        composable("mainPage") {
            MainScreen()
        }
    }
}
@Composable
fun HorizontalPager(navController: NavController, viewModel: MainPageViewModel) {
    var isSkipClicked by remember { mutableStateOf(false) }
    val allDataLoaded by viewModel.allDataLoaded.observeAsState(initial = false)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 26.dp, start = 26.dp, top = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null
            )
            TextButton(
                onClick = {
                    isSkipClicked = true
                    viewModel.loadAllData()
                }
            ) {
                Text(
                    text = "Пропустить",
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0XFFB5B5C9)
                )
            }
        }
        val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
        HorizontalPager(state = pagerState) { page ->
            OnboardingPage(onboardingPages[page])
        }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(26.dp, bottom = 60.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }

    if (isSkipClicked && !allDataLoaded) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(isSkipClicked, allDataLoaded) {
        if (isSkipClicked && allDataLoaded) {
            navController.navigate("mainPage") {
                popUpTo("onboarding") { inclusive = true }
            }
        }
    }
}

@Composable
fun OnboardingPage(page: Onboarding) {
    Column(
        modifier = Modifier
            .height(630.dp)
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(page.poster),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 150.dp)
        )
        Text(
            text = page.title,
            textAlign = TextAlign.Left,
            lineHeight = 42.sp,
            fontSize = 32.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(top = 100.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingPreviews() {
    FinalProjectTheme {
    }
}