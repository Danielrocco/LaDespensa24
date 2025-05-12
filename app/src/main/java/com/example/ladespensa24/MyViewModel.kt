package com.example.ladespensa24

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import java.util.Date

class MyViewModel : ViewModel() {

    private val userDefault: User = User(
        "",
        "",
        "",
        "",
        "",
        "",
        null,
        null,
        null
    )



    private var userEnUso: User = userDefault

    private val _selectedIcon = mutableStateOf("mainScreen")
    val selectedIcon: State<String> = _selectedIcon

    fun selectIcon(screen: String) {
        _selectedIcon.value = screen
    }



    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _passwd = MutableLiveData<String>()
    val passwd: LiveData<String> = _passwd

    private val _isLogged = MutableLiveData<Boolean>(false)
    val isLogged: LiveData<Boolean> = _isLogged

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    private val _isAlertVisible = MutableLiveData<Boolean>()
    val isAlertVisible: LiveData<Boolean> = _isAlertVisible


    fun setUsuarioEnUso(user: User) {
        userEnUso = user
    }

    fun getUsuarioEnUso(): User {
        return userEnUso
    }

    private val users = listOf(
        User(
            "Daniel",
            "Rocco",
            "danielrocco04@gmail.com",
            "hola1234",
            "Calle Gimenez y costa 36",
            "1000200030004000",
            cartProducts = mutableListOf<InCartProduct>(),
            favouriteProducts = listOf<Product>(),
            purchases = mutableListOf<Purchase>()
        )
    )

    fun getUsuarios(): List<User> {
        return users
    }



    fun login(navController: NavController) {
        if (users.any { it.getEmail() == email.value }) {
            val usuarioEncontrado = users.find { it.getEmail() == email.value }
            if (usuarioEncontrado != null) {
                if (usuarioEncontrado.getPasswd() == passwd.value) {
                    _isLogged.value = true
                    setUsuarioEnUso(usuarioEncontrado)
                    navController.navigate("userScreen/${usuarioEncontrado.getEmail()}")
                } else _isAlertVisible.value = true
            }
        } else _isAlertVisible.value = true
    }

    fun logout(navController: NavController) {
        setUsuarioEnUso(userDefault)
        _isLogged.value = false
        navController.navigate("mainScreen")
    }

    fun onLoginChanged(email: String, passwd: String) {
        _email.value = email
        _passwd.value = passwd
        _isLoginEnable.value = isValidEmail(email) && isValidPassword(passwd)
    }

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(passwd: String): Boolean = passwd.length > 7
}
