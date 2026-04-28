package me.rafa.arias.parksmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import me.rafa.arias.parksmart.navigation.AppNavHost
import me.rafa.arias.parksmart.ui.ParkSmartTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ParkSmartTheme {
                AppNavHost()
            }
        }
    }
}