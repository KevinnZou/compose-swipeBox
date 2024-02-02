package com.kevinnzou.compose.composeswipebox

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.kevinnzou.compose.swipebox.DragAnchors
import kotlinx.coroutines.launch

/**
 * Created By Kevin Zou On 2024/2/1
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDragBoxList() {
    val coroutineScope = rememberCoroutineScope()

    var currentDragState: AnchoredDraggableState<DragAnchors>? by remember {
        mutableStateOf(null)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            /**
             * we need to intercept the scroll event and check whether there is an open box
             * if so ,then we need to swipe that box back and reset the state
             */
            /**
             * we need to intercept the scroll event and check whether there is an open box
             * if so ,then we need to swipe that box back and reset the state
             */
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (currentDragState != null && currentDragState!!.currentValue != DragAnchors.Center) {
                    coroutineScope.launch {
                        currentDragState!!.animateTo(DragAnchors.Center)
                        currentDragState = null
                    }
                }
                return Offset.Zero
            }
        }
    }

    val data = remember {
        mutableStateListOf<Int>().apply {
            (1 until 20).forEach {
                add(it)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .nestedScroll(nestedScrollConnection)
    ) {
        itemsIndexed(data) { index, item ->
            Spacer()
            Log.d("KZ", "AnchoredDragBoxWithText")
            AnchoredDragBoxWithText(
                item,
                onDelete = {
                    data.remove(it)
                    currentDragState = null
                }
            ) {
                Log.d("KZ", "AnchoredDragBoxList $index: ${it.targetValue}, ${it}, $currentDragState ${currentDragState?.currentValue} ${currentDragState?.targetValue}")
                /**
                 * Init State, we don't need to do anything
                 */
                if (it.offset == 0f) {
                    Log.d("KZ", "AnchoredDragBoxList $index: Init State ${it.offset}")
                    return@AnchoredDragBoxWithText
                }
                /**
                 * if it is swiping back, we don't need to do anything
                 */
                if (it.targetValue == DragAnchors.Center) {
                    Log.d("KZ", "AnchoredDragBoxList $index: Swiping Back")
                    return@AnchoredDragBoxWithText
                }
                // if there is no opening box, we set it to this opening one
                if (currentDragState == null || (currentDragState!!.currentValue == DragAnchors.Center && currentDragState!!.targetValue == DragAnchors.Center)) {
                    Log.d("KZ", "AnchoredDragBoxList $index: Set State to $it")
                    currentDragState = it
                } else {
                    Log.d("KZ", "AnchoredDragBoxList $index: change $currentDragState to $it")
                    val lastState = currentDragState
                    currentDragState = it
                    // there already had one box opening, we need to swipe it back and then update the state to new one
                    if (lastState?.targetValue != DragAnchors.Center) {
                        coroutineScope.launch {
                            lastState?.animateTo(DragAnchors.Center)
                        }
                    }
                }
                Log.d("KZ", "AnchoredDragBoxList $index: Last State $currentDragState")
            }
        }
    }
}