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
//import androidx.compose.ui.graphics.Color
import com.develoburs.fridgify.ui.theme.DarkBlueColor

//val PurpleColor = Color(0xFF800080)



@Composable
fun FoodCard(
    food: Food,
    onClick: () -> Unit = {}
) {
    val screen_width_android = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screen_width_android / 4

    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(bottom = 8.dp)
            .size(cardSize)
            .border(
                width = 2.dp,
                color = BlueColor,
                shape = RoundedCornerShape(8.dp),
            )
            ,
        colors = CardDefaults.cardColors(containerColor = DarkBlueColor),
        shape = RoundedCornerShape(8.dp)
    ){
        Column(

            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(

                modifier = Modifier
                    .size(cardSize * 0.75f)
                    .border(2.dp, BlueColor,shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(food.image)
            }


            Text(
                text = food.name,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
    }
}

@Composable
fun ClickFoodCard(
    food: Food,
    onClick: () -> Unit = {}
) {
    val screen_width_android = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screen_width_android / 4

    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(bottom = 8.dp)
            .size(cardSize)
            .border(
                width = 2.dp,
                color = BlueColor,
                shape = RoundedCornerShape(8.dp),
            )
        ,
        colors = CardDefaults.cardColors(containerColor = DarkBlueColor),
        shape = RoundedCornerShape(8.dp)
    ){
        Column(

            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(

                modifier = Modifier
                    .size(cardSize * 0.75f)
                    .border(2.dp, BlueColor,shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(food.image)
            }


            Text(
                text = food.name,
                modifier = Modifier.padding(top = 1.dp)
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
                color = DarkBlueColor,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkBlueColor),
        shape = RoundedCornerShape(8.dp)
    )  {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "+", fontSize = 40.sp
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
                color = DarkBlueColor,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkBlueColor),
        shape = RoundedCornerShape(8.dp)
    )  {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "-", fontSize = 40.sp
            )
        }
    }
}