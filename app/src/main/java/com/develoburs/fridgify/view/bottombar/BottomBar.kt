package com.develoburs.fridgify.view.bottombar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.develoburs.fridgify.ui.theme.BlackColor
import com.develoburs.fridgify.ui.theme.CreamColor
import com.develoburs.fridgify.ui.theme.DarkBlueColor
import com.develoburs.fridgify.ui.theme.DarkOrangeColor
import com.develoburs.fridgify.ui.theme.OrangeColor
import com.develoburs.fridgify.ui.theme.PaleBlackColor
import com.develoburs.fridgify.ui.theme.PaleWhiteColor
import com.develoburs.fridgify.ui.theme.WhiteColor

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Fridge,
        BottomBarScreen.Home,
        BottomBarScreen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box() {
        NavigationBar(
            containerColor = OrangeColor,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    NavigationBarItem(
        label = {
            Text(
                text = stringResource(id = screen.title),
                style = if (isSelected) {
                    MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                } else {
                    MaterialTheme.typography.labelSmall
                },
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 2.dp)
            )
        },
        selected = isSelected,
        onClick = {
            navController.navigate(screen.route)
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = WhiteColor,
            indicatorColor  = DarkOrangeColor,
            unselectedIconColor = PaleWhiteColor,
            selectedTextColor = BlackColor,
            unselectedTextColor = PaleBlackColor
        ),
        modifier = Modifier
            .padding(top = 2.dp)
            .height(50.dp)
    )
}