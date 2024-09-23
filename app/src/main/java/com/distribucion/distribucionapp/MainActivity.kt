package com.distribucion.distribucionapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.distribucion.distribucionapp.ui.theme.DistribucionAppTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase
        FirebaseApp.initializeApp(this)
        Log.d("MainActivity", "Firebase inicializado correctamente")

        // Inicializa el proceso de inicio de sesión con Google
        enableEdgeToEdge()
        setupGoogleSignIn() // Configurar Google Sign-In

        // Configura la interfaz de usuario con Jetpack Compose
        setContent {
            DistribucionAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SignInScreen(modifier = Modifier.padding(innerPadding)) // Pantalla con el botón
                }
            }
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))  // Reemplaza con el ID de cliente web de Firebase
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
        Log.d("MainActivity", "Google Sign-In configurado correctamente")
    }

    private fun signInWithGoogle() {
        Log.d("MainActivity", "signInWithGoogle: Iniciando el proceso de inicio de sesión")
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            Log.d("MainActivity", "onActivityResult: Resultado del intento de inicio de sesión recibido")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.result
            if (account != null) {
                Log.d("MainActivity", "onActivityResult: Cuenta obtenida, autenticando con Firebase")
                firebaseAuthWithGoogle(account.idToken!!)
            } else {
                Log.d("MainActivity", "onActivityResult: Fallo al obtener cuenta")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d("MainActivity", "firebaseAuthWithGoogle: Autenticando con Firebase")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("MainActivity", "firebaseAuthWithGoogle: Autenticación exitosa, navegando a la siguiente pantalla")
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.d("MainActivity", "firebaseAuthWithGoogle: Fallo en la autenticación con Firebase")
            }
        }
    }

    @Composable
    fun SignInScreen(modifier: Modifier = Modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    Log.d("MainActivity", "onClick: Intentando iniciar sesión con Google")
                    signInWithGoogle()
                },  // Llama a signInWithGoogle al hacer clic
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Iniciar sesión con Google")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SignInScreenPreview() {
        DistribucionAppTheme {
            SignInScreen()
        }
    }
}
