package com.serdigital.turnapp.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.serdigital.turnapp.ui.component.AppText
import com.serdigital.turnapp.ui.component.AppTextField
import com.serdigital.turnapp.ui.component.CoverApp
import com.serdigital.turnapp.ui.component.PhotoPickerBottomSheet
import com.serdigital.turnapp.ui.component.PrimaryButton
import com.serdigital.turnapp.ui.component.SecondaryButton
import com.serdigital.turnapp.ui.theme.backgroundDark
import com.serdigital.turnapp.ui.theme.primaryDark
import com.serdigital.turnapp.ui.theme.secondaryContainerDark
import com.serdigital.turnapp.ui.viewmodel.AddClientViewModel
import com.serdigital.turnapp.utils.createImageFileUri


@Composable
fun AddClientScreen(
    navController: NavHostController,
    viewModel: AddClientViewModel = viewModel()
) {
    val uiState = viewModel.clientUiState
    val context = LocalContext.current

    var showPhotoSheet by remember { mutableStateOf(false) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onClientPhotoSelected(it.toString()) }
    }

    // Cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempPhotoUri?.let { viewModel.onClientPhotoSelected(it.toString()) }
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
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nombre
                AppTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.onNameChanged(it) },
                    label = "Nombre"
                )

                //Email
                AppTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = "Email",
                    keyboardType = KeyboardType.Email
                )

                // Teléfono
                AppTextField(
                    value = uiState.phone,
                    onValueChange = { viewModel.onPhoneChanged(it) },
                    label = "Teléfono",
                    keyboardType = KeyboardType.Phone
                )

                // Notas
                AppTextField(
                    value = uiState.notes,
                    onValueChange = { viewModel.onNotesChanged(it) },
                    label = "Notas",
                    singleLine = false,
                    maxLines = 4
                )

                // Mensaje predefinido (recordatorio)
                AppTextField(
                    value = uiState.preferredNotification,
                    onValueChange = { viewModel.onPreferredNotificationChanged(it) },
                    label = "Mensaje predefinido",
                    singleLine = false,
                    maxLines = 3
                )

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

                // Botón Guardar
                PrimaryButton(
                    text = "Guardar Cliente",
                    onClick = { viewModel.onSaveClient() },
                    modifier = Modifier.fillMaxWidth()
                )

                // Botón Cancelar
                SecondaryButton(
                    text = "Cancelar",
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // 🔹 Alerta de cliente duplicado
    uiState.duplicateClient?.let { existing ->
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
                    viewModel.onSaveClient() // forzamos guardado
                }) {
                    AppText("Crear nuevo")
                }
            },
            title = { AppText("Posible duplicado") },
            text = { AppText("Ya existe un cliente llamado ${existing.name} con el mismo email y teléfono.") }
        )
    }
}
