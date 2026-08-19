package com.tinderview.cardstack

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

public object CardStackDefaults {

    public fun properties(): CardStackProperties = CardStackProperties()

    @Composable
    public fun LikeStamp(progress: Float) {
        Stamp(
            label = "LIKE",
            color = Color(0xFF2ECC71),
            rotation = -18f,
            progress = progress,
            alignment = Alignment.TopStart,
        )
    }

    @Composable
    public fun PassStamp(progress: Float) {
        Stamp(
            label = "NOPE",
            color = Color(0xFFE74C3C),
            rotation = 18f,
            progress = progress,
            alignment = Alignment.TopEnd,
        )
    }

    @Composable
    public fun SuperLikeStamp(progress: Float) {
        Stamp(
            label = "SUPER",
            color = Color(0xFF3498DB),
            rotation = 0f,
            progress = progress,
            alignment = Alignment.TopCenter,
        )
    }
}

@Composable
private fun Stamp(
    label: String,
    color: Color,
    rotation: Float,
    progress: Float,
    alignment: Alignment,
) {
    val shape = remember { RoundedCornerShape(6.dp) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clearAndSetSemantics { }
            .padding(20.dp),
        contentAlignment = alignment,
    ) {
        val alpha = progress.coerceIn(0f, 1f)
        Text(
            text = label,
            color = color.copy(alpha = alpha),
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 3.sp,
            modifier = Modifier
                .rotate(rotation)
                .background(color.copy(alpha = alpha * 0.12f), shape)
                .border(2.dp, color.copy(alpha = alpha), shape)
                .padding(horizontal = 12.dp, vertical = 5.dp),
        )
    }
}
