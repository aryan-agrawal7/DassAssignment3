package com.DASS_2024111023_2024117009.ims

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.DASS_2024111023_2024117009.ims.screens.LoginScreen
import com.DASS_2024111023_2024117009.ims.screens.RootNavigation
import com.DASS_2024111023_2024117009.ims.screens.StudentMainScreen
import com.DASS_2024111023_2024117009.ims.ui.theme.IMSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IMSTheme {
//                LoginScreen()
//                StudentMainScreen()
                RootNavigation()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IMSTheme {
        Greeting("Android")
    }
}