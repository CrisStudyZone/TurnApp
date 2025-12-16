package com.serdigital.turnapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.serdigital.turnapp.R
import com.serdigital.turnapp.ui.component.*
import com.serdigital.turnapp.ui.theme.*
import com.serdigital.turnapp.ui.viewmodel.RegisterViewModel


@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    // Estados de los inputs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    val state by registerViewModel.registerState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state){
        when(state){
            is RegisterViewModel.RegisterState.Success -> {
                Toast.makeText(
                    context,
                    (state as RegisterViewModel.RegisterState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
            is RegisterViewModel.RegisterState.Error -> {
                Toast.makeText(
                    context,
                    (state as RegisterViewModel.RegisterState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundDark
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = CardDefaults.cardColors(
                    containerColor = surfaceContainerDark,
                    contentColor = onSurfaceDark
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Título
                    Text(
                        text = stringResource(id = R.string.register_title),
                        fontSize = 20.sp,
                        color = primaryDark
                    )

                    // Email
                    EmailTextField(
                        value = email,
                        onValueChange = { email = it }
                    )

                    // Password
                    PasswordTextField(
                        value = password,
                        onValueChange = { password = it },
                        passwordVisible = passwordVisible,
                        onToggleVisibility = { passwordVisible = !passwordVisible }
                    )

                    // Botón Registrar
                    PrimaryButton(
                        text = stringResource(id = R.string.register_button),
                        onClick = { registerViewModel.registerWithEmail(email, password) }
                    )

                    // Volver al login
                    TextButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text(
                            text = stringResource(id = R.string.back_to_login),
                            color = primaryDark
                        )
                    }

                    // Estado (mostrar mensajes)
                    when (state) {
                        is RegisterViewModel.RegisterState.Success -> {
                            Text(
                                text = (state as RegisterViewModel.RegisterState.Success).message,
                                color = secondaryContainerDark
                            )
                        }
                        is RegisterViewModel.RegisterState.Error -> {
                            Text(
                                text = (state as RegisterViewModel.RegisterState.Error).message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
