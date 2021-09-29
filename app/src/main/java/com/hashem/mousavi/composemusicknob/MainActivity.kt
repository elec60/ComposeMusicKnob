package com.hashem.mousavi.composemusicknob

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hashem.mousavi.composemusicknob.ui.theme.ComposeMusicKnobTheme
import kotlin.math.PI
import kotlin.math.atan2

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMusicKnobTheme {
                // A surface container using the 'background' color from the theme
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Knob()
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun Knob(
    radius: Dp = 100.dp,
    knobHeight: Dp = 13.dp,
    knobWidth: Dp = 4.dp,
    knobsCount: Int = 35,
    limitingAngle: Int = 20
) {

    var touchX by remember {
        mutableStateOf(0f)
    }

    var touchY by remember {
        mutableStateOf(0f)
    }

    var centerX by remember {
        mutableStateOf(0f)
    }

    var centerY by remember {
        mutableStateOf(0f)
    }

    var rotation by remember {
        mutableStateOf(10f)
    }

    Box(modifier = Modifier.size(radius.times(2) + knobHeight * 2)) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .onGloballyPositioned {
                    val bound = it.boundsInWindow()
                    centerX = bound.size.width / 2
                    centerY = bound.size.height / 2
                }
                .pointerInteropFilter() { event ->
                    touchX = event.x
                    touchY = event.y

                    val angle = -atan2(centerX - touchX, centerY - touchY) * 180f / PI.toFloat()

                    val b = when (event.action) {
                        MotionEvent.ACTION_DOWN,
                        MotionEvent.ACTION_MOVE -> {
                            if (angle >= limitingAngle / 2 || angle <= -limitingAngle / 2+3)
                                rotation = if (angle < 0) angle + 360 else angle
                            true
                        }
                        else -> false
                    }
                    b
                },
        ) {

            drawCircle(
                brush = Brush.radialGradient(Pair(0f, Color.LightGray), Pair(1F, Color.Black)),
                radius = radius.toPx(),
            )

            val step = (360 - limitingAngle) / (knobsCount - 1)

            for (degree in (limitingAngle / 2)..(360 - limitingAngle / 2) step step) {
                rotate(degrees = degree.toFloat()) {
                    drawLine(
                        color = if (rotation >= degree) Color.Green else Color.LightGray,
                        start = Offset(size.width / 2, -knobHeight.toPx()),
                        end = Offset(size.width / 2, knobHeight.toPx()),
                        strokeWidth = knobWidth.toPx()
                    )
                }
            }

            rotate(rotation) {
                Log.d("ljdwhjndw", "$rotation")
                drawCircle(
                    color = Color.Red,
                    radius = 10.dp.toPx(),
                    center = Offset(
                        x = size.width / 2,
                        y = knobHeight.toPx() + 14.dp.toPx()
                    )
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeMusicKnobTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Knob()
        }
    }
}