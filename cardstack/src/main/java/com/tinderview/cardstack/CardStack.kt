package com.tinderview.cardstack

import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key as composeKey
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.positionChange
import kotlin.math.min
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
public fun <T> CardStack(
    items: List<T>,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    state: CardStackState = rememberCardStackState { items.size },
    properties: CardStackProperties = CardStackDefaults.properties(),
    onSwiped: (T, SwipeDirection) -> Unit = { _, _ -> },
    onEmpty: () -> Unit = {},
    onTopCardClick: (T) -> Unit = {},
    likeOverlay: @Composable (Float) -> Unit = { CardStackDefaults.LikeStamp(it) },
    passOverlay: @Composable (Float) -> Unit = { CardStackDefaults.PassStamp(it) },
    superLikeOverlay: @Composable (Float) -> Unit = { CardStackDefaults.SuperLikeStamp(it) },
    content: @Composable (T) -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val cardShape = remember { RoundedCornerShape(24.dp) }

    val onSwipedState = rememberUpdatedState(onSwiped)
    val onEmptyState = rememberUpdatedState(onEmpty)
    val onClickState = rememberUpdatedState(onTopCardClick)
    val itemsState = rememberUpdatedState(items)

    state.properties = properties
    state.itemCountProvider = { items.size }
    state.reduceMotion = remember(context) {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f,
        ) == 0f
    }

    var notifiedEmpty by remember { mutableStateOf(false) }
    LaunchedEffect(state.currentIndex, items.size) {
        if (state.currentIndex >= items.size && items.isNotEmpty() && !notifiedEmpty) {
            notifiedEmpty = true
            onEmptyState.value()
        }
        if (state.currentIndex < items.size) {
            notifiedEmpty = false
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()
        state.updateCardSize(Size(widthPx, heightPx))

        val start = state.currentIndex
        val lastVisible = min(start + properties.visibleCount - 1, items.lastIndex)
        val stackOffsetPx = with(density) { properties.stackOffset.toPx() }

        if (items.isEmpty() || start > items.lastIndex) {
            return@BoxWithConstraints
        }

        for (index in lastVisible downTo start) {
            val item = items[index]
            val depth = index - start
            val isTop = depth == 0
            composeKey(key(item)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex((properties.visibleCount - depth).toFloat())
                    .graphicsLayer {
                        val progress = state.progressToward(
                            properties.enabledDirections,
                            properties.thresholdFraction,
                        ).second
                        val rise = if (isTop) 0f else progress
                        val scale = 1f - depth * properties.stackScaleStep + rise * properties.stackScaleStep
                        val y = stackOffsetPx * depth * (1f - rise)
                        translationY = y
                        scaleX = scale
                        scaleY = scale
                        if (isTop) {
                            translationX = state.offset.value.x
                            translationY += state.offset.value.y
                            rotationZ = CardStackMath.rotationZ(
                                translationX = state.offset.value.x,
                                divisor = properties.rotationDivisor,
                                maxRotation = properties.maxRotationZ,
                            )
                            shadowElevation = CardStackMath.elevationDp(progress)
                            shape = cardShape
                            clip = false
                        } else {
                            shadowElevation = 2f
                            shape = cardShape
                            clip = false
                        }
                    }
                    .then(
                        if (isTop) {
                            Modifier
                                .semantics {
                                    contentDescription = "Top card"
                                    customActions = listOf(
                                        CustomAccessibilityAction("Like") {
                                            scope.launch { commitSwipe(state, SwipeDirection.Right, itemsState.value, onSwipedState.value) }
                                            true
                                        },
                                        CustomAccessibilityAction("Pass") {
                                            scope.launch { commitSwipe(state, SwipeDirection.Left, itemsState.value, onSwipedState.value) }
                                            true
                                        },
                                        CustomAccessibilityAction("Super like") {
                                            scope.launch { commitSwipe(state, SwipeDirection.Up, itemsState.value, onSwipedState.value) }
                                            true
                                        },
                                        CustomAccessibilityAction("Rewind") {
                                            scope.launch { state.rewind() }
                                            true
                                        },
                                    )
                                }
                                .pointerInput(properties, items.size, state.currentIndex) {
                                    awaitEachGesture {
                                        val down = awaitFirstDown(requireUnconsumed = false)
                                        if (state.isAnimating) return@awaitEachGesture
                                        val tracker = VelocityTracker()
                                        tracker.addPosition(down.uptimeMillis, down.position)
                                        var total = Offset.Zero
                                        var dragging = false
                                        var crossed = false
                                        try {
                                            while (true) {
                                                val event = awaitPointerEvent()
                                                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                                if (change.changedToUpIgnoreConsumed()) {
                                                    if (!dragging) {
                                                        itemsState.value.getOrNull(state.currentIndex)?.let {
                                                            onClickState.value(it)
                                                        }
                                                    } else {
                                                        val velocity = tracker.calculateVelocity()
                                                        val (dir, progress) = state.progressToward(
                                                            properties.enabledDirections,
                                                            properties.thresholdFraction,
                                                        )
                                                        val commit = CardStackMath.shouldCommit(
                                                            direction = dir,
                                                            progress = progress,
                                                            velocityX = velocity.x,
                                                            velocityY = velocity.y,
                                                            flingVelocityPx = state.flingVelocityPx(properties, density),
                                                            enabled = properties.enabledDirections,
                                                        )
                                                        if (commit != null) {
                                                            if (properties.enableHaptics) {
                                                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            }
                                                            val itemAtTop = itemsState.value.getOrNull(state.currentIndex)
                                                            scope.launch {
                                                                state.throwOff(commit, Offset(velocity.x, velocity.y))
                                                                if (itemAtTop != null) onSwipedState.value(itemAtTop, commit)
                                                            }
                                                        } else {
                                                            scope.launch { state.snapBack() }
                                                        }
                                                    }
                                                    break
                                                }
                                                val delta = change.positionChange()
                                                total += delta
                                                tracker.addPosition(change.uptimeMillis, change.position)
                                                if (!dragging && total.getDistance() > viewConfiguration.touchSlop) {
                                                    dragging = true
                                                }
                                                if (dragging) {
                                                    change.consume()
                                                    val (cx, cy) = CardStackMath.constrainDrag(
                                                        total.x,
                                                        total.y,
                                                        properties.enabledDirections,
                                                    )
                                                    scope.launch { state.dragTo(Offset(cx, cy)) }
                                                    val progress = state.progressToward(
                                                        properties.enabledDirections,
                                                        properties.thresholdFraction,
                                                    ).second
                                                    if (properties.enableHaptics && !crossed && progress >= 1f) {
                                                        crossed = true
                                                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    }
                                                    if (progress < 1f) crossed = false
                                                }
                                            }
                                        } catch (_: CancellationException) {
                                            scope.launch { state.snapBack() }
                                        }
                                    }
                                }
                        } else {
                            Modifier
                        },
                    ),
            ) {
                content(item)
                if (isTop) {
                    OverlayLayer(
                        state = state,
                        properties = properties,
                        likeOverlay = likeOverlay,
                        passOverlay = passOverlay,
                        superLikeOverlay = superLikeOverlay,
                    )
                }
            }
            }
        }
    }
}

