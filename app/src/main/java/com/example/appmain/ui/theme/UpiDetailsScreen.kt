import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun UpiDetailsScreen(navController: NavController, amount: String) {
    var upiAddress by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Enter UPI Address", style = MaterialTheme.typography.titleLarge)

        // Input field for UPI address
        OutlinedTextField(
            value = upiAddress,
            onValueChange = { upiAddress = it },
            label = { Text("UPI Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (upiAddress.isNotEmpty()) {
                    initiateUpiPayment(context, upiAddress, amount)
                    navController.navigate("orderConfirmation")
                } else {
                    Toast.makeText(context, "Please enter a valid UPI address", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Proceed to Payment")
        }
    }
}


private fun initiateUpiPayment(context: Context, upiAddress: String, amount: String) {
    val uri = Uri.parse("upi://pay").buildUpon()
        .appendQueryParameter("pa", upiAddress) // Payee address
        .appendQueryParameter("pn", "Foodzie") // Payee name
        .appendQueryParameter("mc", "") // Merchant code (leave blank for individuals)
        .appendQueryParameter("tr", "T12345") // Transaction reference
        .appendQueryParameter("tn", "Foodzie Payment") // Transaction note
        .appendQueryParameter("am", amount) // Amount
        .appendQueryParameter("cu", "INR") // Currency
        .build()

    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = uri
        setPackage("com.google.android.apps.nbu.paisa.user") // Use a specific UPI app if desired
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No UPI app found", Toast.LENGTH_SHORT).show()
    }
}
