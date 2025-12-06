package com.serdigital.turnapp.data.di

import java.util.UUID

data class Specialist(
    val id: String = UUID.randomUUID().toString(),  //PK automatica                // Identificador único (UUID o generado por la DB)
    val name: String,            // Nombre completo
    val types: List<SpecialistType> = listOf(SpecialistType.HAIRDRESSER),        // Tipo de especialista (enum)
    val email: String? = null,       // Correo para vinculación con Calendar
    val phone: String? = null,       // Teléfono de contacto
    val notes: String? = null,       // Notas adicionales
    val availability: List<DayAvailability> = emptyList(), // Días/horarios disponibles
    val services: List<Service> = emptyList()    ,  // Servicios que ofrece
    val specialistPhtoUrl: String? = null  // URL de la foto del especialista
)

