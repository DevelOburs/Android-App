package com.develoburs.fridgify.view.fridge


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

import com.develoburs.fridgify.ui.theme.BlueColor
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
//import androidx.compose.ui.graphics.Color
import com.develoburs.fridgify.ui.theme.DarkBlueColor
import coil.compose.rememberAsyncImagePainter
import com.develoburs.fridgify.ui.theme.YellowColor


//val PurpleColor = Color(0xFF800080)



@Composable
fun FoodCard(
    food: Food,
    onClick: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / 4

    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 4.dp)
            .size(cardSize)
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = BlueColor,
                shape = RoundedCornerShape(8.dp),
            ),
        colors = CardDefaults.cardColors(containerColor = YellowColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(cardSize * 0.75f)
                    .border(2.dp, BlueColor, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                val imageUrl = food.ImageUrl ?: "No Image"
                Text(text = imageUrl,  style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp))
                // Alternatively, use a placeholder image:
                // Image(
                //     painter = painterResource(id = R.drawable.placeholder_image),
                //     contentDescription = null,
                //     modifier = Modifier.fillMaxSize(),
                //     contentScale = ContentScale.Crop
                // )
            }

            Text(
                text = food.Name ?: "Unknown",
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AddFoodCard(onClick: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / 4

    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 8.dp)
            .size(cardSize)
            .border(
                width = 2.dp,
                color = BlueColor,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = BlueColor),
        shape = RoundedCornerShape(8.dp)
    )  {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "+", style = MaterialTheme.typography.bodySmall.copy(fontSize = 40.sp)
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
            .padding(horizontal = 4.dp)
            .padding(bottom = 8.dp)
            .size(cardSize)
            .border(
                width = 2.dp,
                color = BlueColor,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = BlueColor),
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