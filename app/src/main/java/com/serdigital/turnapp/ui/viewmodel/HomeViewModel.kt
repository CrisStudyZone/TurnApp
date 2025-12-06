package com.serdigital.turnapp.ui.viewmodel

import com.google.firebase.auth.FirebaseAuth

class HomeViewModel {

    fun forceLogout() {
        FirebaseAuth.getInstance().signOut()
    }

}