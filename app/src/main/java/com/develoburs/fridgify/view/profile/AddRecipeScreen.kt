package com.develoburs.fridgify.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.develoburs.fridgify.R
import com.develoburs.fridgify.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    onSave: (Recipe) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<String>()) }
    var instructions by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }
    var author by remember { mutableStateOf("Unknown Author") } // Default value
    var likes by remember { mutableStateOf(0) } // Default value
    var comments by remember { mutableStateOf(listOf<String>()) } // Default value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Image Section: Placeholder image with clickable action
                imageUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Recipe Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(200.dp)
                            .padding(8.dp)
                            .clickable {
                                // Replace with actual image picker logic
                                imageUri = "newImageUri" // Simulating image selection
                            },
                        contentScale = ContentScale.Crop
                    )
                } ?: Image(
                    painter = rememberAsyncImagePainter(R.drawable.background_image), // Placeholder resource
                    contentDescription = "Placeholder Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp)
                        .padding(8.dp)
                        .clickable {
                            // Replace with actual image picker logic
                            imageUri = "newImageUri" // Simulating image selection
                        },
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Recipe Name Section
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Recipe Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Ingredients Section
                OutlinedTextField(
                    value = ingredients.joinToString(", "),
                    onValueChange = { ingredients = it.split(", ") },
                    label = { Text("Ingredients") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Instructions Section
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Instructions") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = {
                        // Create a new recipe object with the entered values
                        val newRecipe = Recipe(
                            id = System.currentTimeMillis().toString(), // Generate a unique ID
                            Name = name,
                            ingredients = ingredients,
                            instructions = instructions,
                            Images = listOfNotNull(imageUri),
                            Author = author,
                            Likes = likes,
                            Comments = comments.size,
                            comments = comments
                        )
                        onSave(newRecipe)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Recipe")
                }
            }
        }
    )
}
