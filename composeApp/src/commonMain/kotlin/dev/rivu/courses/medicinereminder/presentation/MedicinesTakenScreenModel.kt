package dev.rivu.courses.medicinereminder.presentation

import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import arrow.core.recover
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.rivu.courses.medicinereminder.data.MedicineRepository
import dev.rivu.courses.medicinereminder.data.models.Medicine
import dev.rivu.courses.medicinereminder.data.models.MedicineTime
import dev.rivu.courses.medicinereminder.presentation.states.MedicinesListState
import dev.rivu.courses.medicinereminder.presentation.states.MedicinesTakenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MedicinesTakenScreenModel(
    private val repository: MedicineRepository
) : ScreenModel {
    private val _state: MutableStateFlow<MedicinesTakenState> = MutableStateFlow(MedicinesTakenState())
    val state: StateFlow<MedicinesTakenState> = _state.asStateFlow()

    fun getData() {
        _state.update {
            it.copy(isLoading = true)
        }
        screenModelScope.launch(Dispatchers.IO) {
            val medicinesTaken = repository.getMedicinesForToday()

            medicinesTaken.fold(
                ifLeft = { error ->
                    val noMedicinesAdded = repository.getAllMedicines()
                        .map {
                            it.isEmpty()
                        }
                        .recover<Throwable, Throwable, Boolean> {
                            true
                        }
                        .bind()

                    _state.update {
                        it.copy(errorDetails = error.message, isLoading = false, medicinesPresent = !noMedicinesAdded)
                    }


                },
                ifRight = { medicinesTaken ->
                    _state.update {
                        it.copy(medicinesTaken = medicinesTaken, errorDetails = null, isLoading = false)
                    }
                }
            )
        }
    }

    fun markMedicineAsTaken(medicine: Medicine, medicineTime: MedicineTime) {
        _state.update {
            it.copy(isLoading = true)
        }
        screenModelScope.launch {
            val update = repository.markMedicineAsTaken(medicine, medicineTime)

            update.fold(
                ifLeft = { error ->
                    _state.update {
                        it.copy(errorDetails = error.stackTraceToString())
                    }
                },
                ifRight = {
                    getData()
                }
            )

        }
    }
}