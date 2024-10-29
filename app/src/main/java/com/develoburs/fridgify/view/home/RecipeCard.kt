package com.develoburs.fridgify.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.develoburs.fridgify.model.Recipe
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.develoburs.fridgify.ui.theme.DarkBlueColor
import com.develoburs.fridgify.ui.theme.DarkerBlueColor

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp)
            .size(screenWidth)
            .border(
                width = 1.dp,
                color = DarkerBlueColor,
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkBlueColor),
        shape = RectangleShape
    ){
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberAsyncImagePainter(recipe.Images[0]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}