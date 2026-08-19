package com.tinderview.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun HeartIcon(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    filled: Boolean = true,
) {
    Canvas(modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.50f, h * 0.86f)
            cubicTo(w * 0.18f, h * 0.64f, w * 0.06f, h * 0.46f, w * 0.18f, h * 0.28f)
            cubicTo(w * 0.28f, h * 0.14f, w * 0.44f, h * 0.16f, w * 0.50f, h * 0.30f)
            cubicTo(w * 0.56f, h * 0.16f, w * 0.72f, h * 0.14f, w * 0.82f, h * 0.28f)
            cubicTo(w * 0.94f, h * 0.46f, w * 0.82f, h * 0.64f, w * 0.50f, h * 0.86f)
            close()
        }
        if (filled) drawPath(path, color) else {
            drawPath(path, color, style = Stroke(width = w * 0.08f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}

@Composable
internal fun CloseIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 20.dp) {
    Canvas(modifier.size(size)) {
        val stroke = Stroke(width = this.size.minDimension * 0.12f, cap = StrokeCap.Round)
        val pad = this.size.minDimension * 0.22f
        drawLine(color, Offset(pad, pad), Offset(this.size.width - pad, this.size.height - pad), stroke.width, StrokeCap.Round)
        drawLine(color, Offset(this.size.width - pad, pad), Offset(pad, this.size.height - pad), stroke.width, StrokeCap.Round)
    }
}

@Composable
internal fun StarIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 20.dp) {
    Canvas(modifier.size(size)) {
        val cx = this.size.width / 2f
        val cy = this.size.height / 2f
        val outer = this.size.minDimension * 0.46f
        val inner = outer * 0.42f
        val path = Path()
        repeat(8) { i ->
            val angle = Math.toRadians(-90.0 + i * 45.0)
            val r = if (i % 2 == 0) outer else inner
            val x = cx + (kotlin.math.cos(angle) * r).toFloat()
            val y = cy + (kotlin.math.sin(angle) * r).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path, color)
    }
}

@Composable
internal fun RewindIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 20.dp) {
    Canvas(modifier.size(size)) {
        val stroke = Stroke(width = this.size.minDimension * 0.11f, cap = StrokeCap.Round)
        val c = Offset(this.size.width * 0.54f, this.size.height * 0.52f)
        val r = this.size.minDimension * 0.30f
        drawArc(
            color = color,
            startAngle = 40f,
            sweepAngle = 260f,
            useCenter = false,
            topLeft = Offset(c.x - r, c.y - r),
            size = Size(r * 2, r * 2),
            style = stroke,
        )
        val tip = Offset(this.size.width * 0.22f, this.size.height * 0.28f)
        val m = this.size.minDimension
        drawPath(
            Path().apply {
                moveTo(tip.x, tip.y - m * 0.10f)
                lineTo(tip.x - m * 0.08f, tip.y + m * 0.08f)
                lineTo(tip.x + m * 0.14f, tip.y + m * 0.04f)
                close()
            },
            color,
        )
    }
}

@Composable
internal fun SparkIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 18.dp) {
    Canvas(modifier.size(size)) {
        val cx = this.size.width / 2f
        val cy = this.size.height / 2f
        val outer = this.size.minDimension * 0.48f
        val inner = outer * 0.28f
        val path = Path()
        repeat(8) { i ->
            val angle = Math.toRadians(-90.0 + i * 45.0)
            val r = if (i % 2 == 0) outer else inner
            val x = cx + (kotlin.math.cos(angle) * r).toFloat()
            val y = cy + (kotlin.math.sin(angle) * r).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path, color)
    }
}

@Composable
internal fun GridIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 18.dp) {
    Canvas(modifier.size(size)) {
        val pad = this.size.minDimension * 0.12f
        val gap = this.size.minDimension * 0.10f
        val cell = (this.size.minDimension - pad * 2 - gap) / 2f
        val radius = CornerRadius(cell * 0.22f, cell * 0.22f)
        listOf(
            Offset(pad, pad),
            Offset(pad + cell + gap, pad),
            Offset(pad, pad + cell + gap),
            Offset(pad + cell + gap, pad + cell + gap),
        ).forEach { origin ->
            drawRoundRect(color, origin, Size(cell, cell), radius)
        }
    }
}

@Composable
internal fun ChatIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 18.dp) {
    Canvas(modifier.size(size)) {
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        this@Canvas.size.width * 0.12f,
                        this@Canvas.size.height * 0.10f,
                        this@Canvas.size.width * 0.88f,
                        this@Canvas.size.height * 0.68f,
                    ),
                    cornerRadius = CornerRadius(this@Canvas.size.minDimension * 0.18f),
                ),
            )
            moveTo(this@Canvas.size.width * 0.30f, this@Canvas.size.height * 0.66f)
            lineTo(this@Canvas.size.width * 0.22f, this@Canvas.size.height * 0.90f)
            lineTo(this@Canvas.size.width * 0.50f, this@Canvas.size.height * 0.66f)
            close()
        }
        drawPath(path, color)
    }
}
