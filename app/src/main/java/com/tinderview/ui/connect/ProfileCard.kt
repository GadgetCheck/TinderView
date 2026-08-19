package com.tinderview.ui.connect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinderview.data.Profile
import com.tinderview.ui.theme.Ink

@Composable
fun ProfileCard(
    profile: Profile,
    modifier: Modifier = Modifier,
    showBio: Boolean = true,
) {
    val shape = remember { RoundedCornerShape(28.dp) }
    val photo = remember(profile.id) { Brush.linearGradient(profile.gradient) }
    val scrim = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.42f to Color.Transparent,
            0.72f to Color(0x66000000),
            1f to Color(0xCC000000),
        )
    }
    val sheen = remember {
        Brush.linearGradient(
            0f to Color.White.copy(alpha = 0.16f),
            0.38f to Color.Transparent,
        )
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(shape)
            .background(photo)
            .border(1.dp, Color.White.copy(alpha = 0.12f), shape),
    ) {
        Box(Modifier.fillMaxSize().background(sheen))
        Box(Modifier.fillMaxSize().background(scrim))
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp),
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = profile.name,
                    color = Ink.Cream,
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Text(
                    text = "  ${profile.age}",
                    color = Ink.Cream.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 3.dp),
                )
            }
            Row(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InfoChip(profile.city)
                profile.interests.take(2).forEach { InfoChip(it) }
            }
            if (showBio) {
                Text(
                    text = profile.bio,
                    color = Ink.Cream.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
    }
}

@Composable
internal fun InfoChip(label: String) {
    Text(
        text = label,
        color = Ink.Cream,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.14f))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 5.dp),
    )
}
