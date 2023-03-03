# compose-swipeBox

This Library provides a composable widget SwipeBox that can be swiped left or right to show the
action buttons. It supports the custom designs for the action buttons. It also provides the
composable widgets SwipeIcon and SwipeText for easy design of action buttons.

<img src="media/swipebox.gif" width=300> <img src="media/swipeboxlist.gif" width=300>

# Usage

The core component of this library is
the [SwipeBox](https://github.com/KevinnZou/compose-swipeBox/blob/main/swipebox/src/main/java/com/kevinnzou/compose/swipebox/SwipeBox.kt)
Please refer to the comment at the top of this file for detail usage. Also, you can refer
to [SwipeBoxExample](https://github.com/KevinnZou/compose-swipeBox/blob/main/app/src/main/java/com/kevinnzou/compose/composeswipebox/SwipeBoxExample.kt)
for more examples.

At all, it is very easy to use:

```kotlin
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBoxAtEnd() {
    val coroutineScope = rememberCoroutineScope()
    SwipeBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 60.dp,
        endContent = { swipeableState, endSwipeProgress ->
            SwipeIcon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                background = Color(0xFFFA1E32),
                weight = 1f,
                iconSize = 20.dp
            ) {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
            }
        }
    ) { _, _, _ ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(Color(148, 184, 216)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Swipe Left", color = Color.White, fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
```

For SwipeBox which support to swipe at both directions:

```kotlin
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBoxAtBoth() {
    val coroutineScope = rememberCoroutineScope()
    SwipeBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.Both,
        startContentWidth = 60.dp,
        startContent = { swipeableState, endSwipeProgress ->
            SwipeIcon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "Favorite",
                tint = Color.White,
                background = Color(0xFFFFB133),
                weight = 1f,
                iconSize = 20.dp
            ) {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
            }
        },
        endContentWidth = 60.dp,
        endContent = { swipeableState, endSwipeProgress ->
            SwipeIcon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                background = Color(0xFFFA1E32),
                weight = 1f,
                iconSize = 20.dp
            ) {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                }
            }
        }
    ) { _, _, _ ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(Color(148, 184, 216)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Swipe Both Directions", color = Color.White, fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
```

## List
Normally, this widget will be used in a list which need it to swipe back when the list start to scroll or another box swipe out. 
This widget is designed to support that feature but needs extra implementations. 

First, we need to define a mutablestate which is the SwipeableState of the current opeing swipebox so that we can control it later.
```kotlin
var currentSwipeState: SwipeableState<Int>? by remember {
    mutableStateOf(null)
}
```

Second, we need to define a nestedScrollConnection and set it to the modifier nestedscroll of the list so that we can intercept the scroll event
and make the current opeing box swipe backward.
```kotlin
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

LazyColumn(
    modifier = Modifier
        .nestedScroll(nestedScrollConnection)
)
```

Finally, we need to define a callback and pass it to the swipebox so that we can get informed
when swipebox start to swipe and then update the currentSwipeState
```kotlin
val onSwipeStateChanged = { state : SwipeableState<Int> ->
    /**
     * if it is swiping back and it equals to the current state
     * it means that the current open box is closed, then we set the state to null
     */
    if (state.targetValue == 0 && currentSwipeState == state) {
        currentSwipeState = null
    }
    // if there is no opening box, we set it to this opening one
    else if (currentSwipeState == null) {
        currentSwipeState = state
    } else {
        // there already had one box opening, we need to swipe it back and then update the state to new one
        coroutineScope.launch {
            currentSwipeState!!.animateTo(0)
            currentSwipeState = state
        }
    }

}


SwipeBox(onSwipeStateChanged){  state, _, _ ->
    // callback on parent when the state targetValue changes which means it is swiping to another state.
    LaunchedEffect(state.targetValue) {
        onSwipeStateChanged(state)
    }
}
```
After that, your list will react to the list scroll and update the swipeboxs' states.
For full example, please refer to [SwipeBoxList](https://github.com/KevinnZou/compose-swipeBox/blob/main/app/src/main/java/com/kevinnzou/compose/composeswipebox/SwipeBoxList.kt)

# Download

The Current Release Version is 1.1.0. For future release, please refer to the release session of the
github repository.

``` kotlin
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
    implementation("com.github.kevinnzou:compose-swipeBox:1.1.0")
}

```

# License

Compose SwipeBox is distributed under the terms of the Apache License (Version 2.0). See
the [license](https://github.com/KevinnZou/compose-swipeBox/blob/main/LICENSE) for more information.
