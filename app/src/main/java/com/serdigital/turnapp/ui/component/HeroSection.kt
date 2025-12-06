package com.serdigital.turnapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serdigital.turnapp.ui.theme.backgroundDark
import com.serdigital.turnapp.ui.theme.onSurfaceDark
import com.serdigital.turnapp.ui.theme.outlineDark
import com.serdigital.turnapp.ui.theme.primaryDark
import com.serdigital.turnapp.ui.theme.secondaryContainerDark

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp)
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        backgroundDark,
                        primaryDark.copy(alpha = 0.1f),
                        secondaryContainerDark.copy(alpha = 0.2f)
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CoverApp()

            // 🔹 Título y subtítulo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Panel",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = onSurfaceDark,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Profesional",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp
                    ),
                    color = primaryDark,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Gestiona tu barbería de manera eficiente.\nControl total de turnos y clientes.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    color = outlineDark,
                    textAlign = TextAlign.Center
                )
            }

            // 🔹 Stats rápidos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBox(value = "24", label = "Turnos Hoy")
                DividerBox()
                StatBox(value = "156", label = "Clientes")
                DividerBox()
                StatBox(value = "98%", label = "Confirmados")
            }
        }
    }
}

@Composable
fun StatBox(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = primaryDark
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = outlineDark
        )
    }
}

@Composable
fun DividerBox() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(outlineDark.copy(alpha = 0.4f))
    )
}