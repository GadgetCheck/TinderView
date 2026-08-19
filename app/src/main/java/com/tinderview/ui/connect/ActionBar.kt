package com.tinderview.ui.connect

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GlassActionBar(
    canRewind: Boolean,
    canSwipe: Boolean,
    onRewind: () -> Unit,
    onPass: () -> Unit,
    onSuperLike: () -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .graphicsLayer { alpha = 0.96f }
            .clip(CircleShape)
            .background(Color(0x66111114))
            .border(1.dp, Color.White.copy(alpha = 0.18f), CircleShape),
        horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ActionButton(
            glyph = "↺",
            tint = Color(0xFFF4C15D),
            enabled = canRewind,
            size = 54.dp,
            onClick = onRewind,
        )
        ActionButton(
            glyph = "✕",
            tint = Color(0xFFFF6B6B),
            enabled = canSwipe,
            size = 64.dp,
            onClick = onPass,
        )
        ActionButton(
            glyph = "★",
            tint = Color(0xFF4DA3FF),
            enabled = canSwipe,
            size = 54.dp,
            onClick = onSuperLike,
        )
        ActionButton(
            glyph = "♥",
            tint = Color(0xFF3DDC84),
            enabled = canSwipe,
            size = 64.dp,
            onClick = onLike,
        )
    }
}

@Composable
private fun ActionButton(
    glyph: String,
    tint: Color,
    enabled: Boolean,
    size: Dp,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else 1f,
        animationSpec = spring(stiffness = 700f, dampingRatio = 0.55f),
        label = "action-scale",
    )
    Box(
        modifier = Modifier
            .size(size)
            .scale(scale)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = if (enabled) 0.12f else 0.05f))
            .border(1.5.dp, tint.copy(alpha = if (enabled) 0.9f else 0.25f), CircleShape)
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = glyph,
            color = tint.copy(alpha = if (enabled) 1f else 0.35f),
            fontSize = if (size > 58.dp) 26.sp else 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
