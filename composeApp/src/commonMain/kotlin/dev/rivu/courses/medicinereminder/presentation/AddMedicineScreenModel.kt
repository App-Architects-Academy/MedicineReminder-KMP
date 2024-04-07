package dev.rivu.courses.medicinereminder.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.rivu.courses.medicinereminder.data.MedicineRepository
import dev.rivu.courses.medicinereminder.data.models.FoodOrder
import dev.rivu.courses.medicinereminder.data.models.Medicine
import dev.rivu.courses.medicinereminder.data.models.MedicineTime
import dev.rivu.courses.medicinereminder.presentation.states.AddMedicineState
import dev.rivu.courses.medicinereminder.presentation.states.AddMedicineState.EnterDetailsState
import dev.rivu.courses.medicinereminder.presentation.states.AddMedicineState.SavingState
import dev.rivu.courses.medicinereminder.presentation.states.AddMedicineState.Success
import dev.rivu.courses.medicinereminder.presentation.states.FieldError
import dev.rivu.courses.medicinereminder.presentation.states.FieldError.Field
import dev.rivu.courses.medicinereminder.presentation.states.FieldError.Field.Name
import dev.rivu.courses.medicinereminder.presentation.states.FieldError.Field.Times
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddMedicineScreenModel(
    val repository: MedicineRepository
) : ScreenModel {

    private val _state: MutableStateFlow<AddMedicineState> = MutableStateFlow(AddMedicineState.EnterDetailsState())
    val state: StateFlow<AddMedicineState> = _state.asStateFlow()

    fun addMedicine(
        name: String,
        foodOrder: FoodOrder?,
        times: Set<MedicineTime>
    ) {
        if (validateMedicine(name, foodOrder, times)) {
            val medicine = Medicine(
                id = 0L,
                name = name,
                foodOrder = foodOrder!!,
                times = times.toList()
            )

            _state.update {
                SavingState(medicine)
            }

            screenModelScope.launch {
                val status = repository.addMedicine(medicine)

                status.fold(
                    ifLeft = { error ->
                        _state.update {
                            if (it is AddMedicineState.EnterDetailsState) {
                                it.copy(saveErrorDetails = error.message ?: "Some error happened")
                            } else {
                                AddMedicineState.EnterDetailsState(
                                    saveErrorDetails = error.message ?: "Some error happened"
                                )
                            }
                        }
                    },
                    ifRight = {
                        _state.update {
                            Success
                        }
                    }
                )
            }
        }
    }


    fun validateMedicine(
        name: String,
        foodOrder: FoodOrder?,
        times: Set<MedicineTime>
    ): Boolean {
        if (name.isBlank() || name.length <= 3) {
            _state.update {
                EnterDetailsState(
                    fieldError = FieldError(
                        error = "Name Cannot be blank",
                        field = Name
                    )
                )
            }
            return false
        }

        if (foodOrder == null) {
            _state.update {
                EnterDetailsState(
                    fieldError = FieldError(
                        error = "Please select whether medicine should be taken before or after food",
                        field = Field.FoodOrder
                    )
                )
            }
            return false
        }

        if (times.isEmpty()) {
            _state.update {
                EnterDetailsState(
                    fieldError = FieldError(
                        error = "Please select timings of the medicine",
                        field = Times
                    )
                )
            }
            return false
        }

        return true
    }
}