package com.kevinnzou.compose.swipebox.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Created By Kevin Zou On 2022/12/24
 *
 * Wrap the real content into a box with weight set and align the content at center
 *
 * @param background the background color of the box
 * @param weight the weight of the box
 * @param onClick the action to be executed when the box is clicked
 * @param content the real content of the box
 */
@Composable
fun RowScope.SwipeContent(
    background: Color = Color.White,
    weight: Float = 1f,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    require(weight > 0.0) { "invalid weight $weight; It must be greater than zero" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .weight(weight)
            .background(background)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}