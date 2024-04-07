package dev.rivu.courses.medicinereminder.presentation.states

import dev.rivu.courses.medicinereminder.data.models.Medicine

sealed class AddMedicineState {
    data class EnterDetailsState(
        val fieldError: FieldError? = null,
        val saveErrorDetails: String? = null
    ) : AddMedicineState()

    data class SavingState(
        val medicine: Medicine
    ) : AddMedicineState()

    object Success : AddMedicineState()

}

data class FieldError(
    val error: String,
    val field: Field
) {
    enum class Field {
        Name, FoodOrder, Times
    }
}
