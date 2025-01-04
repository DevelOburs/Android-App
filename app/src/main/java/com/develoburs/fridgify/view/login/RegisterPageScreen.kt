package com.develoburs.fridgify.view.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.DarkBlueColor
import com.develoburs.fridgify.ui.theme.LightOrangeColor
import com.develoburs.fridgify.ui.theme.OrangeColor
import com.develoburs.fridgify.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    // State for error dialog
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Column Layout
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
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.labelLarge,
            color = OrangeColor,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // User Information Section
        Text(
            text = "User Information",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 16.dp),
            color = DarkBlueColor
        )

        // First Name
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(text = "First Name", style = MaterialTheme.typography.bodySmall) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OrangeColor,
                unfocusedBorderColor = LightOrangeColor
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Last Name
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(text = "Last Name", style = MaterialTheme.typography.bodySmall) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OrangeColor,
                unfocusedBorderColor = LightOrangeColor
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email", style = MaterialTheme.typography.bodySmall) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OrangeColor,
                unfocusedBorderColor = LightOrangeColor
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Phone Number
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(text = "Phone Number", style = MaterialTheme.typography.bodySmall) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OrangeColor,
                unfocusedBorderColor = LightOrangeColor
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Account Information Section
        Text(
            text = "Account Information",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 16.dp),
            color = DarkBlueColor
        )

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username", style = MaterialTheme.typography.bodySmall) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OrangeColor,
                unfocusedBorderColor = LightOrangeColor
            ),
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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OrangeColor,
                unfocusedBorderColor = LightOrangeColor
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Register Button
        Button(
            onClick = {
                // Validation: Check for empty fields
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty()) {
                    errorMessage = "All fields are required."
                    showDialog = true
                } else {
                    // Perform registration
                    viewModel.register(username, email, password, firstName, lastName) { response ->
                        if (response.userId != -1) {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Error: Registration failed. Please try again."
                            showDialog = true
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = OrangeColor,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Register", style = MaterialTheme.typography.titleMedium)
        }

        // Back to Login Button
        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text(text = "Already have an account? Login", style = MaterialTheme.typography.bodyMedium, color = OrangeColor)
        }
    }

    // Error Dialog
    // Error Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.titleMedium,
                    color = OrangeColor // Adjust the color as needed
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OrangeColor // Adjust the color as needed
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeColor, // Same color as register button
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.titleMedium // Same style as register button
                    )
                }
            },
            dismissButton = null // Optional: Add a dismiss button if needed
        )
    }
}
