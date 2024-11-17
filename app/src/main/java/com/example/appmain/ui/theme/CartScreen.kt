package com.example.appmain.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmain.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartViewModel: CartViewModel, navController: NavController) {
    val context = LocalContext.current
    val cartItems by remember { mutableStateOf(cartViewModel.cartItems) }
    var totalPrice by remember { mutableStateOf(cartViewModel.cartTotal) }

    LaunchedEffect(cartItems) {
        totalPrice = cartViewModel.cartTotal
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        cartViewModel.clearCart()
                        totalPrice = cartViewModel.cartTotal
                        Toast.makeText(context, "Cart cleared", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear Cart")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Your Cart", style = MaterialTheme.typography.titleLarge)

            if (cartItems.isEmpty()) {
                Toast.makeText(context, "Your cart is empty. Add some items!", Toast.LENGTH_SHORT).show()
            }

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.6f)
            ) {
                items(cartItems) { cartItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${cartItem.foodItem.name} x${cartItem.quantity}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "₹${cartItem.foodItem.price * cartItem.quantity}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(onClick = {
                            cartViewModel.removeItem(cartItem)
                            totalPrice = cartViewModel.cartTotal
                            if (cartViewModel.cartItems.isEmpty()) {
                                Toast.makeText(context, "Your cart is empty. Add some items!", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove Item")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Total: ₹$totalPrice", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (cartItems.isEmpty()) {
                        Toast.makeText(context, "Your cart is empty. Add some items!", Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate("address")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Proceed to Checkout")
            }
        }
    }
}
