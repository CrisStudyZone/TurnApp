package com.serdigital.turnapp.data.di

import java.time.LocalDateTime

data class AddAppointmentUiState(
    val clients: List<Client> = emptyList(),
    val specialists: List<Specialist> = emptyList(),
    val selectedClient: Client? = null,
    val selectedSpecialist: Specialist? = null,
    val selectedDateTime: LocalDateTime? = null,
    val reminderMinutes: Int = 30, // Valor por defecto: 30 minutos antes
    val clientDropdownExpanded: Boolean = false,
    val specialistDropdownExpanded: Boolean = false,
) {
    val canSchedule: Boolean
        get() =
            selectedClient != null &&
                    selectedSpecialist != null &&
                    selectedDateTime != null
}
