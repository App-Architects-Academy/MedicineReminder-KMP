package dev.rivu.courses.medicinereminder.presentation.states

import dev.rivu.courses.medicinereminder.data.models.Medicine
import dev.rivu.courses.medicinereminder.data.models.MedicineTaken

data class MedicinesTakenState(
    val isLoading: Boolean = false,
    val medicinesTaken: List<MedicineTaken> = emptyList(),
    val medicinesPresent: Boolean = true,
    val errorDetails: String? = null
)
