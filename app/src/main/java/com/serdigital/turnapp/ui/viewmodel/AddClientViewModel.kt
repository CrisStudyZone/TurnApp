package com.serdigital.turnapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.serdigital.turnapp.data.di.Client

class AddClientViewModel: ViewModel() {

    var clientUiState by mutableStateOf(AddClientUiState())

        private set

    // 🔹 Simulación de la DB (luego la reemplazamos por Firebase / SQL)
    private val clientsInDb = mutableListOf<Client>()

    fun onClientPhotoSelected(uri: String) {
        clientUiState = clientUiState.copy(
            lastHaircutPhotoUrl = uri
        )
    }

    fun onNameChanged(newName: String) {
        clientUiState = clientUiState.copy(name = newName,)
    }

    fun onEmailChanged(newEmail: String) {
        clientUiState = clientUiState.copy(email = newEmail,)
    }

    fun onPhoneChanged(newPhone: String) {
        clientUiState = clientUiState.copy(phone = newPhone,)
    }

    fun onNotesChanged(newNotes: String) {
        clientUiState = clientUiState.copy(notes = newNotes,)
    }

    fun onPreferredNotificationChanged(newNotification: String) {
        clientUiState = clientUiState.copy(preferredNotification = newNotification,)
    }

    fun onSaveClient() {
        val duplicate = clientsInDb.find {
            it.name.equals(clientUiState.name, ignoreCase = true) &&
                    it.email.equals(clientUiState.email, ignoreCase = true) &&
                    it.phone.equals(clientUiState.phone, ignoreCase = true)
        }

        if (duplicate != null) {
            // Mostramos alerta en la UI
            clientUiState = clientUiState.copy(duplicateClient = duplicate,)
        } else {
            val newClient = Client(
                name = clientUiState.name,
                email = clientUiState.email,
                phone = clientUiState.phone,
                notes = clientUiState.notes,
                preferredNotification = clientUiState.preferredNotification
            )
            clientsInDb.add(newClient)
            clientUiState = clientUiState.copy(saved = true,)
        }
    }
}

data class AddClientUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val notes: String = "",
    val preferredNotification: String = "",
    val duplicateClient: Client? = null,
    val saved: Boolean = false,
    val lastHaircutPhotoUrl: String = ""
)