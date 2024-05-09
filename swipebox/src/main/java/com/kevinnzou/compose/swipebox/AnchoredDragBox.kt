package com.kevinnzou.compose.swipebox

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
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
import kotlin.math.roundToInt

/**
 * Created By Kevin Zou On 2024/2/1
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
 * 1. anchoredDraggableState: [AnchoredDraggableState], which can be used to change the swipe state.
 * 2. startSwipeProgress: [Float], which represent the progress of the swipe of the start content, 0f for null startContent.
 *
 * Note that the content will be layout in a [RowScope] with mutable width. Thus, for sub content inside it, you have to use the [weight] modifier to determine the width of the content
 * instead of use the [width] modifier directly. Also, since the width of the container will change with swipe progress, the content inside the sub container have to use the [requiredWidth]
 * modifier to avoid the abnormal recompose to that width change. Like size or visibility change.
 * For content with just icon or text, I would recommend you to use the [SwipeIcon] and [SwipeText] which setup the size restriction for you and you only need to set the real content.
 * @param endContentWidth The width of the end content which will be shown when the swipe direction is EndToStart or Both.
 * @param endContent The content of the end content.
 * Two parameters will be provided to that content:
 * 1. anchoredDraggableState: [AnchoredDraggableState], which can be used to change the swipe state.
 * 2. endSwipeProgress: [Float], which represent the progress of the swipe of the end content, 0f for null endContent.
 *
 * Note that the content will be layout in a [RowScope] with mutable width. Thus, for sub content inside it, you have to use the [weight] modifier to determine the width of the content
 * instead of use the [width] modifier directly. Also, since the width of the container will change with swipe progress, the content inside the sub container have to use the [requiredWidth]
 * modifier to avoid the abnormal recompose to that width change. Like size or visibility change.
 * For content with just icon or text, I would recommend you to use the [SwipeIcon] and [SwipeText] which setup the size restriction for you and you only need to set the real content.
 * @param content The main content that will be shown at max width when there is no swipe action.
 * It will be provided three parameters:
 * 1. anchoredDraggableState: [AnchoredDraggableState], which can be used to change the swipe state.
 * 2. startSwipeProgress: [Float], which represent the progress of the swipe of the start content, 0f for null startContent.
 * 3. endSwipeProgress: [Float], which represent the progress of the swipe of the end content, 0f for null endContent.
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun AnchoredDragBox(
    modifier: Modifier = Modifier,
    state: AnchoredDraggableState<DragAnchors> = rememberAnchoredDraggableState(),
    swipeDirection: SwipeDirection = SwipeDirection.EndToStart,
    startContentWidth: Dp = 0.dp,
    startContent: @Composable (RowScope.(anchoredDraggableState: AnchoredDraggableState<DragAnchors>, startSwipeProgress: Float) -> Unit)? = null,
    endContentWidth: Dp = 0.dp,
    endContent: @Composable (RowScope.(anchoredDraggableState: AnchoredDraggableState<DragAnchors>, endSwipeProgress: Float) -> Unit)? = null,
    content: @Composable BoxScope.(anchoredDraggableState: AnchoredDraggableState<DragAnchors>, startSwipeProgress: Float, endSwipeProgress: Float) -> Unit,
) {
    val startWidthPx = with(LocalDensity.current) { startContentWidth.toPx() }
    val endWidthPx = with(LocalDensity.current) { endContentWidth.toPx() }

    val draggableAnchors : DraggableAnchors<DragAnchors> = when (swipeDirection) {
        SwipeDirection.StartToEnd -> DraggableAnchors {
            DragAnchors.Start at startWidthPx
            DragAnchors.Center at 0f
        }

        SwipeDirection.EndToStart -> DraggableAnchors {
            DragAnchors.Center at 0f
            DragAnchors.End at -endWidthPx
        }

        SwipeDirection.Both -> DraggableAnchors {
            DragAnchors.Start at -startWidthPx
            DragAnchors.Center at 0f
            DragAnchors.End at endWidthPx
        }
    }

    state.updateAnchors(draggableAnchors)

    val offsetRange = when (swipeDirection) {
        SwipeDirection.StartToEnd -> 0f..Float.POSITIVE_INFINITY
        SwipeDirection.EndToStart -> Float.NEGATIVE_INFINITY..0f
        SwipeDirection.Both -> Float.NEGATIVE_INFINITY..Float.POSITIVE_INFINITY
    }
    val startSwipeProgress = if (state.requireOffset() > 0f) {
        (state.requireOffset() / startWidthPx).absoluteValue
    } else 0f
    val endSwipeProgress = if (state.requireOffset() < 0f) {
        (state.requireOffset() / endWidthPx).absoluteValue
    } else 0f
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
                    startContent(state, startSwipeProgress)
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
                    endContent(state, endSwipeProgress)
                }
            }
        } // Bottom Layer
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .offset {
                IntOffset(
                    state
                        .requireOffset()
                        .coerceIn(offsetRange)
                        .roundToInt(), 0
                )
            }
            .anchoredDraggable(
                state,
                Orientation.Horizontal
            )) {
            content(state, startSwipeProgress, endSwipeProgress)
        }
    }
}

enum class DragAnchors {
    Start,
    Center,
    End,
}

/**
 * Create and [remember] a [AnchoredDraggableState] with the default animation clock.
 *
 * @param initialValue The initial value of the state.
 * @param positionalThreshold The positional threshold, in px, to be used when calculating the
 * target state while a drag is in progress and when settling after the drag ends. This is the
 * distance from the start of a transition. It will be, depending on the direction of the
 * interaction, added or subtracted from/to the origin offset. It should always be a positive value.
 * @param velocityThreshold The velocity threshold (in px per second) that the end velocity has to
 * exceed in order to animate to the next state, even if the [positionalThreshold] has not been
 * reached.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending state change.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberAnchoredDraggableState(
    initialValue: DragAnchors = DragAnchors.Center,
    positionalThreshold: (distance: Float) -> Float = { distance -> distance * 0.5f },
    velocityThreshold: Dp = 100.dp,
    animationSpec: SpringSpec<Float> = SpringSpec(),
): AnchoredDraggableState<DragAnchors> {
    val density = LocalDensity.current
    return remember {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = { with(density) { velocityThreshold.toPx() } },
            animationSpec = animationSpec
        )
    }
}

/**
 * Create and [remember] a [AnchoredDraggableState] with the given key and default animation clock.
 *
 * @param key The key to be used to save and restore the state.
 * @param initialValue The initial value of the state.
 * @param positionalThreshold The positional threshold, in px, to be used when calculating the
 * target state while a drag is in progress and when settling after the drag ends. This is the
 * distance from the start of a transition. It will be, depending on the direction of the
 * interaction, added or subtracted from/to the origin offset. It should always be a positive value.
 * @param velocityThreshold The velocity threshold (in px per second) that the end velocity has to
 * exceed in order to animate to the next state, even if the [positionalThreshold] has not been
 * reached.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending state change.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberAnchoredDraggableState(
    key: Any?,
    initialValue: DragAnchors = DragAnchors.Center,
    positionalThreshold: (distance: Float) -> Float = { distance -> distance * 0.5f },
    velocityThreshold: Dp = 100.dp,
    animationSpec: SpringSpec<Float> = SpringSpec(),
): AnchoredDraggableState<DragAnchors> {
    val density = LocalDensity.current
    return remember(key) {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = { with(density) { velocityThreshold.toPx() } },
            animationSpec = animationSpec
        )
    }
}