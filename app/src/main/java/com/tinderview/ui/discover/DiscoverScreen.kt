package com.tinderview.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tinderview.data.SampleProfiles
import com.tinderview.ui.theme.Ink
import com.tinderview.ui.theme.PlusJakarta

@Composable
fun DiscoverScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .background(Ink.Night),
    ) {
        Text(
            text = "Nearby",
            style = MaterialTheme.typography.headlineMedium,
            color = Ink.Cream,
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 18.dp),
        )
        Text(
            text = "People around you tonight",
            color = Ink.CreamMuted,
            fontFamily = PlusJakarta,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 8.dp),
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(SampleProfiles, key = { it.id }) { profile ->
                val brush = remember(profile.id) { Brush.linearGradient(profile.gradient) }
                val scrim = remember {
                    Brush.verticalGradient(
                        0.45f to Color.Transparent,
                        1f to Color(0xB3000000),
                    )
                }
                val shape = RoundedCornerShape(22.dp)
                Box(
                    modifier = Modifier
                        .aspectRatio(0.78f)
                        .clip(shape)
                        .background(brush)
                        .border(1.dp, Color.White.copy(alpha = 0.10f), shape),
                ) {
                    Box(Modifier.fillMaxSize().background(scrim))
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp),
                    ) {
                        Text(
                            text = "${profile.name}, ${profile.age}",
                            color = Ink.Cream,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = profile.city,
                            color = Ink.Cream.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }
            }
        }
    }
}
