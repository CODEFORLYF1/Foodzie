package com.example.appmain.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appmain.model.FoodItem
import com.example.appmain.model.foodItems
import com.example.appmain.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, cartViewModel: CartViewModel, username: String) {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            label = { Text("Search for food items") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Hi, $username")
                    }
                },
                actions = {
                    IconButton(onClick = { isSearchActive = !isSearchActive }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Profile: $username") },
                            onClick = { expanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = { expanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                expanded = false
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            // Category Filter
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val categories = listOf("All", "Pizza", "Burger", "Pasta", "Vegan")
                items(categories) { category ->
                    Button(
                        onClick = { selectedCategory = category },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = if (selectedCategory == category) {
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        } else {
                            ButtonDefaults.buttonColors()
                        }
                    ) {
                        Text(text = category)
                    }
                }
            }

            // Filtered Food Items List
            LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                val filteredItems = foodItems.filter { foodItem ->
                    (selectedCategory == "All" || foodItem.name.contains(selectedCategory, ignoreCase = true)) &&
                            (foodItem.name.contains(searchText, ignoreCase = true) || searchText.isEmpty())
                }
                items(filteredItems) { foodItem ->
                    FoodItemCard(foodItem, cartViewModel, navController)
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(
    foodItem: FoodItem,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                // Navigate to the details screen with the food item's ID
                navController.navigate("food_details/${foodItem.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Display food image
            AsyncImage(
                model = foodItem.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Display food name and price
            Text(text = foodItem.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "₹${foodItem.price}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // Add to Cart Button
            Button(
                onClick = {
                    // Add the food item to the cart, with each tap adding one quantity
                    cartViewModel.addToCart(foodItem)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Cart")
            }
        }
    }
}
