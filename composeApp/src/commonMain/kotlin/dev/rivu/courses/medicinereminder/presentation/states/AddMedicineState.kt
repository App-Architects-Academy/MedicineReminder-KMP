package dev.rivu.courses.medicinereminder.presentation.states

import dev.rivu.courses.medicinereminder.data.models.Medicine

sealed class AddMedicineState {
    object EnterDetailsState : AddMedicineState()

    data class SavingState(
        val medicine: Medicine
    ) : AddMedicineState()

    object Success : AddMedicineState()

    data class Error(
        val errorDetails: String
    ) : AddMedicineState()
}
