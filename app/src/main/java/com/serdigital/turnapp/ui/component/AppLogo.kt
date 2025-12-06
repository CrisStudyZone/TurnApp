package com.serdigital.turnapp.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.size
import com.serdigital.turnapp.R

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    @DrawableRes logoRes: Int = R.drawable.logo,
    size: Dp = 200.dp,
    contentDescription: String = "Logo de la app"
) {
    Image(
        painter = painterResource(id = logoRes),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit
    )
}