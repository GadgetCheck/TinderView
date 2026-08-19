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

    internal var fingerOffset: Offset by mutableStateOf(Offset.Zero)

    internal var cardSize: Size by mutableStateOf(Size.Zero)

    internal var lastDirection: SwipeDirection? by mutableStateOf(null)

    internal var lastExit: Offset by mutableStateOf(Offset.Zero)

    internal var properties: CardStackProperties = CardStackProperties()

    internal var reduceMotion: Boolean = false

    internal var onSettledSwipe: ((swipedIndex: Int, direction: SwipeDirection) -> Unit)? = null

    internal var animating: Boolean by mutableStateOf(false)

    public val isAnimating: Boolean
        get() = animating || offset.isRunning

    public val canRewind: Boolean
        get() = lastDirection != null && currentIndex > 0 && !isAnimating

    public val canSwipe: Boolean
        get() = currentIndex < itemCountProvider() && !isAnimating

    public val dragOffset: Offset
        get() = if (offset.isRunning || animating) offset.value else fingerOffset

    public val swipeProgress: Float
        get() = progressToward(properties.enabledDirections, properties.thresholdFraction).second

    internal fun resolvedCardSize(): Size {
        val width = CardStackMath.resolvedDimension(cardSize.width, 1080f)
        val height = CardStackMath.resolvedDimension(cardSize.height, 1920f)
        return Size(width, height)
    }

    internal fun progressToward(
        enabled: Set<SwipeDirection>,
        thresholdFraction: Float = 0.35f,
    ): Pair<SwipeDirection?, Float> {
        val current = dragOffset
        val size = resolvedCardSize()
        return CardStackMath.progressTowardCommit(
            offsetX = current.x,
            offsetY = current.y,
            cardWidth = size.width,
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
        val exit = lastExit
        // Place the returning card off-screen *before* the index moves or
        // [animating] flips, otherwise one frame draws it at rest in the center.
        fingerOffset = exit
        currentIndex = CardStackMath.previousIndex(currentIndex)
        offset.snapTo(exit)
        animating = true
        try {
            if (reduceMotion) {
                offset.snapTo(Offset.Zero)
            } else {
                offset.animateTo(Offset.Zero, offsetSpring(properties.rewindSpring))
            }
            fingerOffset = Offset.Zero
            lastDirection = null
            lastExit = Offset.Zero
        } finally {
            animating = false
        }
    }

    internal fun dragTo(target: Offset) {
        fingerOffset = target
    }

    internal suspend fun snapBack() {
        animating = true
        try {
            offset.snapTo(fingerOffset)
            if (reduceMotion) {
                offset.snapTo(Offset.Zero)
            } else {
                offset.animateTo(Offset.Zero, offsetSpring(properties.snapSpring))
            }
            fingerOffset = Offset.Zero
        } finally {
            animating = false
        }
    }

    internal suspend fun throwOff(
        direction: SwipeDirection,
        initialVelocity: Offset,
    ) {
        if (currentIndex >= itemCountProvider()) return
        val swipedIndex = currentIndex
        animating = true
        try {
            val size = resolvedCardSize()
            val (tx, ty) = CardStackMath.exitTarget(
                direction = direction,
                cardWidth = size.width,
                cardHeight = size.height,
                currentX = dragOffset.x,
                currentY = dragOffset.y,
            )
            val target = Offset(tx, ty)
            offset.snapTo(fingerOffset)
            if (reduceMotion) {
                offset.snapTo(target)
            } else {
                offset.animateTo(
                    targetValue = target,
                    animationSpec = offsetSpring(properties.throwSpring),
                    initialVelocity = initialVelocity,
                )
            }
            fingerOffset = Offset.Zero
            lastDirection = direction
            lastExit = target
            currentIndex = CardStackMath.nextIndex(currentIndex, itemCountProvider())
            offset.snapTo(Offset.Zero)
            onSettledSwipe?.invoke(swipedIndex, direction)
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
