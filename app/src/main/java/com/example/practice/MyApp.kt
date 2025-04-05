package com.example.practice

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice.navitem.BottomNavigationItem
import com.example.practice.screen.LoginScreen
import com.example.practice.screen.ProfileScreen
import com.example.practice.screen.TutorialScreen

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(20.dp),
            painter = painterResource(id = R.drawable.drawer),
            contentDescription = "Drawer"
        )
        Text(text = "Explore")
        Text(text = "Followers") // New text item
        Icon(
            modifier = Modifier.padding(end = 10.dp).size(20.dp),
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Search",
            tint = Color.Unspecified
        )
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(modifier: Modifier) {
    val navItemList = listOf(
        BottomNavigationItem(title = "home", icon = R.drawable.home),
        BottomNavigationItem(title = "favorite", icon = R.drawable.favorite),
        BottomNavigationItem(title = "post", icon = R.drawable.post),
        BottomNavigationItem(title = "chat", icon = R.drawable.chat),
        BottomNavigationItem(title = "profile", icon = R.drawable.profile)
    )
    var selectedIndex by remember { mutableIntStateOf(3) }

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier
                    .height(40.dp),
                title = { TopBar() },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFFf9B77C))
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = modifier.height(50.dp)
            ) {
                navItemList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            if (selectedIndex != index) { // Prevent redundant navigation
                                selectedIndex = index
                                navController.navigate(item.title) // Navigate using route name
                            }
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = if (selectedIndex == index) Color(0xFFB1CB90)
                                        else Color(0xFFf9B77C), // Unselected color
                                        shape = CircleShape
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        if (selectedIndex != index) { // Ensure navigation triggers only once
                                            selectedIndex = index
                                            navController.navigate(item.title)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = "Icon",
                                    tint = Color.Unspecified,
                                    modifier = modifier.size(28.dp)
                                )
                            }
                        },
                        interactionSource = remember { MutableInteractionSource() }, // Disables ripple effect
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent // Removes selection effect
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "chat", // Set a valid initial route
        ) {
//            composable("favorite") { FavoriteScreen(innerPadding) }
//            composable("home") { HomeScreen(innerPadding) }
//            composable("post") {
//                PostScreen(innerPadding,navController)
//            }
            composable("chat") {
                TutorialScreen(modifier.padding(innerPadding))
            }
            composable("profile") {
                ProfileScreen(modifier.padding(innerPadding))
            }
        }
    }
}

