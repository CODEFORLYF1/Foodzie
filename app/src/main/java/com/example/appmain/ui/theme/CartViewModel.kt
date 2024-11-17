package com.example.appmain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.appmain.model.CartItem
import com.example.appmain.model.FoodItem

class CartViewModel : ViewModel() {
    val cartItems: SnapshotStateList<CartItem> = mutableStateListOf()

    fun addToCart(foodItem: FoodItem) {
        val existingCartItem = cartItems.find { it.foodItem.id == foodItem.id }
        if (existingCartItem != null) {
            existingCartItem.quantity += 1
        } else {
            cartItems.add(CartItem(foodItem, 1))
        }
    }

    fun removeItem(cartItem: CartItem) {
        val existingCartItem = cartItems.find { it.foodItem.id == cartItem.foodItem.id }
        if (existingCartItem != null) {
            if (existingCartItem.quantity > 1) {
                existingCartItem.quantity -= 1
            } else {
                cartItems.remove(existingCartItem)
            }
        }
    }

    fun clearCart() {
        cartItems.clear()
    }

    val cartTotal: Double
        get() = cartItems.sumOf { it.foodItem.price * it.quantity }
}

data class CartItem(val foodItem: FoodItem, var quantity: Int)
