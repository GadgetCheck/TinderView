package com.tinderview.cardstack

import kotlin.math.abs
import kotlin.math.max

/**
 * Pure geometry for the deck. Kept allocation-free so unit tests and the
 * gesture path share one implementation.
 */
public object CardStackMath {

    public fun rotationZ(
        translationX: Float,
        divisor: Float,
        maxRotation: Float,
    ): Float {
        if (divisor == 0f) return 0f
        return (translationX / divisor).coerceIn(-maxRotation, maxRotation)
    }

    public fun dominantDirection(offsetX: Float, offsetY: Float): SwipeDirection? {
        if (offsetX == 0f && offsetY == 0f) return null
        val ax = abs(offsetX)
        val ay = abs(offsetY)
        return when {
            ay > ax && offsetY < 0f -> SwipeDirection.Up
            offsetX > 0f -> SwipeDirection.Right
            offsetX < 0f -> SwipeDirection.Left
            offsetY < 0f -> SwipeDirection.Up
            else -> null
        }
    }

    public fun axisDistance(
        offsetX: Float,
        offsetY: Float,
        direction: SwipeDirection,
    ): Float {
        val raw = when (direction) {
            SwipeDirection.Left -> -offsetX
            SwipeDirection.Right -> offsetX
            SwipeDirection.Up -> -offsetY
        }
        return raw.coerceAtLeast(0f)
    }

    public fun progressTowardCommit(
        offsetX: Float,
        offsetY: Float,
        cardWidth: Float,
        thresholdFraction: Float,
        enabled: Set<SwipeDirection>,
    ): Pair<SwipeDirection?, Float> {
        val direction = dominantDirection(offsetX, offsetY) ?: return null to 0f
        if (direction !in enabled) return direction to 0f
        val thresholdPx = cardWidth * thresholdFraction
        if (thresholdPx <= 0f) return direction to 1f
        val distance = axisDistance(offsetX, offsetY, direction)
        return direction to (distance / thresholdPx).coerceIn(0f, 1f)
    }

    public fun shouldCommit(
        direction: SwipeDirection?,
        progress: Float,
        velocityX: Float,
        velocityY: Float,
        flingVelocityPx: Float,
        enabled: Set<SwipeDirection>,
    ): SwipeDirection? {
        if (direction == null || direction !in enabled) return null
        val flingSpeed = when (direction) {
            SwipeDirection.Left -> -velocityX
            SwipeDirection.Right -> velocityX
            SwipeDirection.Up -> -velocityY
        }
        return if (progress >= 1f || flingSpeed >= flingVelocityPx) direction else null
    }

    public fun nextIndex(current: Int, count: Int): Int {
        val next = current + 1
        return if (next <= count) next else current
    }

    public fun previousIndex(current: Int): Int = if (current > 0) current - 1 else 0

    public fun rubberBand(
        value: Float,
        allow: Boolean,
        coefficient: Float = 0.28f,
    ): Float {
        if (allow) return value
        return value * coefficient / (1f + abs(value) * 0.002f)
    }

    public fun constrainDrag(
        offsetX: Float,
        offsetY: Float,
        enabled: Set<SwipeDirection>,
    ): Pair<Float, Float> {
        val xAllowed =
            (offsetX > 0f && SwipeDirection.Right in enabled) ||
                (offsetX < 0f && SwipeDirection.Left in enabled)
        val yAllowed = offsetY < 0f && SwipeDirection.Up in enabled
        return rubberBand(offsetX, xAllowed) to rubberBand(offsetY, yAllowed)
    }

    public fun exitTarget(
        direction: SwipeDirection,
        cardWidth: Float,
        cardHeight: Float,
        currentX: Float,
        currentY: Float,
    ): Pair<Float, Float> {
        val extra = max(cardWidth, cardHeight) * 1.15f
        return when (direction) {
            SwipeDirection.Right -> (cardWidth + extra) to (currentY * 1.2f)
            SwipeDirection.Left -> -(cardWidth + extra) to (currentY * 1.2f)
            SwipeDirection.Up -> currentX to -(cardHeight + extra)
        }
    }

    public fun elevationDp(progress: Float, minDp: Float = 2f, maxDp: Float = 10f): Float {
        return minDp + (maxDp - minDp) * progress.coerceIn(0f, 1f)
    }
}
