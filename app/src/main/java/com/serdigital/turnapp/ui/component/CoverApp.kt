package com.serdigital.turnapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.serdigital.turnapp.R

@Composable
fun CoverApp() {
    // 🔹 Logo con aura detrás
    Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Cuts Logo",
            modifier = Modifier
                .size(220.dp)
                .padding(1.dp),
            contentScale = ContentScale.Fit
        )
    }
}