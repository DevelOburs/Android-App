package com.develoburs.fridgify.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.develoburs.fridgify.model.Recipe
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.DarkBlueColor
import com.develoburs.fridgify.ui.theme.DarkerBlueColor
import com.develoburs.fridgify.ui.theme.PaleWhiteColor
import com.develoburs.fridgify.ui.theme.WhiteColor

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    isProfileScreen: Boolean = false
) {
    val cardSize = LocalConfiguration.current.screenWidthDp.dp
    val cornerRadius = 16.dp

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp)
            .size(cardSize)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkBlueColor),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(cornerRadius))) {
            Image(
                painter = rememberAsyncImagePainter(recipe.Image ?: R.drawable.menu_book),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(cornerRadius)),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardSize / 5)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(WhiteColor, PaleWhiteColor),
                            startY = Float.POSITIVE_INFINITY,
                            endY = 0f
                        )
                    )
                    .clip(RoundedCornerShape(cornerRadius))
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(cardSize / 5)
                    .align(Alignment.BottomCenter)
            )
            {
                Column(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = recipe.Name ?: "Unnamed Recipe", // Provide a default value if null
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = "${recipe.AuthorFirstName ?: ""} ${recipe.AuthorLastName ?: ""}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.comment_icon),
                        contentDescription = null
                    )

                    Text(
                        text = recipe.Comments.toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.favorite_icon),
                        contentDescription = null
                    )

                    Text(
                        text = recipe.Likes.toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            // Edit button in the top right corner
            if (isProfileScreen) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)  // Add some padding to position the icon
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit, // Make sure you have an edit icon resource
                        contentDescription = "Edit",
                        tint = WhiteColor
                    )
                }
            }
        }
    }
}