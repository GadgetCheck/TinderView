package com.tinderview.ui.connect

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinderview.data.Profile
import com.tinderview.ui.theme.Ink
import com.tinderview.ui.theme.InstrumentSerif
import com.tinderview.ui.theme.PlusJakarta
import kotlinx.coroutines.delay

@Composable
fun MatchBurst(
    profile: Profile,
    onDismiss: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(profile.id) {
        visible = true
        delay(2300)
        onDismiss()
    }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.72f,
        animationSpec = spring(stiffness = 220f, dampingRatio = 0.68f),
        label = "match-scale",
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        label = "match-alpha",
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink.Wash.copy(alpha = 0.92f * alpha))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(340.dp)
                .scale(scale)
                .background(
                    Brush.radialGradient(listOf(Ink.Coral.copy(alpha = 0.28f * alpha), Color.Transparent)),
                    CircleShape,
                ),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(72.dp)
                        .offset(x = 10.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Ink.Coral, Ink.CoralDeep))),
                )
                Box(
                    Modifier
                        .size(72.dp)
                        .offset(x = (-10).dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(profile.gradient)),
                )
            }
            Text(
                text = "It's a match",
                color = Ink.Cream,
                fontSize = 42.sp,
                fontFamily = InstrumentSerif,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 22.dp),
            )
            Text(
                text = "You and ${profile.name} liked each other",
                color = Ink.CreamMuted,
                fontSize = 15.sp,
                fontFamily = PlusJakarta,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
