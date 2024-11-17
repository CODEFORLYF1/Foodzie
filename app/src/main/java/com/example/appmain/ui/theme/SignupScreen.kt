package com.example.appmain.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.input.PasswordVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign Up") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    showError = false
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                isError = showError && username.isBlank()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = false
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = showError && password.isBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        navController.navigate("home/$username") {
                            popUpTo("signup") { inclusive = true }
                        }
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navController.navigate("login")
                }
            ) {
                Text("Already have an account? Login")
            }
        }
    }
}