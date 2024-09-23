package com.distribucion.distribucionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.distribucion.distribucionapp.ui.theme.DistribucionAppTheme

class NextActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DistribucionAppTheme {
                NextScreen()
            }
        }
    }
}

@Composable
fun NextScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "¡Bienvenido a la siguiente pantalla!")
    }
}

@Preview(showBackground = true)
@Composable
fun NextScreenPreview() {
    DistribucionAppTheme {
        NextScreen()
    }
}
