package com.kevinnzou.compose.composeswipebox

/**
 * Created By Kevin Zou On 2024/2/1
 */

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kevinnzou.compose.swipebox.AnchoredDragBox
import com.kevinnzou.compose.swipebox.DragAnchors
import com.kevinnzou.compose.swipebox.SwipeDirection
import com.kevinnzou.compose.swipebox.rememberAnchoredDraggableState
import com.kevinnzou.compose.swipebox.widget.SwipeContent
import com.kevinnzou.compose.swipebox.widget.SwipeText
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

/**
 * Created By Kevin Zou On 2022/12/26
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDragBoxAtEnd() {
    val coroutineScope = rememberCoroutineScope()
    AnchoredDragBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 60.dp,
        endContent = { anchoredDraggableState, endSwipeProgress ->
            DeleteIcon {
                coroutineScope.launch {
                    anchoredDraggableState.animateTo(DragAnchors.Center)
                }
            }
        }
    ) { _, _, _ ->
        mainContent("Swipe Left")
    }
}

@Preview
@Composable
fun PreviewAnchoredDragBoxAtEnd() {
    AnchoredDragBoxAtEnd()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDragBoxAtStart() {
    val coroutineScope = rememberCoroutineScope()
    AnchoredDragBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.StartToEnd,
        startContentWidth = 60.dp,
        startContent = { anchoredDraggableState, endSwipeProgress ->
            FavoriteIcon {
                coroutineScope.launch {
                    anchoredDraggableState.animateTo(DragAnchors.Center)
                }
            }
        }
    ) { _, _, _ ->
        mainContent("Swipe Right")
    }
}

@Preview
@Composable
fun PreviewAnchoredDragBoxAtStart() {
    AnchoredDragBoxAtStart()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDragBoxAtEnd2() {
    val coroutineScope = rememberCoroutineScope()
    AnchoredDragBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 120.dp,
        endContent = { anchoredDraggableState, endSwipeProgress ->
            FavoriteIcon {
                coroutineScope.launch {
                    anchoredDraggableState.animateTo(DragAnchors.Center)
                }
            }
            DeleteIcon {
                coroutineScope.launch {
                    anchoredDraggableState.animateTo(DragAnchors.Center)
                }
            }
        }
    ) { _, _, _ ->
        mainContent("Swipe Left Two Icons")
    }
}

@Preview
@Composable
fun PreviewAnchoredDragBoxAtEnd2() {
    AnchoredDragBoxAtEnd2()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDragBoxAtBoth() {
    val coroutineScope = rememberCoroutineScope()
    AnchoredDragBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.Both,
        startContentWidth = 60.dp,
        startContent = { anchoredDraggableState, endSwipeProgress ->
            FavoriteIcon {
                coroutineScope.launch {
                    anchoredDraggableState.animateTo(DragAnchors.Center)
                }
            }
        },
        endContentWidth = 60.dp,
        endContent = { anchoredDraggableState, endSwipeProgress ->
            DeleteIcon {
                coroutineScope.launch {
                    anchoredDraggableState.animateTo(DragAnchors.Center)
                }
            }
        }
    ) { _, _, _ ->
        mainContent("Swipe Both Directions")
    }
}

@Preview
@Composable
fun PreviewAnchoredDragBoxAtBoth() {
    AnchoredDragBoxAtBoth()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDragBoxWithText(
    id: Int = 0,
    onDelete: (Int) -> Unit = {},
    onAnchoredDragStateChanged: (AnchoredDraggableState<DragAnchors>) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val anchoredDraggableState = rememberAnchoredDraggableState(id)
    AnchoredDragBox(
        modifier = Modifier.fillMaxWidth(),
        state = anchoredDraggableState,
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 140.dp,
        endContent = { anchoredDraggableState, endSwipeProgress ->
            SwipeText(background = Color(0xFFFFB133),
                weight = 1f,
                onClick = {
                    coroutineScope.launch {
                        anchoredDraggableState.animateTo(DragAnchors.Center)
                    }
                }) {
                Text(
                    text = "移至\n收藏夹",
                    modifier = Modifier.requiredWidth(42.dp),
                    fontSize = 12.sp, maxLines = 2,
                    textAlign = TextAlign.Center, color = Color.White, fontWeight = FontWeight.Bold,
                )
            }
            SwipeText(background = Color(0xFFFA1E32),
                weight = 1f,
                onClick = {
                    coroutineScope.launch {
                        anchoredDraggableState.animateTo(DragAnchors.Center)
                        onDelete(id)
                    }
                }) {
                Text(
                    text = "删除",
                    modifier = Modifier.requiredWidth(28.dp),
                    fontSize = 12.sp, maxLines = 1,
                    textAlign = TextAlign.Center, color = Color.White, fontWeight = FontWeight.Bold,
                )
            }
        }
    ) { _, _, _ ->
        // callback on parent when the state targetValue changes which means it is swiping to another state.
        LaunchedEffect(Unit) {
            snapshotFlow { anchoredDraggableState.targetValue }
                .drop(1) // drop the initial value
                .distinctUntilChanged()
                .collect {
                    onAnchoredDragStateChanged(anchoredDraggableState)
                }
        }
        mainContent("Swipe Left Text $id")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun PreviewAnchoredDragBoxWithText() {
    AnchoredDragBoxWithText()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDragBoxWithIconAndText(
    id: Int = 0,
    onAnchoredDragStateChanged: (AnchoredDraggableState<DragAnchors>) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val anchoredDraggableState = rememberAnchoredDraggableState(id)
    AnchoredDragBox(
        modifier = Modifier.fillMaxWidth(),
        state = anchoredDraggableState,
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 70.dp,
        endContent = { anchoredDraggableState, endSwipeProgress ->
            SwipeContent(
                onClick = {
                    coroutineScope.launch {
                        anchoredDraggableState.animateTo(DragAnchors.Center)
                    }
                },
                background = Color(0xFFFFB133)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.requiredSize(24.dp)
                    )
                    Text(
                        text = "Favorite",
                        modifier = Modifier
                            .requiredWidth(IntrinsicSize.Max),
                        color = Color.White,
                    )
                }
            }
        }
    ) { _, _, _ ->
        // callback on parent when the state targetValue changes which means it is swiping to another state.
        LaunchedEffect(Unit) {
            snapshotFlow { anchoredDraggableState.targetValue }
                .drop(1) // drop the initial value
                .distinctUntilChanged()
                .collect {
                    onAnchoredDragStateChanged(anchoredDraggableState)
                }
        }
        mainContent("Swipe Left Icon and Text")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun PreviewAnchoredDragBoxWithIconAndText() {
    AnchoredDragBoxWithIconAndText()
}