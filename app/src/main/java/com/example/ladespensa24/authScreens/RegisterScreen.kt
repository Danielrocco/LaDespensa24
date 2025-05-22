package com.example.ladespensa24.authScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ladespensa24.R
import com.example.ladespensa24.viewmodel.MyViewModel

@Composable
fun RegisterScreen(navController: NavController, viewModel: MyViewModel) {

    BackHandler(enabled = true) {
        navController.navigate("homeScreen") {
            popUpTo("homeScreen") { inclusive = false }
            launchSingleTop = true
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        RegisterContent(Modifier.align(Alignment.Center), viewModel, navController)
    }
}

@Composable
private fun RegisterContent(modifier: Modifier, viewModel: MyViewModel, navController: NavController) {
    val name by viewModel.name.observeAsState("")
    val surname by viewModel.surname.observeAsState("")
    val email by viewModel.email.observeAsState("")
    val passwd by viewModel.passwd.observeAsState("")
    val address by viewModel.address.observeAsState("")
    val payCard by viewModel.payCard.observeAsState("")
    val isRegisterEnable by viewModel.isRegisterEnable.observeAsState(false)
    val isEmailExist by viewModel.isEmailExist.observeAsState(false)

    Column(modifier = modifier.padding(16.dp)) {

        TextFieldRegister(
            label = "Nombre",
            value = name,
            onValueChange = { viewModel.onRegisterDataChanged(it, surname, email, passwd, address, payCard) },
            isValid = viewModel.isNameValid.observeAsState(false).value
        )

        TextFieldRegister(
            label = "Apellido",
            value = surname,
            onValueChange = { viewModel.onRegisterDataChanged(name, it, email, passwd, address, payCard) },
            isValid = viewModel.isSurnameValid.observeAsState(false).value
        )

        TextFieldRegister(
            label = "Dirección",
            value = address,
            onValueChange = { viewModel.onRegisterDataChanged(name, surname, email, passwd, it, payCard) },
            isValid = viewModel.isAddressValid.observeAsState(false).value
        )

        TextFieldRegister(
            label = "Correo",
            value = email,
            onValueChange = { viewModel.onRegisterDataChanged(name, surname, it, passwd, address, payCard) },
            keyboardType = KeyboardType.Email,
            isValid = viewModel.isEmailValid.observeAsState(false).value
        )

        TextFieldRegister(
            label = "Tarjeta (16 digit)",
            value = payCard,
            onValueChange = { viewModel.onRegisterDataChanged(name, surname, email, passwd, address, it) },
            keyboardType = KeyboardType.Number,
            isValid = viewModel.isCardValid.observeAsState(false).value
        )

        TextFieldRegister(
            label = "Contraseña (Min 8 Caract)",
            value = passwd,
            onValueChange = { viewModel.onRegisterDataChanged(name, surname, email, it, address, payCard) },
            keyboardType = KeyboardType.Password,
            isPassword = true,
            isValid = viewModel.isPasswordValid.observeAsState(false).value
        )

        Spacer(modifier = Modifier.height(24.dp))

        RegisterButton(
            isRegisterEnable,
            isEmailExist
        ) {
            viewModel.register(navController = navController)
        }
    }
}


@Composable
private fun RegisterButton(registerEnable: Boolean, isEmailExists: Boolean, register: () -> Unit) {
    Text(
        text = if (isEmailExists) "Ese correo ya está asociado a una cuenta." else "",
        modifier = Modifier.fillMaxWidth(),
        color = Color.Red,
        fontFamily = FontFamily(Font(R.font.muli)),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        textAlign = TextAlign.Center
    )
    Button(
        onClick = { register() },
        enabled = registerEnable,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color(0xffb5e354),
            disabledContentColor = Color.White,
            disabledContainerColor = Color(0xFFC9C9C9)
        )
    ) {
        Text(
            text = "Crear Usuario",
            fontFamily = FontFamily(Font(R.font.muli)),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
fun TextFieldRegister(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isValid: Boolean = true
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = {
            if (isPassword) {
                val image = if (passwordVisible)
                    Icons.Default.VisibilityOff
                else
                    Icons.Default.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF383838),
            unfocusedTextColor = Color(0xFF383838),
            focusedContainerColor = Color(0xFFF3F3F3),
            unfocusedContainerColor = Color(0xFFF3F3F3),
            disabledContainerColor = Color(0xFFF3F3F3),
            focusedIndicatorColor = if (isValid) Color.Green else Color.Red,
            unfocusedIndicatorColor = if (isValid) Color.Green else Color.Red,
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
}


