package com.serdigital.turnapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.serdigital.turnapp.data.di.DayAvailability
import com.serdigital.turnapp.data.di.DayOfWeek
import com.serdigital.turnapp.data.di.Service
import com.serdigital.turnapp.data.di.Specialist
import com.serdigital.turnapp.data.di.SpecialistType
import com.serdigital.turnapp.data.di.TimeSlot

class AddSpecialistViewModel: ViewModel() {

    var specialistUiState by mutableStateOf(AddSpecialistUiState())
        private set

    // 🔹 Simulación de la DB (luego la reemplazamos por Firebase / SQL)
    private val specialisttsInDb = mutableListOf<Specialist>()

    // helpers privados para al seleccion multiple de dias y horarios
    private fun weekDays(): List<DayOfWeek> =
        DayOfWeek.entries.filter { it != DayOfWeek.ALL_DAYS }

    private fun isAllDaysSelected(list: List<DayAvailability>): Boolean {
        val daysNeeded = weekDays()
        return daysNeeded.all { d -> list.any { it.dayAvailability == d } }
    }
    fun onNameChanged(newName: String) {
        specialistUiState = specialistUiState.copy(name = newName,)
    }

    fun onEmailChanged(newEmail: String) {
        specialistUiState = specialistUiState.copy(email = newEmail,)
    }

    fun onPhoneChanged(newPhone: String) {
        specialistUiState = specialistUiState.copy(phone = newPhone,)
    }

    fun onTypeChanged(type: SpecialistType, selected: Boolean) {
        val current = specialistUiState.types.toMutableList()
        if (selected) {
            if (type == SpecialistType.HAIRDRESSER) {
                current.clear()
                current.add(SpecialistType.HAIRDRESSER)
            } else {
                current.remove(SpecialistType.HAIRDRESSER)
                if (!current.contains(type)) current.add(type)
            }
        } else {
            current.remove(type)
            if (current.isEmpty()) current.add(SpecialistType.HAIRDRESSER)
        }
        specialistUiState = specialistUiState.copy(types = current)
    }

    //Disponibilidad
    // Llama desde la UI cuando el usuario tilda/desmarca un día o "Todos los días"
    fun onDaySelected(selectedDay: DayOfWeek, isSelected: Boolean) {
        val current = specialistUiState.availability.toMutableList()

        if (selectedDay == DayOfWeek.ALL_DAYS) {
            if (isSelected) {
                // Agregar todos los días de la semana (si no existen)
                weekDays().forEach { day ->
                    if (current.none { it.dayAvailability == day }) {
                        current.add(DayAvailability(day))
                    }
                }
            } else {
                // Desmarcar todos: eliminamos todas las entradas de día (excepto si quieres conservar alguna)
                current.removeAll { it.dayAvailability != DayOfWeek.ALL_DAYS }
                // En general es más claro: current.clear()
                current.clear()
            }
        } else {
            // caso normal: Lunes, Martes, etc.
            if (isSelected) {
                if (current.none { it.dayAvailability == selectedDay }) {
                    current.add(DayAvailability(selectedDay))
                }
            } else {
                current.removeAll { it.dayAvailability == selectedDay }
            }
        }

        // (Opcional) mantenemos orden por día para UX consistente
        val ordered = weekDays().mapNotNull { d -> current.find { it.dayAvailability == d } }
        specialistUiState = specialistUiState.copy(availability = ordered)
    }

