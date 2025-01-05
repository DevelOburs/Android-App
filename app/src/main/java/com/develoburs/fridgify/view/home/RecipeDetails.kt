package com.develoburs.fridgify.view.home

import android.util.Log
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
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.BlackColor
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModelFactory
import androidx.navigation.NavController
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import com.develoburs.fridgify.ui.theme.CharcoalColor
import com.develoburs.fridgify.ui.theme.CreamColor
import com.develoburs.fridgify.ui.theme.CreamColor2
import com.develoburs.fridgify.ui.theme.MintColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    recipeId: String, // Accept recipeId as a String
    onBack: () -> Unit,
    navController: NavController,
    repository: FridgifyRepositoryImpl,
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

        // These functions fetch the initial liked/saved states without altering loading behavior
        viewModel.fetchUserLikedRecipes(repository.getUserID().toString())
        viewModel.fetchUserSavedRecipes(repository.getUserID().toString())

        // Fetch comments without affecting like/save loading state
        viewModel.fetchComments(recipeId)
    }


    // Collect the recipe details state
    val recipe by viewModel.recipeDetail.collectAsState()
    val userLikedRecipes by viewModel.userLikedRecipes.collectAsState()
    val userSavedRecipes by viewModel.userSavedRecipes.collectAsState()
    val saveCount by viewModel.saveCount.collectAsState()
    val comments by viewModel.comments.collectAsState() // Collect comments state
    var newComment by remember { mutableStateOf("") } // For new comment input
    val isLiking by viewModel.isLiking.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val isCommentsLoading by viewModel.isCommentsLoading.collectAsState()
    val isAddingComment by viewModel.isAddingComment.collectAsState()
    val isDeletingComment by viewModel.isDeletingComment.collectAsState()


    // State for like, save, and new comment
    val isLiked = userLikedRecipes.contains(recipeId)
    val isSaved = userSavedRecipes.contains(recipeId)
    Log.d("RecipeDetailsScreen", "Recipe ID: $recipeId, isLiked: $isLiked")
    var showDialog by remember { mutableStateOf(false) }
    var selectedCommentId by remember { mutableStateOf("") } // Track selected comment ID


    if (recipe != null) {
        onLoadingFinished() // Notify the parent that loading is complete
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.confirm_delete),
                    color = Color.Red,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.delete_confirmation_message),
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedCommentId.isNotEmpty() && !isDeletingComment) {
                            viewModel.deleteComment(
                                commentId = selectedCommentId,
                                userId = repository.getUserID().toString()
                            )
                        }
                        if (!isDeletingComment) {
                            showDialog = false // Close the dialog only when not deleting
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDeletingComment) Color.Gray else Color.Red,
                        contentColor = Color.White
                    ),
                    enabled = !isDeletingComment // Disable button during loading
                ) {
                    if (isDeletingComment) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.delete),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        if (!isDeletingComment) {
                            showDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .width(90.dp)
                        .height(40.dp),
                    enabled = !isDeletingComment // Disable cancel button during deletion
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            modifier = Modifier
                .width(250.dp)
                .padding(6.dp),
            shape = RoundedCornerShape(12.dp),
            containerColor = CreamColor2
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = recipe?.Name ?: stringResource(id = R.string.unknown_recipe),
                        color = CharcoalColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = CharcoalColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Like Button
                        IconButton(
                            onClick = {
                                if (!isLiking) {
                                    viewModel.likeOrUnlikeRecipe(recipeId, repository.getUserID().toString())
                                }
                            },
                            enabled = !isLiking // Disable button during loading
                        ) {
                            if (isLiking) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp), // Adjust size
                                    color = Color.Red,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = stringResource(
                                        id = if (isLiked) R.string.unlike else R.string.like
                                    ),
                                    tint = if (isLiked) Color.Red else BlackColor
                                )
                            }
                        }

                        // Like Count
                        Text(
                            text = recipe?.Likes?.toString() ?: "0", // Display the like count from the recipe object
                            style = MaterialTheme.typography.bodyMedium,
                            color = BlackColor
                        )

                    // Save Button with Loading State
                        IconButton(
                            onClick = {
                                if (!isSaving) {
                                    viewModel.saveOrUnsaveRecipe(
                                        recipeId,
                                        repository.getUserID().toString()
                                    )
                                }
                            },
                            enabled = !isSaving // Disable button during loading
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp), // Adjust size
                                    color = Color.Blue,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                    contentDescription = stringResource(
                                        id = if (isSaved) R.string.unsaved else R.string.save
                                    ),
                                    tint = if (isSaved) Color.Blue else BlackColor
                                )
                            }
                        }
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
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp,
                                                bottomStart = 16.dp,
                                                bottomEnd = 16.dp
                                            )
                                        )
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
                                        text = "• ${ingredient.Name}", // Access the 'name' property of each ingredient
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Black
                                    )
                                }
                            }
                                ?: Text(
                                    text = stringResource(id = R.string.no_ingredients_available),
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodySmall
                                )

                            Spacer(modifier = Modifier.height(16.dp))
