package com.tinderview.ui.connect

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinderview.data.Profile
import kotlinx.coroutines.delay

@Composable
fun MatchBurst(
    profile: Profile,
    onDismiss: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(profile.id) {
        visible = true
        delay(2200)
        onDismiss()
    }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.4f,
        animationSpec = spring(stiffness = 180f, dampingRatio = 0.6f),
        label = "match-scale",
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        label = "match-alpha",
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xE6101114).copy(alpha = 0.88f * alpha))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale),
        ) {
            Text("♥", fontSize = 72.sp, color = Color(0xFFFF6B6B))
            Text(
                text = "It's a match",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 8.dp),
            )
            Text(
                text = "You and ${profile.name} liked each other",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
