package com.serdigital.turnapp.ui.screen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.serdigital.turnapp.R
import com.serdigital.turnapp.ui.navigation.Routes
import com.serdigital.turnapp.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Animación de escala
    val scale = remember { Animatable(0f) }

    // Efecto lanzado al entrar en pantalla
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(1500) // esperar un poco después de la animación

        // Verificamos si el usuario está logueado
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.Auth.route) {
                popUpTo(Routes.Splash.route) { inclusive = true }
            }
        }
    }

    // UI
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundDark
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo con animación de escala
            Image(
                painter = painterResource(id = R.drawable.logo), // o R.drawable.ic_launcher
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(160.dp)
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
            )

            // Loader circular debajo
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                color = primaryDark
            )
        }
    }
}
