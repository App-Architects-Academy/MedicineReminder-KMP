package dev.rivu.courses.medicinereminder

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.rivu.courses.medicinereminder.data.db.DBInitializer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(DIProvider.getOrCreateInstance(DBInitializer(this)))
        }
    }
}