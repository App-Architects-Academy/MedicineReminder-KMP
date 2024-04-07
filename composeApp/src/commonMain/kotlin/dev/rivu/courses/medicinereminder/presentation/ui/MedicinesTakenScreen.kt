package dev.rivu.courses.medicinereminder.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
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
import dev.rivu.courses.medicinereminder.presentation.MedicinesTakenScreenModel

class MedicinesTakenScreen(
    val repository: MedicineRepository
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val screenModel = rememberScreenModel {
            MedicinesTakenScreenModel(
                repository = repository
            )
        }

        val state by screenModel.state.collectAsState()

        SideEffect {
            screenModel.getData()
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                "Medicines for Today",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            when {
                !state.medicinesPresent -> {
                    Column {
                        Text("No Medicines added yet")
                        Button(
                            onClick = {
                                navigator?.push(AddMedicineScreen(repository))
                            }
                        ) {
                            Text("Add Medicine")
                        }
                    }
                }

                state.medicinesTaken.isNotEmpty() -> {
                    LazyColumn {
                        items(state.medicinesTaken) { medicine ->
                            Row(modifier = Modifier.fillParentMaxWidth()) {
                                Text(medicine.toString())
                                Checkbox(
                                    checked = medicine.isTaken,
                                    onCheckedChange = {
                                        screenModel.markMedicineAsTaken(medicine.medicine, medicine.medicineTime)
                                    }
                                )
                            }

                        }
                    }
                }
            }

            TextButton(
                onClick = {
                    navigator?.pop()
                    navigator?.push(MedicinesListScreen(repository))
                }
            ) {
                Text("Go to medicines list screen")
            }
        }

    }
}