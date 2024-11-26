package com.example.ladespensa24

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    private val _selectedIcon = mutableStateOf("mainScreen")
    val selectedIcon: State<String> = _selectedIcon

    fun selectIcon(screen: String) {
        _selectedIcon.value = screen
    }
}