    // Llamar cuando el usuario tilda/desmarca un horario para un día específico
    fun onTimeSlotChanged(day: DayOfWeek, slot: TimeSlot, isSelected: Boolean) {
        val current = specialistUiState.availability.toMutableList()
        val idx = current.indexOfFirst { it.dayAvailability == day }

        if (idx >= 0) {
            val dayAvailability = current[idx]
            val newSlots = dayAvailability.selectedSlots.toMutableList()
            if (isSelected) {
                if (!newSlots.contains(slot)) newSlots.add(slot)
            } else {
                newSlots.remove(slot)
            }
            current[idx] = dayAvailability.copy(selectedSlots = newSlots)
        } else {
            // Si el día no estaba seleccionado pero el usuario tilda un horario para "ese día",
            // podemos optar por crear el DayAvailability automáticamente:
            if (isSelected) {
                current.add(DayAvailability(day, listOf(slot)))
            }
        }

        // Ordenar para consistencia
        val ordered = weekDays().mapNotNull { d -> current.find { it.dayAvailability == d } }
        specialistUiState = specialistUiState.copy(availability = ordered)
    }

    // Función para aplicar (o quitar) un TimeSlot a TODOS los días seleccionados
    fun onTimeSlotSelected (slot: TimeSlot, isSelected: Boolean) {
        val current = specialistUiState.availability.toMutableList()

        // Aseguramos que si no hay días seleccionados y se aplica 'isSelected = true',
        // se creen las entradas para toda la semana (opcional — ajustá según UX)
        if (current.isEmpty() && isSelected) {
            weekDays().forEach { d ->
                current.add(DayAvailability(d, listOf(slot)))
            }
        } else {
            // Para cada día seleccionado, agregamos o removemos el slot
            weekDays().forEach { d ->
                val idx = current.indexOfFirst { it.dayAvailability == d }
                if (idx >= 0) {
                    val dayAvailability = current[idx]
                    val newSlots = dayAvailability.selectedSlots.toMutableList()
                    if (isSelected) {
                        if (!newSlots.contains(slot)) newSlots.add(slot)
                    } else {
                        newSlots.remove(slot)
                    }
                    current[idx] = dayAvailability.copy(selectedSlots = newSlots)
                } else if (isSelected) {
                    // Si el día no existía y queremos aplicar el slot a todos, creamos la entrada
                    current.add(DayAvailability(d, listOf(slot)))
                }
            }
        }
        // Ordenar para consistencia
        val ordered = weekDays().mapNotNull { d -> current.find { it.dayAvailability == d } }
        specialistUiState = specialistUiState.copy(availability = ordered)
    }

    fun onServicesChanged(newServices: List<Service>) {
        specialistUiState = specialistUiState.copy(services = newServices)
    }

    fun onNotesChanged(newNotes: String) {
        specialistUiState = specialistUiState.copy(notes = newNotes,)
    }

    fun onSpecialistPhtoUrlChanged(uri: String) {
        specialistUiState = specialistUiState.copy(
            specialistPhtoUrl = uri
        )
    }

    fun onSaveSpecialist() {
        val duplicate = specialisttsInDb.find {
            it.name.equals(specialistUiState.name, ignoreCase = true) &&
                    it.email.equals(specialistUiState.email, ignoreCase = true) &&
                    it.phone.equals(specialistUiState.phone, ignoreCase = true)
        }

        if (duplicate != null) {
            // Mostramos alerta en la UI
            specialistUiState = specialistUiState.copy(duplicateSpecialist = duplicate,)
        } else {
            val newSpecialist = Specialist(
                name = specialistUiState.name,
                email = specialistUiState.email,
                phone = specialistUiState.phone,
                types = specialistUiState.types,
                availability = specialistUiState.availability,
                services = specialistUiState.services,
                notes = specialistUiState.notes,
                specialistPhtoUrl = specialistUiState.specialistPhtoUrl,

            )
            specialisttsInDb.add(newSpecialist)
            specialistUiState = specialistUiState.copy(saved = true,)
        }
    }
}


data class AddSpecialistUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val types: List<SpecialistType> = listOf(SpecialistType.HAIRDRESSER),
    val availability: List<DayAvailability> = emptyList(),
    val services: List<Service> = emptyList(),
    val notes: String = "",
    val specialistPhtoUrl: String? = null,
    val duplicateSpecialist: Specialist? = null,
    val saved: Boolean = false,
)