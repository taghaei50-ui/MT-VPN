package com.v2ray.ang.ui.main

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.ang.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NeonConnectButton(
    isRunning: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 120.dp
) {
    val neon = Color(0xFFA855F7)
    val neonLight = Color(0xFFD8B4FE)
    val idle = Color(0xFF4B4560)

    val transition = rememberInfiniteTransition(label = "neon")
    val pulse by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val orbit by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit"
    )

    val ringColor = if (isRunning) neonLight else idle
    val glow = if (isRunning) pulse else 0.12f
    val boxSize = buttonSize * 1.8f

    Box(
        modifier = modifier.size(boxSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(boxSize)) {
            val c = center
            val r = buttonSize.toPx() / 2f
            val orbitR = r + 22.dp.toPx()
            val glowR = orbitR + 30.dp.toPx()

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(neon.copy(alpha = 0.55f * glow), Color.Transparent),
                    center = c,
                    radius = glowR
                ),
                radius = glowR,
                center = c
            )
            drawCircle(
                color = if (isRunning) neon.copy(alpha = 0.35f) else idle.copy(alpha = 0.35f),
                radius = orbitR,
                center = c,
                style = Stroke(width = 1.5.dp.toPx())
            )
            if (isRunning) {
                val a = orbit * (PI / 180.0)
                val hx = c.x + orbitR * cos(a).toFloat()
                val hy = c.y + orbitR * sin(a).toFloat()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(neonLight, Color.Transparent),
                        center = Offset(hx, hy),
                        radius = 16.dp.toPx()
                    ),
                    radius = 16.dp.toPx(),
                    center = Offset(hx, hy)
                )
                for (i in 6 downTo 0) {
                    val ang = a - i * 0.13
                    val px = c.x + orbitR * cos(ang).toFloat()
                    val py = c.y + orbitR * sin(ang).toFloat()
                    val fade = 1f - i / 7f
                    drawCircle(
                        color = (if (i == 0) Color.White else neonLight).copy(alpha = fade),
                        radius = (2f + 3.5f * fade).dp.toPx(),
                        center = Offset(px, py)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .size(buttonSize)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF241A36), Color(0xFF0D0D12))
                    )
                )
                .border(3.dp, ringColor, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    if (isRunning) R.drawable.ic_stop_24dp else R.drawable.ic_play_24dp
                ),
                contentDescription = stringResource(
                    if (isRunning) R.string.acc_stop else R.string.acc_start
                ),
                tint = if (isRunning) Color(0xFFE9D5FF) else Color.White,
                modifier = Modifier.size(44.dp)
            )
        }
    }
}

@Composable
fun ConnectStatus(
    isRunning: Boolean,
    displayText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRunning) "CONNECTED" else "DISCONNECTED",
            color = if (isRunning) Color(0xFFD8B4FE) else Color(0xFFFF6B8A),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isRunning) "Your connection is protected"
            else "Tap the button to connect",
            color = Color(0xFFC9B8E0),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        if (isRunning && displayText.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = displayText,
                color = Color(0xFF9A8FB3),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ConnectHero(
    isRunning: Boolean,
    displayText: String,
    onToggle: () -> Unit,
    onTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NeonConnectButton(isRunning = isRunning, onClick = onToggle)
        Spacer(modifier = Modifier.height(8.dp))
        ConnectStatus(
            isRunning = isRunning,
            displayText = displayText,
            modifier = Modifier
                .clickable(onClick = onTest)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}
