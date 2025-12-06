package com.serdigital.turnapp.data.di

import java.util.UUID

data class Client(
    val id: String = UUID.randomUUID().toString(), // 🔹Pk automatica           // UUID o ID interno
    val name: String,
    val email: String,
    val phone: String? = null,
    val notes: String? = null,
    val lastHaircutPhotoUrl: String? = null,  // 🔹 Nueva: URL de Firebase Storage
    val preferredNotification: String? = null // 🔹 Nueva: mensaje predefinido editable
)

