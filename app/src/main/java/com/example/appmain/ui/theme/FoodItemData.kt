package com.example.appmain.model

import com.example.appmain.R

// Data class for food items
data class FoodItem(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val image: Int, // Image URL or resource ID for the image
    val videoUrl: String // URL to the video (e.g., video file in res/raw or external URL)
)

// Sample list of food items with images and videos
val foodItems = listOf(
    FoodItem(
        "1",
        "Pizza Margherita",
        500.00,
        "A classic pizza with tomatoes, cheese, and basil.",
        R.drawable.pizza_image, // This is an Int resource ID
        "android.resource://com.example.appmain/raw/pizza_video" // Assuming video is stored in res/raw
    ),
    FoodItem(
        "2",
        "Cheeseburger",
        350.00,
        "A juicy beef burger with cheese, lettuce, and tomato.",
            R.drawable.burger_image, // This is an Int resource ID
        "android.resource://com.example.appmain/raw/burger_video"
    )
    // Add other items as needed...
,
    FoodItem(
        "3",
        "Pasta Carbonara",
        400.00,
        "A creamy pasta with bacon and parmesan.",
        R.drawable.pasta_image, // Replace with the actual drawable resource name
        "android.resource://com.example.appmain/raw/pasta_video" // Replace with actual video URL in res/raw
    ),
    FoodItem(
        "4",
        "Vegan Stir Fry",
        450.00,
        "A healthy stir fry with fresh vegetables and tofu.",
        R.drawable.vegan_image, // Replace with the actual drawable resource name
        "android.resource://com.example.appmain/raw/vegan_video" // Replace with actual video URL in res/raw
    ),
    FoodItem(
        "5",
        "Tacos",
        450.00,
        "Spicy tacos with beef, salsa, and guacamole.",
            R.drawable.taco_image, // Replace with the actual drawable resource name
        "android.resource://com.example.appmain/raw/taco_video" // Replace with actual video URL in res/raw
    )
)
