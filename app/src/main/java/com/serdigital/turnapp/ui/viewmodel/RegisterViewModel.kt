package com.serdigital.turnapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun registerWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _registerState.value = RegisterState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _registerState.value = RegisterState.Success("Usuario registrado correctamente")
                    } else {
                        _registerState.value =
                            RegisterState.Error(task.exception?.localizedMessage ?: "Error al registrarse")
                    }
                }
        }
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        data class Success(val message: String) : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}