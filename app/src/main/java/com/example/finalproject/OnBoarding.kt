package com.example.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.finalproject.ui.theme.FinalProjectTheme

class OnBoarding : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val onboarding: List<@Composable ()-> Unit>  = listOf(
            { OnboardingPage("Узнавай\nо премьерах", R.drawable.onboarding1) },
            { OnboardingPage("Создавай\nколлекции ", R.drawable.onboarding2) },
            { OnboardingPage("Делись\nс друзьями ", R.drawable.onboarding3) })
        setContent {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 26.dp, start = 26.dp, top = 80.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null
                    )
                    TextButton(onClick = {}) {
                        Text(
                            text = "Пропустить",
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0XFFB5B5C9)
                        )
                    }
                }
                val pagerState = rememberPagerState(pageCount = { onboarding.size })
                HorizontalPager(
                    state = pagerState
                ) { page ->
                    onboarding[page]()
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
        }
    }
}

@Composable
fun OnboardingPage(title: String, poster: Int) {
    Column(
        modifier = Modifier
            .height(700.dp)
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(poster),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 150.dp)
        )
        Text(
            text = title,
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
        OnboardingPage("How are you", R.drawable.onboarding2)
    }
}