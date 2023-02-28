package com.kevinnzou.compose.composeswipebox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kevinnzou.compose.swipebox.SwipeBox
import com.kevinnzou.compose.swipebox.SwipeDirection
import com.kevinnzou.compose.swipebox.widget.SwipeIcon
import com.kevinnzou.compose.swipebox.widget.SwipeText
import kotlinx.coroutines.launch

/**
 * Created By Kevin Zou On 2022/12/26
 */

@Composable
fun mainContent(text: String = "Main Content", onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color(148, 184, 216))
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, color = Color.White, fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RowScope.DeleteIcon(onClick: () -> Unit) {
    SwipeIcon(
        imageVector = Icons.Outlined.Delete,
        contentDescription = "Delete",
        tint = Color.White,
        background = Color(0xFFFA1E32),
        weight = 1f,
        iconSize = 20.dp
    ) {
        onClick()
    }
}

@Composable
fun RowScope.FavoriteIcon(onClick: () -> Unit) {
    SwipeIcon(
        imageVector = Icons.Outlined.Favorite,
        contentDescription = "Favorite",
        tint = Color.White,
        background = Color(0xFFFFB133),
        weight = 1f,
        iconSize = 20.dp
    ) {
        onClick()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBoxAtEnd() {
    val coroutineScope = rememberCoroutineScope()
    SwipeBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 60.dp,
        endContent = { swipeableState, endSwipeProgress ->
            DeleteIcon {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
            }
        }
    ) { _, _, _ ->
        mainContent("Swipe Left")
    }
}

@Preview
@Composable
fun PreviewSwipeBoxAtEnd() {
    SwipeBoxAtEnd()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBoxAtStart() {
    val coroutineScope = rememberCoroutineScope()
    SwipeBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.StartToEnd,
        startContentWidth = 60.dp,
        startContent = { swipeableState, endSwipeProgress ->
            FavoriteIcon {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
            }
        }
    ) { _, _, _ ->
        mainContent("Swipe Right")
    }
}

@Preview
@Composable
fun PreviewSwipeBoxAtStart() {
    SwipeBoxAtStart()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBoxAtEnd2() {
    val coroutineScope = rememberCoroutineScope()
    SwipeBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 120.dp,
        endContent = { swipeableState, endSwipeProgress ->
            FavoriteIcon {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
            }
            DeleteIcon {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
            }
        }
    ) { _, _, _ ->
        mainContent("Swipe Left Two Icons")
    }
}

@Preview
@Composable
fun PreviewSwipeBoxAtEnd2() {
    SwipeBoxAtEnd2()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBoxAtBoth() {
    val coroutineScope = rememberCoroutineScope()
    SwipeBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.Both,
        startContentWidth = 60.dp,
        startContent = { swipeableState, endSwipeProgress ->
            FavoriteIcon {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
            }
        },
        endContentWidth = 60.dp,
        endContent = { swipeableState, endSwipeProgress ->
            DeleteIcon {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
            }
        }
    ) { _, _, _ ->
        mainContent("Swipe Both Directions")
    }
}

@Preview
@Composable
fun PreviewSwipeBoxAtBoth() {
    SwipeBoxAtBoth()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBoxWithText(onSwipeStateChanged: (SwipeableState<Int>) -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    SwipeBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 140.dp,
        endContent = { swipeableState, endSwipeProgress ->
            SwipeText(background = Color(0xFFFFB133),
                weight = 1f,
                onClick = {
                    coroutineScope.launch {
                        swipeableState.animateTo(0)
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
                        swipeableState.animateTo(0)
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
    ) { state, _, _ ->
        // callback on parent when the state targetValue changes which means it is swiping to another state.
        LaunchedEffect(state.targetValue) {
            onSwipeStateChanged(state)
        }
        mainContent("Swipe Left Text")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewSwipeBoxWithText() {
    SwipeBoxWithText()
}