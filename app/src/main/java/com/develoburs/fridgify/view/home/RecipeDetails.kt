package com.develoburs.fridgify.view.home

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModelFactory
import androidx.navigation.NavController
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import com.develoburs.fridgify.ui.theme.CreamColor2
import com.develoburs.fridgify.ui.theme.MintColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    recipeId: String, // Accept recipeId as a String
    onBack: () -> Unit,
    navController: NavController,
    repository: FridgifyRepositoryImpl,
    loggedInUsername: String = "Guest",
    onLoadingFinished: () -> Unit
) {
    // Initialize the ViewModel
    val viewModel: RecipeListViewModel = viewModel(
        factory = RecipeListViewModelFactory(
            navController = navController,
            repository = repository
        )
    )

    // Fetch the recipe details when the screen is loaded
    LaunchedEffect(recipeId) {
        viewModel.getRecipeById(recipeId)
        viewModel.fetchUserLikedRecipes(repository.getUserID().toString()) // Fetch liked recipes
    }

    // Collect the recipe details state
    val recipe by viewModel.recipeDetail.collectAsState()
    val userLikedRecipes by viewModel.userLikedRecipes.collectAsState()

    if (recipe != null) {
        onLoadingFinished() // Notify the parent that loading is complete
    }

    // State for like, save, and new comment
    val isLiked = userLikedRecipes.contains(recipeId)
    Log.d("RecipeDetailsScreen", "Recipe ID: $recipeId, isLiked: $isLiked")

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
                        text = recipe?.Name ?: stringResource(id = R.string.unknown_recipe),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Like Button
                        IconButton(
                            onClick = { viewModel.likeOrUnlikeRecipe(recipeId,
                                repository.getUserID().toString()
                            ) }
                        ) {
                            val isLiked = userLikedRecipes.contains(recipeId)
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = stringResource(
                                    id = if (isLiked) R.string.unlike else R.string.like
                                ),
                                tint = if (isLiked) Color.Red else BlackColor
                            )
                        }

                        // Like Count
                        Text(
                            text = recipe?.Likes?.toString() ?: "0", // Use Likes from recipe details
                            style = MaterialTheme.typography.bodyMedium,
                            color = BlackColor
                        )
                    }
                    // Save Button
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
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = CreamColor2)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // Recipe Details Content
                    item {
                        Column {
                            // Recipe Image
                            recipe?.Image?.let { imageUrl ->
                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = stringResource(id = R.string.recipe_image_description),
                                    modifier = Modifier
                                        .size(340.dp)
                                        .padding(6.dp)
                                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                                        .align(Alignment.CenterHorizontally), // Centralized alignment
                                    contentScale = ContentScale.Crop
                                )
                            } ?: Text(
                                text = stringResource(id = R.string.no_image_available),
                                color = Color.Black,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally), // Centralize the "no image" text
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Ingredients
                            Text(
                                text = stringResource(id = R.string.ingredients),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            recipe?.ingredients?.let { ingredients ->
                                ingredients.forEach { ingredient ->
                                    Text(
                                        text = "• $ingredient",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Black
                                    )
                                }
                            } ?: Text(
                                text = stringResource(id = R.string.no_ingredients_available),
                                color = Color.Black,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Instructions
                            Text(
                                text = stringResource(id = R.string.instructions),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = recipe?.instructions
                                    ?: stringResource(id = R.string.no_instructions_available),
                                color = Color.Black,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Comments Section Header
                    item {
                        Text(
                            text = stringResource(id = R.string.comments),
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = 18.sp
                            ),
                            color = Color.Black
                        )
                    }

                    // Comment Box
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp) // Fixed height for the comment section
                                .background(color = CreamColor2, shape = MaterialTheme.shapes.medium)
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
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    reverseLayout = true // Ensures most recent comments appear at the top
                                ) {
                                    items(comments) { (username, comment) ->
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .background(
                                                    Color.White,
                                                    shape = MaterialTheme.shapes.small
                                                )
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
                    }

                    // Add New Comment Section
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            OutlinedTextField(
                                value = newComment,
                                onValueChange = { newComment = it },
                                modifier = Modifier.weight(1f),
                                placeholder = {
                                    Text(
                                        text = stringResource(id = R.string.add_comment),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                if (newComment.isNotBlank()) {
                                    comments.add(
                                        0,
                                        Pair(loggedInUsername, newComment)
                                    ) // Add comment with logged-in username
                                    newComment = "" // Clear input
                                }
                            },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MintColor, // Background color
                                    contentColor = Color.Black // Text color
                                )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.send),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}