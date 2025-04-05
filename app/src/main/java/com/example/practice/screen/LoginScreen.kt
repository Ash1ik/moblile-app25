package com.example.practice.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.practice.MyApp
import com.example.practice.elements.CustomOutlinedTextField
import com.example.practice.elements.CustomizedPasswordField
import com.example.practice.viewmodel.AuthViewModel


@Composable
fun LoginScreen(modifier: Modifier,navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val user = viewModel.userProfile
    val error = viewModel.errorMessage

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user == null) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)

            CustomOutlinedTextField(
                value = username,
                onValueChange = { username = it },
                labelText ="Username"
            )
            CustomOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                labelText ="Email"
            )
            CustomizedPasswordField(
                value = password,
                onValueChange = { password = it },
                labelText = "Password"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Calling login function in ViewModel
                viewModel.login(username, email, password)
            }) {
                Text("Login")
            }

            // Show error message if any
            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red)
            }

        } else {
            // Display username and email if the user is logged in
//            MyApp()
//            Text("Welcome, ${user.username}!", style = MaterialTheme.typography.headlineSmall)
//            Text("Email: ${user.email}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
