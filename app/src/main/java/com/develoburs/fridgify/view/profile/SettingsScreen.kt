package com.develoburs.fridgify.view.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.develoburs.fridgify.ui.theme.BrightGreenColor
import com.develoburs.fridgify.ui.theme.CreamColor
import com.develoburs.fridgify.ui.theme.CreamColor2
import com.develoburs.fridgify.ui.theme.DarkBlueColor
import com.develoburs.fridgify.viewmodel.LoginViewModel

val LightRedColor = Color(0xFFFF4500)
val OrangeColor = Color(0xFFFF8C00)
val LightOrangeColor = Color(0xFFFFB45F)
val YellowColor = Color(0xFFFFC107)
val BurgundyColor = Color(0xFF8B0000)
val DarkOrangeColor = Color(0xFFD2691E)

@Composable
fun SettingsScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    var expandedUserSettings by remember { mutableStateOf(false) }
    var expandedRecipes by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamColor2)
            .padding(16.dp)
    ) {

        // User Settings Dropdown
        DropdownMenuSection(
            title = "User Settings",
            isExpanded = expandedUserSettings,
            onToggleExpand = { expandedUserSettings = !expandedUserSettings },
            items = listOf(
                DropdownMenuItem("Change Password") {
                    showChangePasswordDialog = true // Toggle dialog visibility
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recipes Dropdown
        DropdownMenuSection(
            title = "Recipes",
            isExpanded = expandedRecipes,
            onToggleExpand = { expandedRecipes = !expandedRecipes },
            items = listOf(
                DropdownMenuItem("Liked Recipes") { navController.navigate("recipes/liked") },
                DropdownMenuItem("Saved Recipes") { navController.navigate("recipes/saved") }
            )
        )

        // Spacer to push Logout button to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        CustomButton(
            text = "Logout",
            onClick = { navController.navigate("login") },
            buttonColor = LightRedColor
        )

        // Display the dialog if needed
        if (showChangePasswordDialog) {
            ShowChangePasswordDialog(
                onDismiss = { showChangePasswordDialog = false },
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun DropdownMenuSection(
    title: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    items: List<DropdownMenuItem>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightOrangeColor, shape = MaterialTheme.shapes.medium)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(color= CreamColor, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp)

            )

            Icon(
                imageVector = if (isExpanded) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }

        if (isExpanded) {
            items.forEach { item ->
                Column { // Use Column to arrange Rows vertically
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(OrangeColor, RoundedCornerShape(8.dp))
                            .clickable {
                                item.onClick()
                            }
                            .padding(vertical = 16.dp, horizontal = 16.dp) // Button-like padding
                    ) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodyMedium.copy(color = CreamColor),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    // Add spacing between rows
                    Spacer(modifier = Modifier.height(8.dp)) // Adjust the height as needed
                }
            }
        }
    }
}

data class DropdownMenuItem(val label: String, val onClick: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowChangePasswordDialog(onDismiss: () -> Unit, viewModel: LoginViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()
            },
            title = { Text(text = "Change Password", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text(text = "Username", style = MaterialTheme.typography.bodyLarge) },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = OrangeColor,
                            unfocusedBorderColor = LightOrangeColor,
                            focusedLabelColor = OrangeColor,
                            unfocusedLabelColor = DarkBlueColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text(text = "Old Password", style = MaterialTheme.typography.bodyLarge) },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                val visibilityIcon = if (passwordVisible) Icons.Default.Lock else Icons.Outlined.Lock
                                Icon(imageVector = visibilityIcon, contentDescription = "Toggle Password Visibility")
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = OrangeColor,
                            unfocusedBorderColor = LightOrangeColor,
                            focusedLabelColor = OrangeColor,
                            unfocusedLabelColor = DarkBlueColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text(text = "New Password", style = MaterialTheme.typography.bodyLarge) },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                val visibilityIcon = if (passwordVisible) Icons.Default.Lock else Icons.Outlined.Lock
                                Icon(imageVector = visibilityIcon, contentDescription = "Toggle Password Visibility")
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = OrangeColor,
                            unfocusedBorderColor = LightOrangeColor,
                            focusedLabelColor = OrangeColor,
                            unfocusedLabelColor = DarkBlueColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.changePassword(username, oldPassword, newPassword) { response ->
                            if (response.userId != -1) {
                                Log.d("ChangePasswordDialog", "Password changed successfully for user: ${response.username}")
                                showDialog = false
                                onDismiss()
                            } else {
                                Log.e("ChangePasswordDialog", "Failed to change password")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrightGreenColor
                    )
                ) {
                    Text(text = "OK", style = MaterialTheme.typography.bodyLarge)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BurgundyColor
                    )
                ) {
                    Text(text = "Cancel", style = MaterialTheme.typography.bodyLarge)
                }
            }
        )
    }
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    buttonColor: Color = MaterialTheme.colorScheme.surfaceVariant // Default color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp) // Flat style
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
