package com.v2ray.ang.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AutoLoadingOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier,
    title: String = "Updating configs...",
    subtitle: String = "Finding the fastest server"
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(250)),
        exit = fadeOut(animationSpec = tween(500)),
        modifier = modifier
    ) {
        val neon = Color(0xFFA855F7)
        val neonLight = Color(0xFFD8B4FE)

        val transition = rememberInfiniteTransition(label = "loading")
        val spin by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1100, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "spin"
        )
        val spinBack by transition.animateFloat(
            initialValue = 360f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "spinBack"
        )
        val pulse by transition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 900, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xF20D0D12))
                .pointerInput(Unit) { detectTapGestures { } },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(130.dp)) {
                        val stroke = 6.dp.toPx()
                        val outerD = size.minDimension - stroke * 2
                        val outerTl = Offset(stroke, stroke)
                        val outerSize = Size(outerD, outerD)

                        val innerInset = stroke * 4.5f
                        val innerD = size.minDimension - innerInset * 2
                        val innerTl = Offset(innerInset, innerInset)
                        val innerSize = Size(innerD, innerD)

                        drawArc(
                            color = neon.copy(alpha = 0.2f * pulse),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = outerTl,
                            size = outerSize,
                            style = Stroke(width = stroke)
                        )
                        drawArc(
                            color = neonLight,
                            startAngle = spin,
                            sweepAngle = 110f,
                            useCenter = false,
                            topLeft = outerTl,
                            size = outerSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = neon,
                            startAngle = spinBack,
                            sweepAngle = 70f,
                            useCenter = false,
                            topLeft = innerTl,
                            size = innerSize,
                            style = Stroke(width = stroke * 0.7f, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "MT",
                        color = neonLight,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = title,
                    color = neonLight,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    color = Color(0xFFC9B8E0),
                    fontSize = 14.sp
                )
            }
        }
    }
}
