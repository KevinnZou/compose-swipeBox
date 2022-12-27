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