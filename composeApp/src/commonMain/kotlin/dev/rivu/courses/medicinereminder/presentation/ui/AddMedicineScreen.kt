package dev.rivu.courses.medicinereminder.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.rivu.courses.medicinereminder.data.MedicineRepository
import dev.rivu.courses.medicinereminder.data.models.FoodOrder
import dev.rivu.courses.medicinereminder.data.models.MedicineTime
import dev.rivu.courses.medicinereminder.presentation.AddMedicineScreenModel
import dev.rivu.courses.medicinereminder.presentation.states.AddMedicineState
import dev.rivu.courses.medicinereminder.presentation.states.AddMedicineState.EnterDetailsState
import dev.rivu.courses.medicinereminder.presentation.states.AddMedicineState.SavingState
import dev.rivu.courses.medicinereminder.presentation.states.AddMedicineState.Success
import dev.rivu.courses.medicinereminder.presentation.states.FieldError

class AddMedicineScreen(
    val repository: MedicineRepository
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel {
            AddMedicineScreenModel(
                repository = repository
            )
        }

        val state by screenModel.state.collectAsState()

        var medicineName: String by remember {
            mutableStateOf("")
        }

        var medicineFoodOrder: FoodOrder? by remember {
            mutableStateOf(null)
        }

        var medicineTimes: Set<MedicineTime> by remember {
            mutableStateOf(emptySet())
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                "Add Medicines",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            when (state) {
                is EnterDetailsState -> {
                    val currentState = state as EnterDetailsState
                    AddMedicineContent(
                        currentState = currentState,
                        medicineName = medicineName,
                        foodOrder = medicineFoodOrder,
                        times = medicineTimes,
                        onNameChange = {
                            medicineName = it
                        },
                        onFoodOrderChange = {
                            medicineFoodOrder = it
                        },
                        onTimesAdded = {
                            medicineTimes = medicineTimes + it
                        },
                        onTimesRemoved = {
                            medicineTimes = medicineTimes - it
                        },
                        onSaveClicked = {
                            screenModel.addMedicine(name = medicineName, foodOrder = medicineFoodOrder, times = medicineTimes)
                        }
                    )

                    if (currentState.saveErrorDetails != null) {
                        Text(currentState.saveErrorDetails)
                    }
                }

                is SavingState -> {
                    Text("Saving Medicine ${(state as SavingState).medicine}")
                }

                Success -> {
                    val navigator = LocalNavigator.current
                    navigator?.pop()
                    navigator?.push(MedicinesListScreen(repository))
                }
            }
        }
    }
}

@Composable
fun AddMedicineContent(
    currentState: AddMedicineState.EnterDetailsState,
    medicineName: String,
    foodOrder: FoodOrder?,
    times: Set<MedicineTime>,
    onNameChange: (String) -> Unit,
    onFoodOrderChange: (FoodOrder) -> Unit,
    onTimesAdded: (MedicineTime) -> Unit,
    onTimesRemoved: (MedicineTime) -> Unit,
    onSaveClicked: ()->Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = medicineName,
            onValueChange = onNameChange,
            label = {
                Text("Medicine Name")
            },
            isError = currentState.fieldError?.field == FieldError.Field.Name
        )
        if (currentState.fieldError?.field == FieldError.Field.Name) {
            Text(currentState.fieldError.error)
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column(Modifier.selectableGroup()) {
            Text("Should the medicine be taken before/after food?")
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Before Food")
                RadioButton(
                    selected = foodOrder == FoodOrder.BeforeFood,
                    onClick = {
                        onFoodOrderChange(FoodOrder.BeforeFood)
                    },
                    modifier = Modifier.semantics { contentDescription = "Before Food" }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("After Food")
                RadioButton(
                    selected = foodOrder == FoodOrder.AfterFood,
                    onClick = { onFoodOrderChange(FoodOrder.AfterFood) },
                    modifier = Modifier.semantics { contentDescription = "After Food" }
                )
            }

            if (currentState.fieldError?.field == FieldError.Field.FoodOrder) {
                Text(currentState.fieldError.error)
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column(Modifier.selectableGroup()) {
            Text("When should the medicine be taken?")

            MedicineTime.entries.forEach { medicineTime ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isAdded = times.contains(medicineTime)
                    Text(medicineTime.name)
                    Checkbox(
                        checked = isAdded,
                        onCheckedChange = {
                            if (isAdded) {
                                onTimesRemoved(medicineTime)
                            } else {
                                onTimesAdded(medicineTime)
                            }
                        },
                        modifier = Modifier.semantics { contentDescription = "Before Food" }
                    )
                }
            }

            if (currentState.fieldError?.field == FieldError.Field.Times) {
                Text(currentState.fieldError.error)
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedButton(
            onClick = onSaveClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Medicine")
        }
    }
}