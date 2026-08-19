package com.tinderview.cardstack

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density

@Composable
public fun rememberCardStackState(
    initialIndex: Int = 0,
    itemCount: () -> Int,
): CardStackState {
    val state = remember {
        CardStackState(initialIndex = initialIndex, itemCount = itemCount)
    }
    state.itemCountProvider = itemCount
    return state
}

@Stable
public class CardStackState internal constructor(
    initialIndex: Int,
    itemCount: () -> Int,
) {
    internal var itemCountProvider: () -> Int = itemCount

    public var currentIndex: Int by mutableIntStateOf(initialIndex.coerceAtLeast(0))
        internal set

    internal val offset = Animatable(Offset.Zero, Offset.VectorConverter)

    internal var cardSize: Size by mutableStateOf(Size.Zero)

    internal var lastDirection: SwipeDirection? by mutableStateOf(null)

    internal var lastExit: Offset by mutableStateOf(Offset.Zero)

    internal var properties: CardStackProperties = CardStackProperties()

    internal var reduceMotion: Boolean = false

    internal var animating: Boolean by mutableStateOf(false)

    public val isAnimating: Boolean
        get() = animating || offset.isRunning

    public val canRewind: Boolean
        get() = lastDirection != null && currentIndex > 0 && !isAnimating

    public val canSwipe: Boolean
        get() = currentIndex < itemCountProvider() && !isAnimating

    public val dragOffset: Offset
        get() = offset.value

    public val swipeProgress: Float
        get() = progressToward(CardStackProperties().enabledDirections).second

    internal fun progressToward(
        enabled: Set<SwipeDirection>,
        thresholdFraction: Float = 0.35f,
    ): Pair<SwipeDirection?, Float> {
        return CardStackMath.progressTowardCommit(
            offsetX = offset.value.x,
            offsetY = offset.value.y,
            cardWidth = cardSize.width.coerceAtLeast(1f),
            thresholdFraction = thresholdFraction,
            enabled = enabled,
        )
    }

    internal fun updateCardSize(size: Size) {
        if (size != cardSize && size.width > 0f && size.height > 0f) {
            cardSize = size
        }
    }

    public suspend fun swipe(direction: SwipeDirection) {
        if (!canSwipe) return
        throwOff(direction, initialVelocity = Offset.Zero)
    }

    public suspend fun rewind() {
        if (!canRewind) return
        animating = true
        try {
            currentIndex = CardStackMath.previousIndex(currentIndex)
            offset.snapTo(lastExit)
            if (reduceMotion) {
                offset.snapTo(Offset.Zero)
            } else {
                offset.animateTo(Offset.Zero, offsetSpring(properties.rewindSpring))
            }
            lastDirection = null
            lastExit = Offset.Zero
        } finally {
            animating = false
        }
    }

    internal suspend fun dragTo(target: Offset) {
        offset.snapTo(target)
    }

    internal suspend fun snapBack() {
        animating = true
        try {
            if (reduceMotion) {
                offset.snapTo(Offset.Zero)
            } else {
                offset.animateTo(Offset.Zero, offsetSpring(properties.snapSpring))
            }
        } finally {
            animating = false
        }
    }

    internal suspend fun throwOff(
        direction: SwipeDirection,
        initialVelocity: Offset,
    ) {
        if (currentIndex >= itemCountProvider()) return
        animating = true
        try {
            val (tx, ty) = CardStackMath.exitTarget(
                direction = direction,
                cardWidth = cardSize.width.coerceAtLeast(1f),
                cardHeight = cardSize.height.coerceAtLeast(1f),
                currentX = offset.value.x,
                currentY = offset.value.y,
            )
            val target = Offset(tx, ty)
            if (reduceMotion) {
                offset.snapTo(target)
            } else {
                offset.animateTo(
                    targetValue = target,
                    animationSpec = offsetSpring(properties.throwSpring),
                    initialVelocity = initialVelocity,
                )
            }
            lastDirection = direction
            lastExit = target
            currentIndex = CardStackMath.nextIndex(currentIndex, itemCountProvider())
            offset.snapTo(Offset.Zero)
        } finally {
            animating = false
        }
    }

    internal fun flingVelocityPx(properties: CardStackProperties, density: Density): Float {
        return with(density) { properties.flingVelocity.toPx() }
    }
}

internal fun offsetSpring(spec: SpringSpec<Float>): SpringSpec<Offset> {
    return spring(
        dampingRatio = spec.dampingRatio,
        stiffness = spec.stiffness,
        visibilityThreshold = Offset(1f, 1f),
    )
}
