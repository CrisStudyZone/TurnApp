package com.serdigital.turnapp.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.serdigital.turnapp.R
import com.serdigital.turnapp.data.di.DayAvailability
import com.serdigital.turnapp.data.di.DayOfWeek
import com.serdigital.turnapp.data.di.Service
import com.serdigital.turnapp.data.di.SpecialistType
import com.serdigital.turnapp.data.di.TimeSlot
import com.serdigital.turnapp.ui.component.AppText
import com.serdigital.turnapp.ui.component.AppTextField
import com.serdigital.turnapp.ui.component.CoverApp
import com.serdigital.turnapp.ui.component.PhotoPickerBottomSheet
import com.serdigital.turnapp.ui.component.PrimaryButton
import com.serdigital.turnapp.ui.component.SecondaryButton
import com.serdigital.turnapp.ui.theme.backgroundDark
import com.serdigital.turnapp.ui.theme.primaryDark
import com.serdigital.turnapp.ui.theme.secondaryContainerDark
import com.serdigital.turnapp.ui.viewmodel.AddSpecialistViewModel
import com.serdigital.turnapp.utils.createImageFileUri

@Composable
fun AddSpecialistScreen(
    navController: NavHostController,
    viewModel: AddSpecialistViewModel = viewModel()
) {
    val uiState = viewModel.specialistUiState
    val context = LocalContext.current

    var showPhotoSheet by remember { mutableStateOf(false) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onSpecialistPhtoUrlChanged(it.toString()) }
    }

    // Cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempPhotoUri?.let { viewModel.onSpecialistPhtoUrlChanged(it.toString()) }
        }
    }

    fun openCamera() {
        val uri = createImageFileUri(context)
        tempPhotoUri = uri
        cameraLauncher.launch(uri)
    }

    fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundDark
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 🔹 Encabezado con CoverApp
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

            // 🔹 Formulario
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Nombre
                    AppTextField(
                        value = uiState.name,
                        onValueChange = { viewModel.onNameChanged(it) },
                        label = "Nombre"
                    )
                }

                item {
                    //Email
                    AppTextField(
                        value = uiState.email,
                        onValueChange = { viewModel.onEmailChanged(it) },
                        label = "Email",
                        keyboardType = KeyboardType.Email
                    )
                }

                item {
                    // Teléfono
                    AppTextField(
                        value = uiState.phone,
                        onValueChange = { viewModel.onPhoneChanged(it) },
                        label = "Teléfono",
                        keyboardType = KeyboardType.Phone
                    )
                }

                item {
                    // Notas
                    AppTextField(
                        value = uiState.notes,
                        onValueChange = { viewModel.onNotesChanged(it) },
                        label = "Notas",
                        singleLine = false,
                        maxLines = 4
                    )
                }

                item {
                    // Tipo de especialista
                    AppText(
                        text = "Especialidades"
                    )
                    SpecialistTypeSelector(
                        selectedTypes = uiState.types,
                        onTypeToggled = { type, selected ->
                            viewModel.onTypeChanged(type, selected)
                        }
                    )
                }

                item {
                    // Disponibilidad
                    AppText(
                        text = "Disponibilidad"
                    )
                    AvailabilitySelector(
                        availability = uiState.availability,
                        onDaySelected = { day, selected ->
                            viewModel.onDaySelected(day, selected)
                        },
                        onTimeSlotChanged = { day, slot, selected ->
                            viewModel.onTimeSlotChanged(day, slot, selected)
                        },
                        onTimeSlotSelected = { slot, selected ->
                            viewModel.onTimeSlotSelected(slot, selected)
                        }
                    )
                }

                item {
                    AppText(
                        text = "Servicios"
                    )
                    MultiSelectGrid(
                        items = Service.entries.map { it.name },
                        selectedItems = uiState.services.map { it.name },
                        onSelectionChanged = { newList ->
                            viewModel.onServicesChanged(newList.map { Service.valueOf(it) })
                        }
                    )
                }

                item {
                    // 🔹 Botones de acción
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .clickable(
                                onClick = { showPhotoSheet = true }
                            )
                    ) {
                        // Botón Agregar Foto
                        IconButton(
                            onClick = { showPhotoSheet = true },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_photo),
                                contentDescription = "Agregar foto",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        PhotoPickerBottomSheet(
                            showSheet = showPhotoSheet,
                            onDismiss = { showPhotoSheet = false },
                            onCameraClick = { openCamera() },
                            onGalleryClick = { openGallery() }
                        )
                    }
                }

                item {
                    // Botón Guardar
                    PrimaryButton(
                        text = "Guardar Profesional",
                        onClick = { viewModel.onSaveSpecialist() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    // Botón Cancelar
                    SecondaryButton(
                        text = "Cancelar",
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    // 🔹 Alerta de Especialista duplicado
    uiState.duplicateSpecialist?.let { existing ->
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    // usar el existente
                    navController.popBackStack()
                }) {
                    AppText("Usar existente")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // ignorar y crear uno nuevo
                    viewModel.onSaveSpecialist() // forzamos guardado
                }) {
                    AppText("Crear nuevo")
                }
            },
            title = { AppText("Posible duplicado") },
            text = { AppText("Ya existe un especialista llamado ${existing.name} con el mismo email y teléfono.") }
        )
    }
}

