package com.s097t0r1.kode.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

object KodeTypography {
    val Meta = TextStyle(
        fontSize = 14.sp,
        color = Color(0xFF97979B)
    )

    val Subtitle = TextStyle(
        fontSize = 13.sp,
        color = Color(0xFF55555C)
    )

    val Detail = TextStyle(
        fontSize = 15.sp,
        color = Color(0xFF55555C)
    )
}