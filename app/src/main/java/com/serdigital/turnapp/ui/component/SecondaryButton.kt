package com.serdigital.turnapp.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import com.serdigital.turnapp.ui.theme.secondaryContainerDark
import com.serdigital.turnapp.ui.theme.onSecondaryDark
import com.serdigital.turnapp.ui.theme.primaryDark

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = secondaryContainerDark,
            contentColor = onSecondaryDark
        )
    ) {
        Text(text, fontSize = 16.sp, color = primaryDark)
    }
}