@Composable
fun MultiSelectGrid(
    items: List<String>,
    selectedItems: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    val currentSelection = remember { mutableStateListOf<String>().apply { addAll(selectedItems) } }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.heightIn(max = 200.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items) { item ->
            val isSelected = currentSelection.contains(item)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isSelected) currentSelection.remove(item) else currentSelection.add(item)
                        onSelectionChanged(currentSelection.toList())
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface,
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier.padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppText(item)
                }
            }
        }
    }
}

@Composable
fun AvailabilitySelector(
    availability: List<DayAvailability>,
    onDaySelected: (DayOfWeek, Boolean) -> Unit,
    onTimeSlotChanged: (DayOfWeek, TimeSlot, Boolean) -> Unit,
    onTimeSlotSelected: (TimeSlot, Boolean) -> Unit
) {
    var showPerDay by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Checkbox "Todos los días"
        Row(verticalAlignment = Alignment.CenterVertically) {
            val allDaysSelected = DayOfWeek.entries
                .filter { it != DayOfWeek.ALL_DAYS }
                .all { day -> availability.any { it.dayAvailability == day } }

            Checkbox(
                checked = allDaysSelected,
                onCheckedChange = { selected ->
                    DayOfWeek.entries
                        .filter { it != DayOfWeek.ALL_DAYS }
                        .forEach { day -> onDaySelected(day, selected) }
                }
            )
            AppText("Todos los días")
        }

        // ---- Global de horarios (se aplican a todos los días seleccionados) ----
        Row(verticalAlignment = Alignment.CenterVertically) {
            val allSelected = availability.isNotEmpty() &&
                    availability.all { slots ->
                        listOf(TimeSlot.MORNING, TimeSlot.AFTERNOON, TimeSlot.EVENING)
                            .all { it in slots.selectedSlots }
                    }

            Checkbox(
                checked = allSelected,
                onCheckedChange = { selected ->
                    listOf(TimeSlot.MORNING, TimeSlot.AFTERNOON, TimeSlot.EVENING).forEach { slot ->
                        onTimeSlotSelected(slot, selected)
                    }
                }
            )
            AppText("Todos los turnos (todos los días)")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = availability.all { TimeSlot.MORNING in it.selectedSlots },
                onCheckedChange = { selected -> onTimeSlotSelected(TimeSlot.MORNING, selected) }
            )
            AppText("Turno mañana (todos los días)")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = availability.all { TimeSlot.AFTERNOON in it.selectedSlots },
                onCheckedChange = { selected -> onTimeSlotSelected(TimeSlot.AFTERNOON, selected) }
            )
            AppText("Turno tarde (todos los días)")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = availability.all { TimeSlot.EVENING in it.selectedSlots },
                onCheckedChange = { selected -> onTimeSlotSelected(TimeSlot.EVENING, selected) }
            )
            AppText("Turno noche (todos los días)")
        }

        // ---- Opción para ver por día ----
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = showPerDay,
                onCheckedChange = { showPerDay = it }
            )
            AppText("Configurar días en particular")
        }

        // ---- Lista de días (solo se muestra si el usuario activó la opción) ----
        if (showPerDay) {
            DayOfWeek.entries.filter { it != DayOfWeek.ALL_DAYS }.forEach { day ->
                val isSelected = availability.any { it.dayAvailability == day }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { onDaySelected(day, it) }
                    )
                    AppText(day.name)
                }

                if (isSelected) {
                    val slots = availability.find { it.dayAvailability == day }?.selectedSlots ?: emptyList()
                    Column (
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TimeSlot.entries.forEach { slot ->
                            Row(
                                modifier = Modifier
                                    .padding(start = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = slots.contains(slot),
                                    onCheckedChange = { onTimeSlotChanged(day, slot, it) }
                                )
                                AppText(slot.name)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SpecialistTypeSelector(
    selectedTypes: List<SpecialistType>,
    onTypeToggled: (SpecialistType, Boolean) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 👈 2 columnas, ajusta a gusto
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp), // 👈 limitar altura con scroll interno
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(SpecialistType.entries.toTypedArray()) { type ->
            FilterChip(
                selected = selectedTypes.contains(type),
                onClick = {
                    val willSelect = !selectedTypes.contains(type)
                    onTypeToggled(type, willSelect)
                },
                label = { Text(type.name) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

