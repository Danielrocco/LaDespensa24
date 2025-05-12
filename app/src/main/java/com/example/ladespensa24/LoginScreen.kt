package com.example.ladespensa24

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
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
import com.example.ladespensa24.MyViewModel
import com.example.ladespensa24.NormalImage
import com.example.ladespensa24.R
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun LoginScreen(navController: NavController, viewModel: MyViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp)
    ) {
        LoginContent(Modifier.align(Alignment.Center), viewModel, navController)
    }
}

@Composable
private fun LoginContent(modifier: Modifier, viewModel: MyViewModel, navController: NavController) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val passwd: String by viewModel.passwd.observeAsState(initial = "")
    val isLoginEnable: Boolean by viewModel.isLoginEnable.observeAsState(initial = false)
    val isAlertEnable: Boolean by viewModel.isAlertVisible.observeAsState(initial = false)

    Column(modifier = modifier) {
        NormalImage(
            Modifier
                .align(Alignment.CenterHorizontally)
                .size(175.dp),
            R.drawable.carne,
            null
        )
        Spacer(modifier = Modifier.size(100.dp))
        Email(email) { viewModel.onLoginChanged(it, passwd) }
        Spacer(modifier = Modifier.size(24.dp))
        Passwd(passwd) { viewModel.onLoginChanged(email, it) }
        Spacer(modifier = Modifier.size(200.dp))
        LoginButton(isLoginEnable, isAlertEnable) { viewModel.login(navController = navController) }
    }
}

@Composable
private fun LoginButton(loginEnable: Boolean, isAlertEnable: Boolean, login: () -> Unit) {

    Text(
        text = "No se encontro ese correo o la contraseña es incorrecta",
        modifier = Modifier.fillMaxWidth(),
        color = if (isAlertEnable) Color.Red
        else Color.Transparent,
        fontFamily = FontFamily(Font(R.font.mulilight)),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        textAlign = TextAlign.Center
    )
    Button(
        onClick = { login() },
        enabled = loginEnable,
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        border = BorderStroke(3.dp, Color.White),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Black,
            disabledContentColor = Color(0xFF6D6D6D),
            disabledContainerColor = Color.Black
        )
    ) {
        Text(
            text = "Inicia sesión",
            fontFamily = FontFamily(Font(R.font.mulilight)),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun Email(email: String, onTextFieldChanged: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.persona),
            contentDescription = "Icono persona",
            modifier = Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.size(20.dp))
        TextField(value = email,
            onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFF383838),
                unfocusedTextColor = Color(0xFF383838),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            placeholder = {
                Text(
                    text = "Correo",
                    fontFamily = FontFamily(Font(R.font.mulilight)),
                    fontWeight = FontWeight.Bold
                )
            })
    }
}

@Composable
private fun Passwd(passwd: String, onTextFieldChanged: (String) -> Unit) {
    var passVisibility by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.candado),
            contentDescription = "Icono persona",
            modifier = Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.size(20.dp))
        TextField(
            value = passwd,
            onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFF383838),
                unfocusedTextColor = Color(0xFF383838),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            placeholder = {
                Text(
                    text = "Contraseña",
                    fontFamily = FontFamily(Font(R.font.mulilight)),
                    fontWeight = FontWeight.Bold
                )
            },
            trailingIcon = {
                val image = if (passVisibility) Icons.Default.VisibilityOff
                else Icons.Filled.Visibility
                IconButton(onClick = { passVisibility = !passVisibility }) {
                    Icon(imageVector = image, contentDescription = "Show Password")
                }
            },
            visualTransformation = if (passVisibility) VisualTransformation.None
            else PasswordVisualTransformation()
        )
    }
}
