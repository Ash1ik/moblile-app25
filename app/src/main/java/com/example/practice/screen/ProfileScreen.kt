package com.example.practice.screen

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.practice.R
import com.example.practice.TopBar
import com.example.practice.api.allRecipeData
import com.example.practice.elements.EditProfileDialog
import com.example.practice.elements.FixedButton
import com.example.practice.elements.UserProfile
import com.example.practice.pages.post.RecipePostsCard
import com.example.practice.viewmodel.AuthViewModel
import com.example.practice.viewmodel.VideoViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    context: Context,
    navController: NavController
) {
    val userName = viewModel.getUsername(context = context).orEmpty()
    val userPass = viewModel.getPassword(context = context).orEmpty()
    val userEmail = viewModel.getEmail(context = context).orEmpty()

    // Trigger login side-effect using LaunchedEffect
    LaunchedEffect(Unit) {
        viewModel.login(userName, userEmail, userPass)
    }

    // State to control the drawer's visibility
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val user = viewModel.profile

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onLogoutClick = {
                    viewModel.logout(context)
                    navController.navigate("home") // Navigate to the login screen
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Profile") },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
//                    colors = TopAppBarDefaults.smallTopAppBarColors(
//                        containerColor = Color.Blue,
//                        titleContentColor = Color.White,
//                        navigationIconContentColor = Color.White
//                    )
                )
            },
            content = { padding ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserNamePart(
                        user?.username ?: "Unknown User",
                        user?.email ?: "Unknown Email",
                        user?.profile_picture ?: "Unknown Picture",
                        viewModel
                    )
                    Spacer(modifier = Modifier.height(13.dp))
                    PostCollectsHistoryButton()
                    Spacer(modifier = Modifier.height(6.dp))
                    PostCollectsHistory(navController, userName)
                }
            }
        )
    }
}



@Composable
fun DrawerContent(
    onLogoutClick: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp // Get screen width in DP

    Column(
        modifier = Modifier
            .width((screenWidth * 0.5).dp) // Set drawer width to 50% of screen width
            .fillMaxHeight() // Take full screen height
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onLogoutClick) {
            Text("Logout", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Red))
        }
    }
}




@Composable
fun UserNamePart(
    userName: String,
    userEmail: String,
    userProfile: String,
    viewModel: AuthViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(color = Color(0xFFEFE7DC)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserProfile(110.dp, 110.dp, userProfile)
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
        ) {

            // user name
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = userName,
                        fontSize = screenRatioFontSize(20f),
                        fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
                        fontWeight = FontWeight(400),
                        color = Color.Black
                    )
                    Text(
                        text = userEmail,
                        fontSize = screenRatioFontSize(12f),
                        fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
                        fontWeight = FontWeight(400),
                        color = Color.Black
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.profile_settings),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                // Handle click event
                                showDialog = true
                            }
                        )
                )
            }

            EditProfileDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(24.dp))

        }

    }
}


@Composable
private fun FollowingFansCollects(following: Int, fans: Int, collects: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Following(following)
        Fans(fans)
        Collects(collects)
    }
}

@Composable
fun Following(following: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AllText(following.toString())
        AllText("Following")

    }
}

@Composable
fun Fans(fans: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AllText(fans.toString())
        AllText("Fans")

    }
}

@Composable
fun Collects(collects: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AllText(collects.toString())
        AllText("Collects")
    }
}

@Composable
fun AllText(text: String) {

    Text(
        text = text,
        fontSize = screenRatioFontSize(16f),
        fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
        fontWeight = FontWeight(400),
        color = Color.Black
    )
}


@Composable
fun FillTheProfile() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .background(color = Color(0xFFEFE7DC)),
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 8.dp),
            text = "Click here to fill in the profile",
            style = TextStyle(
                fontSize = screenRatioFontSize(16f),
                lineHeight = 20.sp,
                fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
                fontWeight = FontWeight(400),
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Preview
@Composable
fun PostCollectsHistoryButton() {

    var selectedButton by remember { mutableStateOf("Posts") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .background(color = Color(0xFFEFE7DC)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FixedButton(
            text = "Posts",
            isSelected = selectedButton == "Posts",
            onClick = { selectedButton = "Posts" },
            modifier = Modifier.wrapContentWidth()
        )
        FixedButton(
            text = "Collects",
            isSelected = selectedButton == "Collects",
            onClick = { selectedButton = "Collects" },
            modifier = Modifier.wrapContentWidth()
        )
        FixedButton(
            text = "History",
            isSelected = selectedButton == "History",
            onClick = { selectedButton = "History" },
            modifier = Modifier.wrapContentWidth()
        )
    }

}


@Composable
fun PostCollectsHistory(
    navController: NavController,
    userName: String,
    viewModel: VideoViewModel = viewModel()
) {
    val videoList = viewModel.videoList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchVideos()
    }

    LazyVerticalGrid(
        modifier = Modifier
            .padding(bottom = 6.dp),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(videoList.value.filter { it.uploaded_by == userName }) { video ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                RecipePostsCard(
                    navController = navController,
                    title = video.title,
                    description = video.description.take(100), // Truncate description to 100 characters
                    author = video.uploaded_by,
                    totalLikes = video.total_likes,
                    totalDislikes = video.total_dislikes,
                    videoUrl = video.video_file,
                    videoId = video.id,
                    thumbnailUrl = video.thamnail
                )
            }
        }
    }
}


@Composable
fun screenRatioFontSize(font: Float): TextUnit {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val ratio = font / screenHeight.value
    return (screenHeight * ratio).value.sp
}