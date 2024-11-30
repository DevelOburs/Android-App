package com.develoburs.fridgify.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // App Settings Row
        ExpandableSettingRow(
            title = "App Settings",
            settings = listOf("Enable Notifications", "Dark Mode")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Settings Row
        ExpandableSettingRow(
            title = "User Settings",
            settings = listOf("Privacy Mode", "Location Access")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate to Liked Recipes with parameter
        CustomButton(
            text = "Liked Recipes",
            onClick = { navController.navigate("recipes/liked") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate to Saved Recipes with parameter
        CustomButton(
            text = "Saved Recipes",
            onClick = { navController.navigate("recipes/saved") }
        )

        // Spacer to push Logout button to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Button to redirect to the Login page
        CustomButton(
            text = "Logout",
            onClick = { navController.navigate("login") },
            buttonColor = Color(0xFFB00020)
        )
    }
}

@Composable
fun ExpandableSettingRow(title: String, settings: List<String>) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        // Row Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 32.dp)
            )

            Icon(
                imageVector = if (isExpanded) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }

        // Settings Toggles (shown only when expanded)
        if (isExpanded) {
            settings.forEach { setting ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = setting,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    var isToggled by remember { mutableStateOf(false) }
                    Switch(
                        checked = isToggled,
                        onCheckedChange = { isToggled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
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