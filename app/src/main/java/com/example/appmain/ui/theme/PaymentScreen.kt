package com.example.appmain.ui.screens

import android.app.Activity
import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.razorpay.Checkout

@Composable
fun PaymentScreen(navController: NavController, cartTotal: Double, latitude: Double, longitude: Double) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(cartTotal, latitude, longitude) {
        // Create order, handle Razorpay and update loading state based on payment result
        createOrder(context, cartTotal, latitude, longitude, navController) { success ->
            isLoading = false
            if (success) {
                // Navigate to home after payment success
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Payment failed, please try again.", Toast.LENGTH_LONG).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Processing payment, please wait...", style = MaterialTheme.typography.titleLarge)
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}

fun createOrder(context: Context, cartTotal: Double, latitude: Double, longitude: Double, navController: NavController, callback: (Boolean) -> Unit) {
    val client = OkHttpClient()
    val amountInPaise = (cartTotal * 100).toInt()

    val requestBody = FormBody.Builder()
        .add("amount", amountInPaise.toString())
        .add("currency", "INR")
        .add("payment_capture", "1")
        .build()

    val apiKey = "rzp_test_p5d5RqwOHeDsYY"
    val apiSecret = "H1uMjXvmaFkJECbB8M0tCeR5"
    val credentials = "$apiKey:$apiSecret"
    val authHeader = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

    val apiUrl = "https://api.razorpay.com/v1/orders"
    val request = Request.Builder()
        .url(apiUrl)
        .post(requestBody)
        .addHeader("Authorization", authHeader)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            callback(false)
            Toast.makeText(context, "Error creating order: ${e.message}", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseData = response.body?.string() ?: ""
                try {
                    val jsonResponse = JSONObject(responseData)
                    val orderId = jsonResponse.getString("id")
                    initiateRazorpayPayment(context, orderId, cartTotal, navController, callback)
                } catch (e: Exception) {
                    callback(false)
                    Toast.makeText(context, "Error parsing order response", Toast.LENGTH_LONG).show()
                }
            } else {
                callback(false)
                Toast.makeText(context, "Error creating order", Toast.LENGTH_LONG).show()
            }
        }
    })
}

fun initiateRazorpayPayment(
    context: Context, orderId: String, cartTotal: Double,
    navController: NavController, callback: (Boolean) -> Unit
) {
    val checkout = Checkout()
    checkout.setKeyID("rzp_test_p5d5RqwOHeDsYY")

    val options = JSONObject().apply {
        put("name", "Foodzie")
        put("description", "Order Payment")
        put("currency", "INR")
        put("amount", (cartTotal * 100).toInt()) // Amount in paise
        put("order_id", orderId)
        put("theme.color", "#3399cc")

        val prefill = JSONObject().apply {
            put("email", "user@example.com")
            put("contact", "8341785131")
        }
        put("prefill", prefill)
    }

    // Open Razorpay Checkout
    (context as Activity).runOnUiThread {
        try {
            checkout.open(context, options)
        } catch (e: Exception) {
            callback(false)
            Toast.makeText(context, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
