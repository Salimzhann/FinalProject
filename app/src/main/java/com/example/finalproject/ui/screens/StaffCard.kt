package com.example.finalproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.domain.model.StaffMember

@Composable
fun StaffCard(staff: StaffMember, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = staff.posterUrl),
            contentDescription = "Staff Image",
            modifier = Modifier
                .size(70.dp, 100.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Column {
            Text(staff.nameRu, style = MaterialTheme.typography.bodyLarge)
            Text(staff.professionText, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}