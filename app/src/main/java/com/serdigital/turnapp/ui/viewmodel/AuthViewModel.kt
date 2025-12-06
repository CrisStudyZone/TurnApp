package com.serdigital.turnapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val authUI: AuthUI
) : ViewModel(){

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun onLoginError(message: String) {
        _authState.value = AuthState.Error(message)
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Por favor completa todos los campos")
            return
        }
        val credential = EmailAuthProvider.getCredential(email, password)
        signInWithCredential(credential)
    }

    fun signInWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success(auth.currentUser?.displayName ?: "Usuario")

                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error desconocido")
                }
            }
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Por favor ingresa un correo válido")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success("Se envió un correo para restablecer la contraseña")
                } else {
                    _authState.value = AuthState.Error(
                        task.exception?.localizedMessage ?: "Error al enviar el correo de recuperación"
                    )
                }
            }
    }

    sealed class AuthState {
        object Idle : AuthState()
        data class Success(val userName: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
