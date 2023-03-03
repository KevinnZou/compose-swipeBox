package com.kevinnzou.compose.composeswipebox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Created By Kevin Zou On 2023/2/24
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBoxList() {
    val coroutineScope = rememberCoroutineScope()

    var currentSwipeState: SwipeableState<Int>? by remember {
        mutableStateOf(null)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            /**
             * we need to intercept the scroll event and check whether there is an open box
             * if so ,then we need to swipe that box back and reset the state
             */
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (currentSwipeState != null && currentSwipeState!!.currentValue != 0) {
                    coroutineScope.launch {
                        currentSwipeState!!.animateTo(0)
                        currentSwipeState = null
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
            SwipeBoxWithText(
                item,
                onDelete = {
                    data.remove(it)
                    currentSwipeState = null
                }
            ) {
                /**
                 * if it is swiping back and it equals to the current state
                 * it means that the current open box is closed, then we set the state to null
                 */
                if (it.targetValue == 0 && currentSwipeState == it) {
                    currentSwipeState = null
                    return@SwipeBoxWithText
                }
                // if there is no opening box, we set it to this opening one
                if (currentSwipeState == null) {
                    currentSwipeState = it
                } else {
                    // there already had one box opening, we need to swipe it back and then update the state to new one
                    coroutineScope.launch {
                        currentSwipeState!!.animateTo(0)
                        currentSwipeState = it
                    }
                }
            }
        }
    }
}