// Recipe Calories
                            Text(
                                text = stringResource(id = R.string.calories),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${recipe?.calories ?: stringResource(id = R.string.not_available)} kcal",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(16.dp))

// Cooking Time
                            Text(
                                text = stringResource(id = R.string.cooking_time),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${recipe?.cookingTime ?: stringResource(id = R.string.not_available)} minutes",
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

                    // Comments Section
                    item {
                        Text(
                            text = stringResource(id = R.string.comments),
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 18.sp),
                            color = Color.Black
                        )
                    }

                    // Comment Box
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp) // Fixed height for the comment section
                                .background(
                                    color = CreamColor,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(8.dp)
                        ) {
                            if (isCommentsLoading) {
                                // Show loading spinner while comments are being fetched
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = MintColor
                                )
                            } else if (comments.isEmpty()) {
                                // Show "No comments yet" placeholder
                                Text(
                                    text = stringResource(id = R.string.no_comments_available),
                                    color = Color.Gray,
                                    modifier = Modifier.align(Alignment.Center),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            } else {
                                // Show comments
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    reverseLayout = true // Most recent comments at the top
                                ) {
                                    items(comments) { comment ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .background(
                                                    Color.White,
                                                    shape = MaterialTheme.shapes.small
                                                )
                                                .padding(8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Column for displaying comment details
                                            Column {
                                                // Display commenter's username
                                                Text(
                                                    text = comment.username
                                                        ?: stringResource(id = R.string.unknown_user),
                                                    color = Color.Black,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )

                                                Spacer(modifier = Modifier.height(4.dp))

                                                // Display the actual comment text
                                                Text(
                                                    text = comment.comment,
                                                    color = Color.DarkGray,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontSize = 16.sp
                                                    )
                                                )
                                            }

                                            if (comment.userId == repository.getUserID().toString()) {
                                                IconButton(
                                                    onClick = {
                                                        if (!isDeletingComment) {
                                                            selectedCommentId = comment.id?.toString() ?: "" // Set the selected comment ID
                                                            showDialog = true // Show delete confirmation dialog
                                                        }
                                                    },
                                                    enabled = !isDeletingComment || selectedCommentId != comment.id?.toString() // Disable only for active delete
                                                ) {
                                                    if (isDeletingComment && selectedCommentId == comment.id.toString()) {
                                                        CircularProgressIndicator(
                                                            modifier = Modifier.size(20.dp),
                                                            color = Color.Red,
                                                            strokeWidth = 2.dp
                                                        )
                                                    } else {
                                                        Icon(
                                                            imageVector = Icons.Default.Delete,
                                                            contentDescription = stringResource(id = R.string.delete_comment),
                                                            tint = Color.Red
                                                        )
                                                    }
                                                }
                                            }
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
                                singleLine = true,
                                enabled = !isAddingComment // Disable input while adding a comment
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (newComment.isNotBlank()) {
                                        viewModel.addComment(
                                            recipeId,
                                            repository.getUserID().toString(),
                                            newComment
                                        )
                                        newComment = "" // Clear the input
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MintColor,
                                    contentColor = Color.Black
                                ),
                                enabled = !isAddingComment // Disable button while loading
                            ) {
                                if (isAddingComment) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.Black,
                                        strokeWidth = 2.dp
                                    )
                                } else {
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
        }

    )
}