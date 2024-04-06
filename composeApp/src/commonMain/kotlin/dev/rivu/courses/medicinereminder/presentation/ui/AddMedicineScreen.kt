package dev.rivu.courses.medicinereminder.presentation.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import dev.rivu.courses.medicinereminder.data.MedicineRepository
import dev.rivu.courses.medicinereminder.presentation.MedicinesListScreenModel
import dev.rivu.courses.medicinereminder.presentation.MedicinesTakenScreenModel

class AddMedicineScreen(
    val repository: MedicineRepository
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel {
            MedicinesListScreenModel(
                repository = repository
            )
        }



    }
}