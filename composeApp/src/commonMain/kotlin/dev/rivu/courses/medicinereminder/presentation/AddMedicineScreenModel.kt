package dev.rivu.courses.medicinereminder.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import dev.rivu.courses.medicinereminder.data.MedicineRepository
import dev.rivu.courses.medicinereminder.presentation.states.AddMedicineState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddMedicineScreenModel(
    val repository: MedicineRepository
) : ScreenModel {

    private val _state: MutableStateFlow<AddMedicineState> = MutableStateFlow(AddMedicineState.EnterDetailsState)
    val state: StateFlow<AddMedicineState> = _state.asStateFlow()

}