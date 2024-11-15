package com.example.finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.domain.model.FilmBrief
import com.example.finalproject.ui.viewmodel.MainPageViewModel

@Composable
fun FilmographyItem(film: FilmBrief) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(97.dp, 132.dp)
                .background(Color.LightGray)
                .padding(4.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = film.rating ?: "N/A",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .background(Color.Gray, shape = RoundedCornerShape(20))
                    .padding(4.dp),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = film.nameRu ?: film.nameEn ?: "",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${film.description}, ${film.professionKey}",
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmographyScreen(
    actorId: Int,
    onNavigateBack: ()-> Unit
) {
    val viewModel: MainPageViewModel = viewModel()
    val actorDetail by viewModel.actorDetails.observeAsState()
    LaunchedEffect(actorId) {
        viewModel.loadActorDetails(actorId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            actorDetail?.let { actor ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text(
                        text = actor.nameRu ?: actor.nameEn ?: "Актёр",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            items(if (actor.films.size < 8) actor.films else actor.films.take(8)) { film ->
                                FilmographyItem(film)
                            }
                        }
                    }
                }
            }
        }
    )
}
