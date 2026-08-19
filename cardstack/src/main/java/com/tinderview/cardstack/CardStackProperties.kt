package com.tinderview.cardstack

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Tunables for stack depth, commit threshold, and motion.
 * Threshold is a fraction of card width — never a raw pixel value.
 */
@Immutable
public data class CardStackProperties(
    public val visibleCount: Int = 3,
    public val stackOffset: Dp = 12.dp,
    public val stackScaleStep: Float = 0.05f,
    public val maxRotationZ: Float = 14f,
    public val rotationDivisor: Float = 25f,
    public val thresholdFraction: Float = 0.35f,
    public val flingVelocity: Dp = 900.dp,
    public val enabledDirections: Set<SwipeDirection> = setOf(
        SwipeDirection.Left,
        SwipeDirection.Right,
        SwipeDirection.Up,
    ),
    public val enableHaptics: Boolean = true,
    public val enableColorWash: Boolean = true,
    public val snapSpring: SpringSpec<Float> = spring(
        dampingRatio = 0.72f,
        stiffness = 400f,
    ),
    public val throwSpring: SpringSpec<Float> = spring(
        dampingRatio = 0.86f,
        stiffness = 260f,
    ),
    public val rewindSpring: SpringSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = 380f,
    ),
)
