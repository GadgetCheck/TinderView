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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tinderview.ui.theme.CloseIcon
import com.tinderview.ui.theme.HeartIcon
import com.tinderview.ui.theme.Ink
import com.tinderview.ui.theme.RewindIcon
import com.tinderview.ui.theme.StarIcon

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
    val pill = RoundedCornerShape(percent = 50)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(22.dp, pill, ambientColor = Color.Black.copy(alpha = 0.45f), spotColor = Color.Black.copy(alpha = 0.55f))
            .clip(pill)
            .background(
                Brush.verticalGradient(
                    0f to Color(0xCC2A2724),
                    1f to Color(0xE6141210),
                ),
            )
            .border(1.dp, Color.White.copy(alpha = 0.16f), pill)
            .padding(horizontal = 10.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActionButton(enabled = canRewind, tint = Ink.Gold, size = 50.dp, onClick = onRewind) {
                RewindIcon(Ink.Gold.copy(alpha = if (canRewind) 1f else 0.35f), size = 20.dp)
            }
            ActionButton(enabled = canSwipe, tint = Ink.Nope, size = 60.dp, onClick = onPass) {
                CloseIcon(Ink.Nope.copy(alpha = if (canSwipe) 1f else 0.35f), size = 22.dp)
            }
            ActionButton(enabled = canSwipe, tint = Ink.Ice, size = 50.dp, onClick = onSuperLike) {
                StarIcon(Ink.Ice.copy(alpha = if (canSwipe) 1f else 0.35f), size = 20.dp)
            }
            ActionButton(enabled = canSwipe, tint = Ink.Mint, size = 60.dp, onClick = onLike) {
                HeartIcon(Ink.Mint.copy(alpha = if (canSwipe) 1f else 0.35f), size = 22.dp)
            }
        }
    }
}

@Composable
private fun ActionButton(
    enabled: Boolean,
    tint: Color,
    size: Dp,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
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
            .background(tint.copy(alpha = if (enabled) 0.16f else 0.06f))
            .border(1.25.dp, tint.copy(alpha = if (enabled) 0.85f else 0.22f), CircleShape)
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        icon()
    }
}
