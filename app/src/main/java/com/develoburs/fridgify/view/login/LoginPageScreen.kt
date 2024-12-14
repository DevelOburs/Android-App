package com.develoburs.fridgify.view.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.develoburs.fridgify.view.bottombar.BottomBarScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.OrangeColor
import com.develoburs.fridgify.viewmodel.LoginViewModel

@Composable
fun LoginPageScreen( navController: NavController, viewModel: LoginViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginResult by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Title
        Text(
            text = stringResource(id = R.string.app_name),
            color = OrangeColor, // Customize the color
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp),
            style = MaterialTheme.typography.labelLarge
        )

        // Username Input
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username", style = MaterialTheme.typography.titleMedium) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password", style = MaterialTheme.typography.titleMedium) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    val visibilityIcon =
                        if (passwordVisible) Icons.Default.Lock else Icons.Outlined.Lock
                    Icon(imageVector = visibilityIcon, contentDescription = "Toggle Password Visibility")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* Handle done action */ }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            colors = ButtonColors(
                containerColor = OrangeColor, contentColor = OrangeColor,
                disabledContainerColor = OrangeColor,
                disabledContentColor = OrangeColor,
            ),
            onClick = {
                viewModel.login(username, email, password) { response ->
                    loginResult = if (response.error.isNullOrEmpty()) {
                        navController.navigate(BottomBarScreen.Home.route) {
                            // Clear the back stack so the user can't return to the login screen
                            popUpTo("login") { inclusive = true }
                        }
                        "Welcome, ${response.username} (ID: ${response.userId})"
                    } else {
                        "Error: ${response.error}"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Login", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Register Button (Navigate to Register Page)
        TextButton(
            onClick = {
                // Navigate to Register screen
                navController.navigate("register")
            }
        ) {
            Text(text = "Not registered? Create an account", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


