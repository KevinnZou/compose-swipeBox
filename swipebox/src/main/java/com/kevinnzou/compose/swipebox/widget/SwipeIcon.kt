package com.kevinnzou.compose.swipebox.widget

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created By Kevin Zou On 2022/12/23
 *
 * Wrap the Icon into the [SwipeContent] with required size so that the icon size will
 * not change with the outside container
 *
 */
@Composable
fun RowScope.SwipeIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    background: Color = Color.White,
    iconSize: Dp = 16.dp,
    weight: Float = 1.0f,
    onClick: () -> Unit,
) {
    SwipeContent(
        background = background,
        weight = weight,
        onClick = onClick
    ) {
        Icon(
            imageVector,
            contentDescription,
            modifier = Modifier.requiredSize(iconSize),
            tint = tint
        )
    }
}

@Composable
fun RowScope.SwipeIcon(
    background: Color = Color.White,
    painter: Painter,
    contentDescription: String?,
    iconSize: Dp = 16.dp,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    weight: Float = 1.0f,
    onClick: () -> Unit,
) {
    SwipeContent(
        background = background,
        weight = weight,
        onClick = onClick
    ) {
        Icon(
            painter,
            contentDescription,
            modifier = Modifier.requiredSize(iconSize),
            tint = tint
        )
    }
}

@Composable
fun RowScope.SwipeIcon(
    background: Color = Color.White,
    bitmap: ImageBitmap,
    contentDescription: String?,
    iconSize: Dp = 16.dp,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    weight: Float = 1.0f,
    onClick: () -> Unit,
) {
    SwipeContent(
        background = background,
        weight = weight,
        onClick = onClick
    ) {
        Icon(
            bitmap,
            contentDescription,
            modifier = Modifier.requiredSize(iconSize),
            tint = tint
        )
    }
}
