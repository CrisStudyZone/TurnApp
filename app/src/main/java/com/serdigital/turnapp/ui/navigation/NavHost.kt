package com.serdigital.turnapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.serdigital.turnapp.ui.screen.AddAppointmentScreen
import com.serdigital.turnapp.ui.screen.AddClientScreen
import com.serdigital.turnapp.ui.screen.AddSpecialistScreen
import com.serdigital.turnapp.ui.screen.AuthScreen
import com.serdigital.turnapp.ui.screen.CalendarScreen
import com.serdigital.turnapp.ui.screen.HomeScreen
import com.serdigital.turnapp.ui.screen.RegisterScreen
import com.serdigital.turnapp.ui.screen.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyNavigation() {
    val navController = rememberNavController() // 1. Obtener el NavController
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        composable(Routes.Splash.route) { SplashScreen(navController) }
        composable("auth") { AuthScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("add_appointment") { AddAppointmentScreen(navController) }
        composable("add_client") { AddClientScreen(navController) }
        composable("add_specialist") { AddSpecialistScreen(navController) }
        composable("calendar") { CalendarScreen(navController) }
    }
}