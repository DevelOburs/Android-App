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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.develoburs.fridgify.view.bottombar.BottomBarScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.DarkBlueColor
import com.develoburs.fridgify.ui.theme.LightOrangeColor
import com.develoburs.fridgify.ui.theme.OrangeColor
import com.develoburs.fridgify.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPageScreen( navController: NavController, viewModel: LoginViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginResult by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Loading state
    var isErrorDialogVisible by remember { mutableStateOf(false) } // State for error dialog visibility

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Title
        Icon(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "logo",
            tint = OrangeColor,
            modifier = Modifier.height(120.dp).padding(0.dp)
        )

        // Username Input
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username", style = MaterialTheme.typography.titleMedium) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = OrangeColor, // Color for focused state
            unfocusedBorderColor = LightOrangeColor, // Color for unfocused state
            focusedLabelColor = OrangeColor, // Label color when focused
            unfocusedLabelColor = DarkBlueColor, // Label color when not focused
            cursorColor = OrangeColor,
        ),

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
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = OrangeColor, // Color for focused state
            unfocusedBorderColor = LightOrangeColor, // Color for unfocused state
            focusedLabelColor = OrangeColor, // Label color when focused
            unfocusedLabelColor = DarkBlueColor, // Label color when not focused
            cursorColor = OrangeColor,
        ),
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Show Loading Indicator or Login Button
        if (isLoading) {
            CircularProgressIndicator(color = OrangeColor)
        } else {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeColor,
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White
                ),
                onClick = {
                    isLoading = true // Show loading indicator
                    viewModel.login(username, password) { response ->
                        isLoading = false // Hide loading indicator
                        if (response.error.isNullOrEmpty()) {
                            navController.navigate(BottomBarScreen.Home.route) {
                                popUpTo("login") { inclusive = true }
                            }
                            loginResult = "Welcome, ${response.username} (ID: ${response.userId})"
                        } else {
                            loginResult = response.error
                            isErrorDialogVisible = true // Show error dialog
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Login", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }
        if (isErrorDialogVisible) {
            AlertDialog(
                onDismissRequest = { isErrorDialogVisible = false },
                title = {
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                text = {
                    Text(
                        text = loginResult ?: "Unknown error occurred.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { isErrorDialogVisible = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("OK", style = MaterialTheme.typography.bodyMedium)
                    }
                },
                containerColor = LightOrangeColor, // Optional: background color for dialog
                tonalElevation = 4.dp
            )
        }


        Spacer(modifier = Modifier.height(8.dp))

        // Register Button (Navigate to Register Page)
        TextButton(
            onClick = {
                // Navigate to Register screen
                navController.navigate("register")
            }
        ) {
            Text(text = "Not registered? Create an account", style = MaterialTheme.typography.bodyMedium, color = OrangeColor)
        }
    }
}


