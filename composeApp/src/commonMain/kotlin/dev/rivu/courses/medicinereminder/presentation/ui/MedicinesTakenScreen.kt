package dev.rivu.courses.medicinereminder.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

            }
        }

    }
}