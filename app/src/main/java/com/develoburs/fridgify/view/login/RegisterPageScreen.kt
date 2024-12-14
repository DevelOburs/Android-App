package com.develoburs.fridgify.view.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.develoburs.fridgify.viewmodel.LoginViewModel

@Composable
fun RegisterPageScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var registerResult by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title Text
        Text(
            text = "Register",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF6200EA),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // User Information Section
        Text(
            text = "User Information",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // First Name
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(text = "First Name", style = MaterialTheme.typography.bodySmall) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Last Name
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(text = "Last Name", style = MaterialTheme.typography.bodySmall) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email", style = MaterialTheme.typography.bodySmall) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Phone Number
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(text = "Phone Number", style = MaterialTheme.typography.bodySmall) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Account Information Section
        Text(
            text = "Account Information",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username", style = MaterialTheme.typography.bodySmall) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password", style = MaterialTheme.typography.bodySmall) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.Lock else Icons.Outlined.Lock
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Register Button
        Button(
            onClick = {
                viewModel.register(username, email, password, firstName, lastName) { response ->
                    registerResult = if (response.userId != -1) {
                        navController.navigate("login") {
                            // Clear the back stack so the user can't return to the login screen
                            popUpTo("register") { inclusive = true }
                        }
                        "Welcome, ${response.username} (ID: ${response.userId})"
                    } else {
                        "Error: Registration failed. Please try again."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Register", style = MaterialTheme.typography.bodyMedium)
        }

        // Back to Login Button
        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text(text = "Already have an account? Login", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
