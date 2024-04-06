package dev.rivu.courses.medicinereminder.presentation.states

import dev.rivu.courses.medicinereminder.data.models.Medicine

data class MedicinesListState(
    val isLoading: Boolean = false,
    val medicines: List<Medicine> = emptyList(),
    val errorDetails: String? = null,
)
