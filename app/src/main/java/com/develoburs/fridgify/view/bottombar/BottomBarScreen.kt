package com.develoburs.fridgify.view.bottombar

import com.develoburs.fridgify.R

sealed class BottomBarScreen(
    val route: String,
    val title: Int,
    val icon: Int
) {
    object Fridge : BottomBarScreen(
        route = "fridge",
        title = R.string.fridge,
        icon = R.drawable.fridge_icon
    )

    object Home : BottomBarScreen(
        route = "home",
        title = R.string.home,
        icon = R.drawable.home_icon
    )


    object Profile : BottomBarScreen(
        route = "profile",
        title = R.string.profile,
        icon = R.drawable.profile_icon
    )
}