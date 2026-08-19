package com.tinderview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tinderview.ui.TinderViewApp
import com.tinderview.ui.theme.TinderViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TinderViewTheme {
                TinderViewApp()
            }
        }
    }
}
