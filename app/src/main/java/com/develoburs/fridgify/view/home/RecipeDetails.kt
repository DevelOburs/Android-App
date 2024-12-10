package com.develoburs.fridgify.view.home

import com.develoburs.fridgify.model.Recipe
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material3.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.BlackColor
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    recipe: Recipe,
    onBack: () -> Unit,
    loggedInUsername: String = "Guest" // Pass the logged-in user's username here
) {
    var isLiked by remember { mutableStateOf<Boolean?>(null) }
    var isSaved by remember { mutableStateOf<Boolean?>(null) }
    var newComment by remember { mutableStateOf("") }
    val comments = remember {
        mutableStateListOf<Pair<String, String>>() // Pair of username and comment
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = recipe.Name ?: stringResource(id = R.string.unknown_recipe),
                        color = BlackColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = BlackColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {
                    IconButton(onClick = { isLiked = isLiked != true }) {
                        Icon(
                            imageVector = if (isLiked == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(id = if (isLiked == true) R.string.liked else R.string.like),
                            tint = if (isLiked == true) Color.Red else BlackColor
                        )
                    }
                    IconButton(onClick = { isSaved = isSaved != true }) {
                        Icon(
                            imageVector = if (isSaved == true) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = stringResource(id = if (isSaved == true) R.string.saved else R.string.save),
                            tint = if (isSaved == true) Color.Blue else BlackColor
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // Recipe Details Content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Recipe Image
                        recipe.Image?.firstOrNull()?.let { imageUrl ->
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = stringResource(id = R.string.recipe_image_description),
                                modifier = Modifier
                                    .size(300.dp)
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Text(
                            text = stringResource(id = R.string.no_image_available),
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Ingredients
                        Text(
                            text = stringResource(id = R.string.ingredients),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        recipe.ingredients?.let { ingredients ->
                            ingredients.forEach { ingredient ->
                                Text(text = "• $ingredient", style = MaterialTheme.typography.bodySmall)
                            }
                        } ?: Text(
                            text = stringResource(id = R.string.no_ingredients_available),
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Instructions
                        Text(
                            text = stringResource(id = R.string.instructions),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = recipe.instructions ?: stringResource(id = R.string.no_instructions_available),
                            color = if (recipe.instructions == null) Color.Gray else Color.Unspecified,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Static Comment Section
                    Text(
                        text = stringResource(id = R.string.comments),
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = 18.sp
                        )
                    )

                    // Comment Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Static height for the comment section
                            .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                            .padding(8.dp)
                    ) {
                        if (comments.isEmpty()) {
                            // Show "No comments yet" placeholder
                            Text(
                                text = stringResource(id = R.string.no_comments_available),
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.titleMedium
                            )
                        } else {
                            // Display comments in a scrollable list (most recent first)
                            LazyColumn(reverseLayout = true) {
                                items(comments) { (username, comment) ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .background(Color.White, shape = MaterialTheme.shapes.small)
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = username,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Bold,
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = comment,
                                            color = Color.DarkGray,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 16.sp,
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Add New Comment Section
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = newComment,
                            onValueChange = { newComment = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text(text = stringResource(id = R.string.add_comment), style = MaterialTheme.typography.bodyMedium) },
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            if (newComment.isNotBlank()) {
                                comments.add(0, Pair(loggedInUsername, newComment)) // Add comment with logged-in username
                                newComment = "" // Clear input
                            }
                        }) {
                            Text(text = stringResource(id = R.string.submit), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    )
}
