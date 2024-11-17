package com.example.appmain

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appmain.ui.screens.*
import com.example.appmain.viewmodel.CartViewModel
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {

    private lateinit var navController: NavHostController
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartViewModel = CartViewModel()
        setContent {
            navController = rememberNavController()
            FoodDeliveryApp(navController, cartViewModel)
        }
    }

    override fun onPaymentSuccess(paymentId: String?) {
        Toast.makeText(this, "Payment Successful! ID: $paymentId", Toast.LENGTH_SHORT).show()
        cartViewModel.clearCart() // Clear the cart after payment success
        // Navigate to HomeScreen after successful payment
        navController.navigate("home/") {
            popUpTo("home") { inclusive = true }
        }
    }

    override fun onPaymentError(code: Int, description: String?) {
        Toast.makeText(this, "Payment Failed! Error: $description", Toast.LENGTH_SHORT).show()
        // Handle payment failure here if needed
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDeliveryApp(navController: NavHostController, cartViewModel: CartViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foodzie") },
                actions = {
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable("home/{username}", arguments = listOf(navArgument("username") { type = NavType.StringType })) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: "User"
                HomeScreen(navController, cartViewModel, username)
            }
            composable("cart") { CartScreen(cartViewModel, navController) }
            composable("address") { AddressScreen(navController, cartViewModel) }
            composable("payment/{cartTotal}/{latitude}/{longitude}") { backStackEntry ->
                val cartTotal = backStackEntry.arguments?.getString("cartTotal")?.toDouble() ?: 0.0
                val latitude = backStackEntry.arguments?.getString("latitude")?.toDouble() ?: 0.0
                val longitude = backStackEntry.arguments?.getString("longitude")?.toDouble() ?: 0.0
                PaymentScreen(navController, cartTotal, latitude, longitude)
            }
            composable("food_details/{foodItemId}") { backStackEntry ->
                val foodItemId = backStackEntry.arguments?.getString("foodItemId")
                FoodDetailsScreen(navController, foodItemId, cartViewModel)
            }
            composable("login") { LoginScreen(navController) }
            composable("signup") { SignupScreen(navController) }
        }
    }
}
