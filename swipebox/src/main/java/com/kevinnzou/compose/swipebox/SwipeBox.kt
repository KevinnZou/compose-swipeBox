package com.kevinnzou.compose.swipebox

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

/**
 * Created By Kevin Zou On 2022/12/21
 */

/**
 * Designed a box layout that you can swipe to show action boxes from both directions
 *
 * @param modifier The modifier to be applied to the SwipeBox.
 * @param state The state object to be used to control the SwipeBox.
 * @param swipeDirection The direction to swipe to. If the direction is [SwipeDirection.EndToStart], then only the endContent will be shown.
 * @param startContentWidth The width of the start content which will be shown when the swipe direction is StartToEnd or Both.
 * @param startContent The content of the start content.
 * Two parameters will be provided to that content:
 * 1. swipeableState: [SwipeableState], which can be used to change the swipe state.
 * 2. startSwipeProgress: [Float], which represent the progress of the swipe of the start content, 0f for null startContent.
 *
 * Note that the content will be layout in a [RowScope] with mutable width. Thus, for sub content inside it, you have to use the [weight] modifier to determine the width of the content
 * instead of use the [width] modifier directly. Also, since the width of the container will change with swipe progress, the content inside the sub container have to use the [requiredWidth]
 * modifier to avoid the abnormal recompose to that width change. Like size or visibility change.
 * For content with just icon or text, I would recommend you to use the [SwipeIcon] and [SwipeText] which setup the size restriction for you and you only need to set the real content.
 * @param endContentWidth The width of the end content which will be shown when the swipe direction is EndToStart or Both.
 * @param endContent The content of the end content.
 * Two parameters will be provided to that content:
 * 1. swipeableState: [SwipeableState], which can be used to change the swipe state.
 * 2. endSwipeProgress: [Float], which represent the progress of the swipe of the end content, 0f for null endContent.
 *
 * Note that the content will be layout in a [RowScope] with mutable width. Thus, for sub content inside it, you have to use the [weight] modifier to determine the width of the content
 * instead of use the [width] modifier directly. Also, since the width of the container will change with swipe progress, the content inside the sub container have to use the [requiredWidth]
 * modifier to avoid the abnormal recompose to that width change. Like size or visibility change.
 * For content with just icon or text, I would recommend you to use the [SwipeIcon] and [SwipeText] which setup the size restriction for you and you only need to set the real content.
 * @param thresholds Specifies where the thresholds between the states are. The thresholds will be used to determine which state to animate to when swiping stops. This is represented
 * as a lambda that takes two states and returns the threshold between them in the form of a [ThresholdConfig].
 * Note that the order of the states corresponds to the swipe direction.
 *
 * @param content The main content that will be shown at max width when there is no swipe action.
 * It will be provided three parameters:
 * 1. swipeableState: [SwipeableState], which can be used to change the swipe state.
 * 2. startSwipeProgress: [Float], which represent the progress of the swipe of the start content, 0f for null startContent.
 * 3. endSwipeProgress: [Float], which represent the progress of the swipe of the end content, 0f for null endContent.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBox(
    modifier: Modifier = Modifier,
    state: SwipeableState<Int> = rememberSwipeableState(initialValue = 0),
    swipeDirection: SwipeDirection = SwipeDirection.EndToStart,
    startContentWidth: Dp = 0.dp,
    startContent: @Composable (RowScope.(swipeableState: SwipeableState<Int>, startSwipeProgress: Float) -> Unit)? = null,
    endContentWidth: Dp = 0.dp,
    endContent: @Composable (RowScope.(swipeableState: SwipeableState<Int>, endSwipeProgress: Float) -> Unit)? = null,
    thresholds: (from: Int, to: Int) -> ThresholdConfig = { _, _ -> FixedThreshold(12.dp) },
    content: @Composable BoxScope.(swipeableState: SwipeableState<Int>, startSwipeProgress: Float, endSwipeProgress: Float) -> Unit,
) {
    val swipeableState = state
    val startWidthPx = with(LocalDensity.current) { startContentWidth.toPx() }
    val endWidthPx = with(LocalDensity.current) { endContentWidth.toPx() }
    val anchors = when (swipeDirection) {
        SwipeDirection.StartToEnd -> mapOf(
            0f to 0,
            startWidthPx to 1
        )
        SwipeDirection.EndToStart -> mapOf(
            0f to 0,
            -endWidthPx to 1
        )
        SwipeDirection.Both -> mapOf(
            0f to 0,
            startWidthPx to 1,
            -endWidthPx to 2
        )
    }

    val offsetRange = when (swipeDirection) {
        SwipeDirection.StartToEnd -> 0f..Float.POSITIVE_INFINITY
        SwipeDirection.EndToStart -> Float.NEGATIVE_INFINITY..0f
        SwipeDirection.Both -> Float.NEGATIVE_INFINITY..Float.POSITIVE_INFINITY
    }
    val startSwipeProgress by animateFloatAsState(
        targetValue = if (swipeableState.offset.value > 0f) {
            (swipeableState.offset.value / startWidthPx).absoluteValue
        } else 0f
    )
    val endSwipeProgress by animateFloatAsState(
        targetValue = if (swipeableState.offset.value < 0f) {
            (swipeableState.offset.value / endWidthPx).absoluteValue
        } else 0f
    )
    val startContentLiveWidth = startContentWidth * startSwipeProgress
    val endContentLiveWidth = endContentWidth * endSwipeProgress
    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clipToBounds()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = when (swipeDirection) {
                SwipeDirection.StartToEnd -> Arrangement.Start
                SwipeDirection.EndToStart -> Arrangement.End
                SwipeDirection.Both -> Arrangement.SpaceBetween
            }
        ) {
            if (swipeDirection in listOf(
                    SwipeDirection.StartToEnd,
                    SwipeDirection.Both
                ) && startContent != null
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(startContentLiveWidth)
                        .clipToBounds()
                ) {
                    startContent(swipeableState, startSwipeProgress)
                }
            }
            if (swipeDirection in listOf(
                    SwipeDirection.EndToStart,
                    SwipeDirection.Both
                ) && endContent != null
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(endContentLiveWidth)
                        .clipToBounds()
                ) {
                    endContent(swipeableState, endSwipeProgress)
                }
            }
        } // Bottom Layer
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .offset {
                IntOffset(
                    swipeableState.offset.value
                        .coerceIn(offsetRange)
                        .toInt(), 0
                )
            }
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                thresholds = thresholds
            )) {
            content(swipeableState, startSwipeProgress, endSwipeProgress)
        }
    }
}

enum class SwipeDirection {
    StartToEnd, EndToStart, Both
}