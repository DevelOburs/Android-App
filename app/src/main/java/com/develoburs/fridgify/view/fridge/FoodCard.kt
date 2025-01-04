package com.develoburs.fridgify.view.fridge


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.develoburs.fridgify.model.Food

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.develoburs.fridgify.ui.theme.LightOrangeColor
import com.develoburs.fridgify.ui.theme.OrangeColor




@Composable
fun FoodCard(
    food: Food,
    onClick: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / 4
    val LightYellowColor = Color(0xFFFFF9C4)

    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 4.dp)
            .size(cardSize,height = cardSize * 1.1f)
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = OrangeColor,
                shape = RoundedCornerShape(8.dp),
            ),
        colors = CardDefaults.cardColors(containerColor = LightYellowColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(cardSize * 0.65f)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(food.ImageUrl ?: com.develoburs.fridgify.R.drawable.menu_book),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = food.Name ?: "Unknown",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AddFoodCard(onClick: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / 4

    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 4.dp)
            .size(cardSize,height = cardSize * 1.1f)
            .border(
                width = 2.dp,
                color = OrangeColor,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = LightOrangeColor),
        shape = RoundedCornerShape(8.dp)
    )  {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(

                "+", style = MaterialTheme.typography.bodySmall.copy(fontSize = 40.sp),

            )
        }
    }
}
@Composable
fun DeleteFoodCard(onClick: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / 4

    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 4.dp)
            .size(cardSize,height = cardSize * 1.1f)
            .border(
                width = 2.dp,
                color = OrangeColor,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = LightOrangeColor),
        shape = RoundedCornerShape(8.dp)
    )  {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "-", style = MaterialTheme.typography.bodySmall.copy(fontSize = 40.sp)
            )
        }
    }
}