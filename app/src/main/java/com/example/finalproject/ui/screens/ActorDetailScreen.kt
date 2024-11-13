package com.example.finalproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.ui.viewmodel.MainPageViewModel

@Composable
fun ActorDetailScreen(actorId: Int, viewModel: MainPageViewModel) {
    LaunchedEffect(actorId) {
        viewModel.loadActorDetails(actorId)
    }

    val actorDetail = viewModel.actorDetails.value
    if (actorDetail != null) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Image(painter = rememberAsyncImagePainter(actorDetail.posterUrl),
                contentDescription = "Actor Image",
                modifier = Modifier.size(150.dp))
            Text("Name: ${actorDetail.nameRu}", style = MaterialTheme.typography.bodyLarge)
            Text("Profession: ${actorDetail.profession}", style = MaterialTheme.typography.bodySmall)
            // Add other details as needed
        }
    } else {
        Text("Loading or no details available")
    }
}
