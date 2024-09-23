package com.distribucion.distribucionapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.distribucion.distribucionapp.ui.theme.DistribucionAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase

class MenuActivity : ComponentActivity() {

    // Cliente de ubicación
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            DistribucionAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MenuScreen(modifier = Modifier.padding(innerPadding), obtenerUbicacionYGuardar = {
                        obtenerUbicacionYGuardar()
                    })
                }
            }
        }
    }

    // Función para obtener la ubicación y guardar en Firebase
    private fun obtenerUbicacionYGuardar() {
        // Verificar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permisos si no están concedidos
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
            return
        }

        // Obtener la última ubicación conocida
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("GPS", "Latitud: $latitude, Longitud: $longitude")

                // Llamar a la función para almacenar en Firebase
                guardarPosicionEnFirebase(latitude, longitude)
            } else {
                Log.d("GPS", "No se pudo obtener la ubicación.")
            }
        }
    }

    // Función para guardar la ubicación en Firebase
    private fun guardarPosicionEnFirebase(latitude: Double, longitude: Double) {
        // Instancia de Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("ubicaciones")

        // Crear un mapa con los datos de ubicación
        val ubicacionData = mapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )

        // Almacenar la ubicación en una nueva entrada en la base de datos
        reference.push().setValue(ubicacionData)
            .addOnSuccessListener {
                Log.d("Firebase", "Ubicación almacenada correctamente.")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error al almacenar la ubicación", e)
            }
    }
}

@Composable
fun MenuScreen(modifier: Modifier = Modifier, obtenerUbicacionYGuardar: () -> Unit) {
    var totalCompra by remember { mutableStateOf("") }
    var distancia by remember { mutableStateOf("") }
    var costoDespacho by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ingresa el total de la compra:")
        TextField(
            value = totalCompra,
            onValueChange = { totalCompra = it },
            label = { Text("Total de compra") },
            modifier = Modifier.padding(16.dp)
        )

        Text(text = "Ingresa la distancia en km:")
        TextField(
            value = distancia,
            onValueChange = { distancia = it },
            label = { Text("Distancia en km") },
            modifier = Modifier.padding(16.dp)
        )

        Button(onClick = {
            // Convierte los valores ingresados a números antes de calcular
            val total = totalCompra.toIntOrNull() ?: 0
            val km = distancia.toIntOrNull() ?: 0
            costoDespacho = calcularDespacho(total, km)
        }) {
            Text(text = "Calcular despacho")
        }

        Text(text = "El costo del despacho es: $costoDespacho pesos")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para obtener la ubicación y guardarla
        Button(onClick = {
            obtenerUbicacionYGuardar()
        }) {
            Text(text = "Obtener ubicación y guardar en Firebase")
        }
    }
}

fun calcularDespacho(totalCompra: Int, distancia: Int): Int {
    return when {
        totalCompra >= 50000 -> 0  // Despacho gratis
        totalCompra in 25000..49999 -> 150 * distancia  // $150 por km
        else -> 300 * distancia  // $300 por km
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    DistribucionAppTheme {
        MenuScreen(obtenerUbicacionYGuardar = {})
    }
}

