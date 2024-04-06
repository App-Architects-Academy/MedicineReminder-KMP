import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import dev.rivu.courses.medicinereminder.DIProvider
import dev.rivu.courses.medicinereminder.data.db.DBInitializer
import dev.rivu.courses.medicinereminder.presentation.ui.MedicinesTakenScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import medicinereminder.composeapp.generated.resources.Res
import medicinereminder.composeapp.generated.resources.compose_multiplatform

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App(diProvider: DIProvider) {

    val repository by rememberSaveable {
        mutableStateOf(
            diProvider.getMedicinesRepository(diProvider.getMedicinesDS(diProvider.getDB()))
        )
    }

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Navigator(MedicinesTakenScreen(repository))
    }
}