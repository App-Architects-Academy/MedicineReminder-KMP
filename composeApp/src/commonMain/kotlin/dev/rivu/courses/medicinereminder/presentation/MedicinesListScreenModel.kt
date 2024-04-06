package dev.rivu.courses.medicinereminder.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.rivu.courses.medicinereminder.data.MedicineRepository
import dev.rivu.courses.medicinereminder.presentation.states.MedicinesListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MedicinesListScreenModel(
    val repository: MedicineRepository
) : ScreenModel {
    private val _state: MutableStateFlow<MedicinesListState> = MutableStateFlow(MedicinesListState())
    val state: StateFlow<MedicinesListState> = _state.asStateFlow()

    fun getData() {
        _state.update {
            it.copy(isLoading = true)
        }
        screenModelScope.launch(Dispatchers.IO) {
            val medicinesTakenEither = repository.getAllMedicines()
            medicinesTakenEither.fold(
                ifLeft = { error ->
                    _state.update {
                        it.copy(errorDetails = error.message, isLoading = false)
                    }
                },
                ifRight = { medicines ->
                    _state.update {
                        it.copy(medicines = medicines, errorDetails = null, isLoading = false)
                    }
                }
            )
        }
    }
}