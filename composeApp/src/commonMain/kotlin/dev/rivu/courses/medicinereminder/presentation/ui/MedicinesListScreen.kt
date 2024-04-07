package dev.rivu.courses.medicinereminder.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.rivu.courses.medicinereminder.data.MedicineRepository
import dev.rivu.courses.medicinereminder.presentation.MedicinesListScreenModel
import dev.rivu.courses.medicinereminder.presentation.MedicinesTakenScreenModel

class MedicinesListScreen(
    val repository: MedicineRepository
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel {
            MedicinesListScreenModel(
                repository = repository
            )
        }

        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.current

        SideEffect {
            screenModel.getData()
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                "Medicines Added",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            when {
                state.medicines.isNotEmpty() -> {
                    LazyColumn {
                        items(state.medicines) { medicine ->
                            Text(medicine.toString())
                        }
                    }
                }
            }

            TextButton(
                onClick = {
                    navigator?.pop()
                    navigator?.push(MedicinesTakenScreen(repository))
                }
            ) {
                Text("Go to medicines taken screen")
            }
        }



    }
}