package com.echoist.linkedout.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.echoist.linkedout.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// 배경과 thumb를 이미지로 지정할수있는 커스텀 switch 입니다.
@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ImageSwitch(
    height: Dp,
    width: Dp,
    circleButtonPadding: Dp = 4.dp,
    outerBackgroundOnResource: Int, // on 상태의 배경
    outerBackgroundOffResource: Int, // off 상태의 배경
    circleBackgroundOnResource: Int, // on 상태의 thumb
    circleBackgroundOffResource: Int, // off 상태의 thumb
    stateOn: Int = 1,
    stateOff: Int = 0,
    initialValue: Int = 0, // 초기상태 false
    onCheckedChanged: (checked: Boolean) -> Unit
) {
    val swipeableState = rememberSwipeableState(
        initialValue = initialValue,
        confirmStateChange = { newState ->
            if (newState == stateOff) {
                onCheckedChanged(false)
            } else {
                onCheckedChanged(true)
            }
            true
        }
    )


    val sizePx = with(LocalDensity.current) { (width - height).toPx() }
    val anchors = mapOf(0f to stateOff, sizePx to stateOn) // Maps anchor points (in px) to states

    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .height(height)
            .width(width)
            .clip(RoundedCornerShape(height))
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.Transparent)
            .then(
                if (swipeableState.currentValue == stateOff) Modifier.paint(
                    painterResource(id = outerBackgroundOffResource),
                    contentScale = ContentScale.FillBounds
                ) else Modifier.paint(
                    painterResource(id = outerBackgroundOnResource),
                    contentScale = ContentScale.FillBounds
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(height)
                .padding(circleButtonPadding)
                .clip(RoundedCornerShape(50))
                .then(
                    if (swipeableState.currentValue == stateOff) Modifier.paint(
                        painterResource(id = circleBackgroundOffResource),
                        contentScale = ContentScale.FillBounds
                    ) else Modifier.paint(
                        painterResource(id = circleBackgroundOnResource),
                        contentScale = ContentScale.FillBounds
                    )
                )
                .clickable {
                    scope.launch {

                        if (swipeableState.currentValue == stateOff) {
                            swipeableState.animateTo(stateOn)
                            onCheckedChanged(true)
                        } else {
                            swipeableState.animateTo(stateOff)
                            onCheckedChanged(false)

                        }

                    }


                }
        )
    }


}
@Preview
@Composable
fun PrevImageSwitch() {
        ImageSwitch(
            height = 40.dp,
            width = 80.dp,
            outerBackgroundOnResource = R.drawable.switch_body_lemon,
            outerBackgroundOffResource = R.drawable.switch_body_orange,
            circleBackgroundOnResource = R.drawable.switch_btn_fruit_lemon,
            circleBackgroundOffResource = R.drawable.switch_btn_fruit_orange,
            onCheckedChanged = { checked ->
                if (checked) Log.d("checked test", "true")
                else Log.d("checked test","false")}
        )

    }

