package com.serdigital.turnapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.serdigital.turnapp.R
import com.serdigital.turnapp.ui.component.AppLogo
import com.serdigital.turnapp.ui.component.EmailTextField
import com.serdigital.turnapp.ui.component.PasswordTextField
import com.serdigital.turnapp.ui.component.PrimaryButton
import com.serdigital.turnapp.ui.component.SecondaryButton
import com.serdigital.turnapp.ui.navigation.Routes
import com.serdigital.turnapp.ui.theme.backgroundDark
import com.serdigital.turnapp.ui.theme.onSurfaceDark
import com.serdigital.turnapp.ui.theme.primaryDark
import com.serdigital.turnapp.ui.theme.surfaceContainerDark
import com.serdigital.turnapp.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    // Estados de los inputs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val credentialManager = CredentialManager.create(context)

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(context.getString(R.string.default_web_client_id))
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    val state by authViewModel.authState.collectAsState()

    val scope = rememberCoroutineScope()
    //Reaccion a los cambios de estado del auth
    LaunchedEffect(state) {
        if (state is AuthViewModel.AuthState.Success) {
            navController.navigate(Routes.Home.route)
        } else if (state is AuthViewModel.AuthState.Error) {
            Toast.makeText(
                context,
                (state as AuthViewModel.AuthState.Error).message,
                Toast.LENGTH_SHORT).show()
        }
    }

    // Contenedor principal
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundDark // tu color de fondo
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
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Logo de la app
                    AppLogo()

                    // Campo Email
                    EmailTextField(
                        value = email,
                        onValueChange = { email = it }
                    )

                    // Campo Password
                    PasswordTextField(
                        value = password,
                        onValueChange = { password = it },
                        passwordVisible = passwordVisible,
                        onToggleVisibility = { passwordVisible = !passwordVisible }
                    )

                    // Botón principal
                    PrimaryButton(
                        text = "Iniciar sesión",
                        onClick = { authViewModel.signInWithEmail(email, password) }
                    )

                    // Botón registro
                    SecondaryButton(
                        text = "Registrarse",
                        onClick = { navController.navigate(Routes.Register.route) }
                    )

                    // Botón Google
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                try {
                                    val result = credentialManager.getCredential(context, request)
                                    val credential = result.credential as GoogleIdTokenCredential
                                    val firebaseCredential = GoogleAuthProvider.getCredential(credential.idToken, null)

                                    authViewModel.signInWithCredential(firebaseCredential) // ⬅️ tu función del VM
                                    navController.navigate(Routes.Home.route)
                                } catch (e: Exception) {
                                    authViewModel.onLoginError(e.localizedMessage ?: "Error con Google Sign-In")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google icon",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Iniciar sesión con Google")
                    }

                    // Olvidaste tu contraseña
                    TextButton(onClick = { authViewModel.sendPasswordReset(email) }) {
                        Text("¿Olvidaste tu contraseña?", color = primaryDark)
                    }
                }
            }
        }
    }
}
