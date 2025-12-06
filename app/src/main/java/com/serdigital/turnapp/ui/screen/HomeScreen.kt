package com.serdigital.turnapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.serdigital.turnapp.ui.component.ActionButtons
import com.serdigital.turnapp.ui.component.BottomNavigationBar
import com.serdigital.turnapp.ui.component.HeroSection
import com.serdigital.turnapp.ui.component.QuickStats
import com.serdigital.turnapp.ui.component.ServicesPreview
import com.serdigital.turnapp.ui.navigation.Routes
import com.serdigital.turnapp.ui.theme.backgroundDark

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundDark) // fondo
            .padding(bottom = 6.dp) // espacio para la nav bar
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 65.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HeroSection()
            ActionButtons(
                onScheduleClick = { navController.navigate(Routes.AddAppointment.route) },
                onAddProfessionalClick = { navController.navigate(Routes.AddSpecialist.route) },
                onTodayAppointmentsClick = { navController.navigate(Routes.CalendarScreen.route) },
            )
            ServicesPreview()
            QuickStats()
        }
        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}