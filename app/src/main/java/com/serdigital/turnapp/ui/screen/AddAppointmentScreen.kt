package com.serdigital.turnapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.serdigital.turnapp.data.di.AddAppointmentUiState
import com.serdigital.turnapp.data.di.Client
import com.serdigital.turnapp.data.di.Specialist
import com.serdigital.turnapp.ui.component.AppTextField
import com.serdigital.turnapp.ui.component.ClientPhoto
import com.serdigital.turnapp.ui.component.CoverApp
import com.serdigital.turnapp.ui.component.DateTimePickerField
import com.serdigital.turnapp.ui.component.DropdownMenuField
import com.serdigital.turnapp.ui.component.PrimaryButton
import com.serdigital.turnapp.ui.component.SecondaryButton
import com.serdigital.turnapp.ui.theme.backgroundDark
import com.serdigital.turnapp.ui.theme.outlineDark
import com.serdigital.turnapp.ui.theme.primaryDark
import com.serdigital.turnapp.ui.theme.secondaryContainerDark
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddAppointmentScreen(
    navController: NavHostController
) {
    val uiState by remember { mutableStateOf(AddAppointmentUiState()) }
    val onClientSelected by remember { mutableStateOf({ client: Client -> Unit }) }
    val onSpecialistSelected by remember { mutableStateOf({ specialist: Specialist -> Unit }) }
    val onDateTimeSelected by remember { mutableStateOf({ dateTime: LocalDateTime -> Unit }) }
    val onReminderMinutesChanged by remember { mutableStateOf({ minutes: Int -> Unit }) }
    val onScheduleClick by remember { mutableStateOf({ }) }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = backgroundDark
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp)
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
                Column {
                    CoverApp()
                }
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 🔹 Cliente
                DropdownMenuField(
                    label = "Seleccionar Cliente",
                    items = uiState.clients,
                    selectedItem = uiState.selectedClient,
                    expanded = uiState.clientDropdownExpanded,
                    onItemSelected = { onClientSelected(it) },
                    itemLabel = { it.name }
                )

                SecondaryButton(
                    text = "Agregar Cliente",
                    onClick = { navController.navigate("add_client") },
                    modifier = Modifier.fillMaxWidth(),
                )

                // Foto último corte
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(outlineDark.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    uiState.selectedClient?.lastHaircutPhotoUrl?.let { url ->
                        ClientPhoto(
                            imageUrl = url,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    } ?: run {
                        // si no hay cliente seleccionado o no hay foto, mostrar placeholder
                        ClientPhoto(
                            imageUrl = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }
                }

                // 🔹 Especialista
                DropdownMenuField(
                    label = "Seleccionar Especialista",
                    items = uiState.specialists,
                    selectedItem = uiState.selectedSpecialist,
                    expanded = uiState.specialistDropdownExpanded,
                    onItemSelected = { onSpecialistSelected(it) },
                    itemLabel = { it.name }
                )

                // 🔹 Fecha y hora
                DateTimePickerField(
                    selectedDateTime = uiState.selectedDateTime,
                    onDateTimeSelected = onDateTimeSelected
                )

                // 🔹 Notificación predefinida
                AppTextField(
                    value = uiState.reminderMinutes.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { onReminderMinutesChanged(it) }
                    },
                    label = "Recordatorio (minutos antes)",
                    keyboardType = KeyboardType.Number
                )

                // 🔹 Botón enviar
                PrimaryButton(
                    text = "Agendar Turno",
                    onClick = onScheduleClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

val mockClient = Client(
    id = "1",
    name = "Juan Pérez",
    email = "juan.perez@example.com",
    phone = "+54 9 11 1234 5678",
    notes = "Cliente nuevo, prefiere corte clásico",
    lastHaircutPhotoUrl = "https://picsum.photos/400/300" // imagen random de prueba
)