@Composable
private fun OverlayLayer(
    state: CardStackState,
    properties: CardStackProperties,
    likeOverlay: @Composable (Float) -> Unit,
    passOverlay: @Composable (Float) -> Unit,
    superLikeOverlay: @Composable (Float) -> Unit,
) {
    val (direction, progress) = state.progressToward(
        properties.enabledDirections,
        properties.thresholdFraction,
    )
    if (properties.enableColorWash && direction != null && progress > 0f) {
        val wash = when (direction) {
            SwipeDirection.Right -> Color(0xFF2ECC71)
            SwipeDirection.Left -> Color(0xFFE74C3C)
            SwipeDirection.Up -> Color(0xFF3498DB)
        }
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = progress * 0.12f }
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            if (direction == SwipeDirection.Left) wash else Color.Transparent,
                            if (direction == SwipeDirection.Up) wash else Color.Transparent,
                            if (direction == SwipeDirection.Right) wash else Color.Transparent,
                        ),
                    ),
                ),
        )
    }
    val likeProgress = if (direction == SwipeDirection.Right) progress else 0f
    val passProgress = if (direction == SwipeDirection.Left) progress else 0f
    val superProgress = if (direction == SwipeDirection.Up) progress else 0f
    if (likeProgress > 0f) likeOverlay(likeProgress)
    if (passProgress > 0f) passOverlay(passProgress)
    if (superProgress > 0f) superLikeOverlay(superProgress)
}

private suspend fun <T> commitSwipe(
    state: CardStackState,
    direction: SwipeDirection,
    items: List<T>,
    onSwiped: (T, SwipeDirection) -> Unit,
) {
    val item = items.getOrNull(state.currentIndex) ?: return
    state.swipe(direction)
    onSwiped(item, direction)
}
