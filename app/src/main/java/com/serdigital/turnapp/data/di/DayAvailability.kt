package com.serdigital.turnapp.data.di

data class DayAvailability(
    val dayAvailability: DayOfWeek,
    val selectedSlots: List<TimeSlot> = emptyList()
)
