package com.example.ladespensa24.authScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.example.ladespensa24.NormalImage
import com.example.ladespensa24.R
import com.example.ladespensa24.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController, viewModel: MyViewModel) {
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
        LoginContent(Modifier.align(Alignment.Center), viewModel, navController)
    }
}

@Composable
private fun LoginContent(modifier: Modifier, viewModel: MyViewModel, navController: NavController) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val passwd: String by viewModel.passwd.observeAsState(initial = "")
    val isAlertEnable: Boolean by viewModel.isAlertVisible.observeAsState(initial = false)

    Column(modifier = modifier) {
        NormalImage(
            Modifier
                .align(Alignment.CenterHorizontally)
                .size(175.dp),
            R.drawable.logo,
            null
        )
        Spacer(modifier = Modifier.size(100.dp))
        Email(email) { viewModel.onLoginChanged(it, passwd) }
        Spacer(modifier = Modifier.size(24.dp))
        Passwd(passwd) { viewModel.onLoginChanged(email, it) }
        Spacer(modifier = Modifier.size(180.dp))
        LoginButton(isAlertEnable, viewModel, navController)
        Spacer(modifier = Modifier.size(24.dp))
        GoRegisterButton(navController, viewModel)
    }
}

@Composable
fun GoRegisterButton(navController: NavController, viewModel: MyViewModel) {
    Button(
        onClick = {
            viewModel.clearFormData()
            navController.navigate("registerScreen")
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color(0xFF00BCD4),
            disabledContentColor = Color.White,
            disabledContainerColor = Color(0xFFC9C9C9)
        )
    ) {
        Text(
            text = "Registrate",
            fontFamily = FontFamily(Font(R.font.muli)),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun LoginButton(
    isAlertEnable: Boolean,
    viewModel: MyViewModel,
    navController: NavController,
) {
    Text(
        text = "No se encontro ese correo o la contraseña es incorrecta",
        modifier = Modifier.fillMaxWidth(),
        color = if (isAlertEnable) Color.Red
        else Color.Transparent,
        fontFamily = FontFamily(Font(R.font.muli)),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.size(12.dp))
    Button(
        onClick = {
            viewModel.login(navController)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffb5e354))
    ) {
        Text(
            text = "Iniciar sesión",
            fontFamily = FontFamily(Font(R.font.muli)),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 18.sp
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
            colorFilter = ColorFilter.tint(Color.Black)
        )
        Spacer(modifier = Modifier.size(20.dp))
        TextField(
            value = email,
            onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFF383838),
                unfocusedTextColor = Color(0xFF383838),
                focusedContainerColor = Color(0xFFF3F3F3),
                unfocusedContainerColor = Color(0xFFF3F3F3),
                disabledContainerColor = Color(0xFFF3F3F3),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            placeholder = {
                Text(
                    text = "Correo",
                    fontFamily = FontFamily(Font(R.font.muli)),
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
            colorFilter = ColorFilter.tint(Color.Black)
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
                focusedContainerColor = Color(0xFFF3F3F3),
                unfocusedContainerColor = Color(0xFFF3F3F3),
                disabledContainerColor = Color(0xFFF3F3F3),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            placeholder = {
                Text(
                    text = "Contraseña",
                    fontFamily = FontFamily(Font(R.font.muli)),
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
