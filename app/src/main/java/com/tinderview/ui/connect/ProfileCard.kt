package com.tinderview.ui.connect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinderview.data.Profile

@Composable
fun ProfileCard(
    profile: Profile,
    modifier: Modifier = Modifier,
) {
    val shape = remember { RoundedCornerShape(24.dp) }
    val photo = remember(profile.id) { Brush.linearGradient(profile.gradient) }
    val scrim = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.45f to Color.Transparent,
            1f to Color(0xA6000000),
        )
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(shape)
            .background(photo),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(scrim),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(22.dp),
        ) {
            Text(
                text = "${profile.name}, ${profile.age}",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = profile.city,
                color = Color.White.copy(alpha = 0.86f),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp),
            )
            Text(
                text = profile.bio,
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 16.sp,
                lineHeight = 22.sp,
            )
        }
    }
}
