package com.example.appmain.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appmain.model.FoodItem
import com.example.appmain.model.foodItems
import com.example.appmain.viewmodel.CartViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailsScreen(navController: NavController, foodItemId: String?, cartViewModel: CartViewModel) {
    val context = LocalContext.current
    val foodItem = getFoodItemById(foodItemId)

    if (foodItem == null) {
        Toast.makeText(context, "Food item not found", Toast.LENGTH_SHORT).show()
        navController.popBackStack()
        return
    }

    // Create and set up the ExoPlayer
    val player = remember { ExoPlayer.Builder(context).build() }
    DisposableEffect(player) {
        val mediaItem = MediaItem.fromUri(Uri.parse(foodItem.videoUrl))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        Log.d("FoodDetailsScreen", "Player is set up and ready to play")
        onDispose {
            player.release()
            Log.d("FoodDetailsScreen", "Player released")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(foodItem.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Video player
            AndroidView(
                factory = {
                    StyledPlayerView(context).apply {
                        this.player = player // Set the player directly
                        useController = true
                        setShowBuffering(StyledPlayerView.SHOW_BUFFERING_ALWAYS)
                        Log.d("FoodDetailsScreen", "StyledPlayerView initialized")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Adjust the height as needed
            )

            // Display food image
            AsyncImage(
                model = foodItem.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp)
            )

            // Food name and description
            Text(
                text = foodItem.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = foodItem.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Price
            Text(
                text = "Price: ₹${foodItem.price}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add to cart button
            Button(
                onClick = { cartViewModel.addToCart(foodItem) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Add to Cart")
            }
        }
    }
}

fun getFoodItemById(foodItemId: String?): FoodItem? {
    return foodItems.find { it.id == foodItemId }
}
