package com.tinderview.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

private fun DrawScope.stroke(width: Float = size.minDimension * 0.09f) = Stroke(
    width = width,
    cap = StrokeCap.Round,
    join = StrokeJoin.Round,
)

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
            moveTo(w * 0.50f, h * 0.84f)
            cubicTo(w * 0.22f, h * 0.66f, w * 0.10f, h * 0.50f, w * 0.18f, h * 0.32f)
            cubicTo(w * 0.24f, h * 0.18f, w * 0.40f, h * 0.16f, w * 0.50f, h * 0.30f)
            cubicTo(w * 0.60f, h * 0.16f, w * 0.76f, h * 0.18f, w * 0.82f, h * 0.32f)
            cubicTo(w * 0.90f, h * 0.50f, w * 0.78f, h * 0.66f, w * 0.50f, h * 0.84f)
            close()
        }
        drawPath(path, color, style = if (filled) Fill else stroke())
    }
}

@Composable
internal fun CloseIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 20.dp) {
    Canvas(modifier.size(size)) {
        val pad = this.size.minDimension * 0.24f
        val s = stroke()
        drawLine(color, Offset(pad, pad), Offset(this.size.width - pad, this.size.height - pad), s.width, s.cap)
        drawLine(color, Offset(this.size.width - pad, pad), Offset(pad, this.size.height - pad), s.width, s.cap)
    }
}

@Composable
internal fun StarIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 20.dp) {
    Canvas(modifier.size(size)) {
        val cx = this.size.width / 2f
        val cy = this.size.height / 2f
        val outer = this.size.minDimension * 0.46f
        val inner = outer * 0.44f
        val path = Path()
        repeat(10) { i ->
            val angle = Math.toRadians(-90.0 + i * 36.0)
            val r = if (i % 2 == 0) outer else inner
            val x = cx + (cos(angle) * r).toFloat()
            val y = cy + (sin(angle) * r).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path, color)
    }
}

@Composable
internal fun RewindIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 20.dp) {
    Canvas(modifier.size(size)) {
        val s = stroke()
        val m = this.size.minDimension
        val c = Offset(this.size.width * 0.56f, this.size.height * 0.52f)
        val r = m * 0.30f
        drawArc(
            color = color,
            startAngle = 50f,
            sweepAngle = 250f,
            useCenter = false,
            topLeft = Offset(c.x - r, c.y - r),
            size = Size(r * 2, r * 2),
            style = s,
        )
        val tip = Offset(this.size.width * 0.24f, this.size.height * 0.26f)
        drawPath(
            Path().apply {
                moveTo(tip.x - m * 0.02f, tip.y - m * 0.12f)
                lineTo(tip.x - m * 0.14f, tip.y + m * 0.04f)
                lineTo(tip.x + m * 0.10f, tip.y + m * 0.06f)
                close()
            },
            color,
        )
    }
}

@Composable
internal fun SearchIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 18.dp) {
    Canvas(modifier.size(size)) {
        val s = stroke()
        val m = this.size.minDimension
        val r = m * 0.28f
        val c = Offset(m * 0.42f, m * 0.42f)
        drawCircle(color, r, c, style = s)
        drawLine(
            color,
            Offset(c.x + r * 0.72f, c.y + r * 0.72f),
            Offset(m * 0.82f, m * 0.82f),
            s.width,
            StrokeCap.Round,
        )
    }
}

@Composable
internal fun CardsIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 18.dp) {
    Canvas(modifier.size(size)) {
        val s = stroke(this.size.minDimension * 0.085f)
        val m = this.size.minDimension
        drawRoundRect(
            color = color,
            topLeft = Offset(m * 0.28f, m * 0.16f),
            size = Size(m * 0.52f, m * 0.64f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(m * 0.10f),
            style = s,
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(m * 0.16f, m * 0.26f),
            size = Size(m * 0.52f, m * 0.64f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(m * 0.10f),
            style = s,
        )
    }
}

@Composable
internal fun ChatIcon(color: Color, modifier: Modifier = Modifier, size: Dp = 18.dp) {
    Canvas(modifier.size(size)) {
        val s = stroke()
        val m = this.size.minDimension
        drawRoundRect(
            color = color,
            topLeft = Offset(m * 0.14f, m * 0.16f),
            size = Size(m * 0.72f, m * 0.50f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(m * 0.16f),
            style = s,
        )
        drawLine(
            color,
            Offset(m * 0.32f, m * 0.64f),
            Offset(m * 0.24f, m * 0.84f),
            s.width,
            StrokeCap.Round,
        )
        drawLine(
            color,
            Offset(m * 0.24f, m * 0.84f),
            Offset(m * 0.48f, m * 0.64f),
            s.width,
            StrokeCap.Round,
        )
    }
}
