package com.example.finalproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.domain.model.StaffMember

@Composable
fun StaffCard(staff: StaffMember, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .clickable(onClick = onClick)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberAsyncImagePainter(model = staff.posterUrl),
                contentDescription = "Staff Image",
                modifier = Modifier.size(100.dp)
            )
            Text(staff.nameRu, style = MaterialTheme.typography.bodyMedium)
            Text(staff.professionText, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
