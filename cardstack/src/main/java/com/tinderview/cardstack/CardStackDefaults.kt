package com.tinderview.cardstack

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
            .clearAndSetSemantics { }
            .padding(20.dp),
        contentAlignment = alignment,
    ) {
        Text(
            text = label,
            color = color.copy(alpha = progress.coerceIn(0f, 1f)),
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            modifier = Modifier
                .rotate(rotation)
                .border(3.5.dp, color.copy(alpha = progress.coerceIn(0f, 1f)), shape)
                .padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}
