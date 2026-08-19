package com.tinderview.cardstack

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

public class CardStackMathTest {

    private val allDirections = setOf(
        SwipeDirection.Left,
        SwipeDirection.Right,
        SwipeDirection.Up,
    )

    @Test
    public fun rotationIsClamped() {
        val rotation = CardStackMath.rotationZ(1000f, divisor = 25f, maxRotation = 14f)
        assertEquals(14f, rotation, 0.01f)
    }

    @Test
    public fun dominantDirectionPrefersVerticalWhenUp() {
        assertEquals(SwipeDirection.Up, CardStackMath.dominantDirection(10f, -80f))
        assertEquals(SwipeDirection.Right, CardStackMath.dominantDirection(80f, -10f))
        assertEquals(SwipeDirection.Left, CardStackMath.dominantDirection(-80f, 4f))
    }

    @Test
    public fun progressReachesOneAtThreshold() {
        val (_, progress) = CardStackMath.progressTowardCommit(
            offsetX = 140f,
            offsetY = 0f,
            cardWidth = 400f,
            thresholdFraction = 0.35f,
            enabled = allDirections,
        )
        assertEquals(1f, progress, 0.01f)
    }

    @Test
    public fun progressIsZeroWhenDirectionDisabled() {
        val (_, progress) = CardStackMath.progressTowardCommit(
            offsetX = 0f,
            offsetY = -200f,
            cardWidth = 400f,
            thresholdFraction = 0.35f,
            enabled = setOf(SwipeDirection.Left, SwipeDirection.Right),
        )
        assertEquals(0f, progress, 0.01f)
    }

    @Test
    public fun flingCommitsEvenBelowDistanceThreshold() {
        val commit = CardStackMath.shouldCommit(
            direction = SwipeDirection.Right,
            progress = 0.2f,
            velocityX = 1200f,
            velocityY = 10f,
            flingVelocityPx = 900f,
            enabled = allDirections,
        )
        assertEquals(SwipeDirection.Right, commit)
    }

    @Test
    public fun weakFlingDoesNotCommit() {
        val commit = CardStackMath.shouldCommit(
            direction = SwipeDirection.Right,
            progress = 0.2f,
            velocityX = 200f,
            velocityY = 0f,
            flingVelocityPx = 900f,
            enabled = allDirections,
        )
        assertNull(commit)
    }

    @Test
    public fun indexAdvancesAndRewinds() {
        assertEquals(1, CardStackMath.nextIndex(0, 5))
        assertEquals(5, CardStackMath.nextIndex(5, 5))
        assertEquals(0, CardStackMath.previousIndex(0))
        assertEquals(2, CardStackMath.previousIndex(3))
    }

    @Test
    public fun rubberBandReducesDisabledAxis() {
        val raw = 200f
        val banded = CardStackMath.rubberBand(raw, allow = false)
        assertTrue(banded < raw && banded > 0f)
    }

    @Test
    public fun distanceThresholdCommitsWithoutFling() {
        val commit = CardStackMath.shouldCommit(
            direction = SwipeDirection.Left,
            progress = 1f,
            velocityX = 0f,
            velocityY = 0f,
            flingVelocityPx = 900f,
            enabled = allDirections,
        )
        assertEquals(SwipeDirection.Left, commit)
    }

    @Test
    public fun constrainDragRubberBandsDisabledUp() {
        val (x, y) = CardStackMath.constrainDrag(
            offsetX = 80f,
            offsetY = -200f,
            enabled = setOf(SwipeDirection.Left, SwipeDirection.Right),
        )
        assertEquals(80f, x, 0.01f)
        assertTrue(kotlin.math.abs(y) < 200f)
    }

    @Test
    public fun exitTargetLeavesTheScreen() {
        val (rightX, _) = CardStackMath.exitTarget(SwipeDirection.Right, 400f, 600f, 20f, 10f)
        val (leftX, _) = CardStackMath.exitTarget(SwipeDirection.Left, 400f, 600f, -20f, 10f)
        val (_, upY) = CardStackMath.exitTarget(SwipeDirection.Up, 400f, 600f, 0f, -20f)
        assertTrue(rightX > 400f)
        assertTrue(leftX < -400f)
        assertTrue(upY < -600f)
    }

    @Test
    public fun resolvedDimensionFallsBackWhenUnmeasured() {
        assertEquals(1080f, CardStackMath.resolvedDimension(0f, 1080f), 0.01f)
        assertEquals(1080f, CardStackMath.resolvedDimension(1f, 1080f), 0.01f)
        assertEquals(400f, CardStackMath.resolvedDimension(400f, 1080f), 0.01f)
    }

    @Test
    public fun progressIsHalfAtMidThreshold() {
        val (_, progress) = CardStackMath.progressTowardCommit(
            offsetX = 70f,
            offsetY = 0f,
            cardWidth = 400f,
            thresholdFraction = 0.35f,
            enabled = allDirections,
        )
        assertEquals(0.5f, progress, 0.02f)
    }
}
