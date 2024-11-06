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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.ui.theme.DarkBlueColor
import com.develoburs.fridgify.ui.theme.DarkerBlueColor
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

@Composable
fun FoodCard(
    food: Food,
    onClick: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / 4

    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 8.dp)
            .size(cardSize)
            .border(
                width = 1.dp,
                color = DarkerBlueColor,
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkBlueColor),
        shape = RectangleShape
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(cardSize * 0.75f)
                    .border(1.dp, DarkerBlueColor),
                contentAlignment = Alignment.Center
            ) {
                Text(food.image)
            }


            Text(
                text = food.name,
                modifier = Modifier.padding(top = 0.dp)
            )
        }
    }
}