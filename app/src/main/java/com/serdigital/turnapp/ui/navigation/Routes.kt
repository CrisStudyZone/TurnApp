package com.serdigital.turnapp.ui.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Auth : Routes("auth")
    object Register : Routes("register")
    object Home : Routes("home")
    object AddAppointment : Routes("add_appointment")
    object AddClient : Routes("add_client")
    object AddSpecialist : Routes("add_specialist")

    object CalendarScreen : Routes("calendar")
}
