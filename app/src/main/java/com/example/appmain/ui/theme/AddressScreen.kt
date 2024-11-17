package com.example.appmain.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.appmain.viewmodel.CartViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun AddressScreen(navController: NavController, cartViewModel: CartViewModel) {
    val context = LocalContext.current
    var cartTotal by remember { mutableStateOf(0.0) }
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    // Calculate total cart price
    LaunchedEffect(cartViewModel.cartItems) {
        cartTotal = cartViewModel.cartItems.sumOf { it.foodItem.price * it.quantity }
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Register permission launcher for location
    val locationPermissionRequest =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                Toast.makeText(context, "Location permission granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation(fusedLocationClient) { location ->
                    currentLocation = location
                    Toast.makeText(context, "Location: $location", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    // Check for permissions and request if not granted
    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation(fusedLocationClient) { location ->
                    currentLocation = location
                    Toast.makeText(context, "Location: $location", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Select Your Location", style = MaterialTheme.typography.titleLarge)

        // Box to contain the map and make it smaller
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Adjust the height to make the map smaller
                .padding(16.dp)
        ) {
            // OSMDroid Map to show current location
            MapViewSection(currentLocation)
        }

        Spacer(modifier = Modifier.weight(1f)) // This will push the button to the bottom

        // Button to proceed to Payment Screen
        Button(
            onClick = {
                currentLocation?.let { location ->
                    val (latitude, longitude) = location
                    navController.navigate("payment/$cartTotal/$latitude/$longitude")
                } ?: Toast.makeText(context, "Please wait for the location to load", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Proceed to Payment")
        }
    }
}

fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Pair<Double, Double>?) -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            fusedLocationClient.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived(Pair(location.latitude, location.longitude))
                Toast.makeText(fusedLocationClient.applicationContext, "Location found: ${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
            } else {
                onLocationReceived(null)
                Toast.makeText(fusedLocationClient.applicationContext, "Location not found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(fusedLocationClient.applicationContext, "Error getting location: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(fusedLocationClient.applicationContext, "Location permission not granted", Toast.LENGTH_SHORT).show()
    }
}
@Composable
fun MapViewSection(currentLocation: Pair<Double, Double>?) {
    val context = LocalContext.current

    // Initialize OSMDroid Configuration
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

    val mapView = remember { MapView(context).apply {
        setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        setBuiltInZoomControls(true)
        setMultiTouchControls(true)
    } }

    LaunchedEffect(currentLocation) {
        if (currentLocation != null) {
            val controller: IMapController = mapView.controller
            controller.setZoom(15)
            controller.setCenter(GeoPoint(currentLocation.first, currentLocation.second))

            // Add location overlay
            val locationOverlay = MyLocationNewOverlay(mapView)
            locationOverlay.enableMyLocation()
            mapView.overlays.add(locationOverlay)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
    }
}
