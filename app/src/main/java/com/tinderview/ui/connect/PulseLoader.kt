package com.tinderview.ui.connect

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinderview.ui.theme.Ink
import com.tinderview.ui.theme.PlusJakarta

@Composable
fun PulseLoader(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulse-t",
    )
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Canvas(Modifier.size(200.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val maxR = size.minDimension / 2f
                repeat(3) { i ->
                    val phase = ((t + i / 3f) % 1f)
                    drawCircle(
                        color = Ink.Coral.copy(alpha = (1f - phase) * 0.55f),
                        radius = maxR * (0.18f + phase * 0.78f),
                        center = center,
                        style = Stroke(width = 2.5.dp.toPx()),
                    )
                }
                drawCircle(color = Ink.Coral, radius = maxR * 0.12f, center = center)
            }
            Text(
                text = "tinderview",
                color = Ink.Cream.copy(alpha = 0.55f),
                fontFamily = PlusJakarta,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                letterSpacing = 3.4.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
