package com.serdigital.turnapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
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
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Routes.Auth.route) {
            AuthScreen(navController = navController)
        }
        composable(Routes.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Routes.Home.route) {
            HomeScreen(navController = navController)
        }
//        composable(Routes.Dashboard.route) {
//            DashboardScreen(navController = navController)
//        }
        composable(Routes.AddAppointment.route) {
            AddAppointmentScreen(navController = navController)
        }
        composable(
            route = Routes.AddClient.route,
            deepLinks = listOf(navDeepLink { uriPattern = "turnapp://addClient" })
        ) {
            AddClientScreen(navController = navController)
        }
        composable(Routes.AddSpecialist.route) {
            AddSpecialistScreen(navController = navController)
        }
        composable(Routes.CalendarScreen.route) {
            CalendarScreen(navController = navController)
        }
        /*composable(Routes.AllAppointments.route) {
            AllAppointmentsScreen(navController = navController)
        }*/
    }
}