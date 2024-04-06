package dev.rivu.courses.medicinereminder.data

import arrow.core.Either
import dev.rivu.courses.medicinereminder.data.models.Medicine
import dev.rivu.courses.medicinereminder.data.models.MedicineTaken
import dev.rivu.courses.medicinereminder.data.models.MedicineTime

interface MedicineDS {
    suspend fun getAllMedicines(): Either<Throwable, List<Medicine>>
    suspend fun getMedicineById(id: Long): Either<Throwable, Medicine>
    suspend fun getMedicineByName(name: String): Either<Throwable, Medicine>
    suspend fun addMedicine(medicine: Medicine): Either<Throwable, Unit>

    suspend fun getMedicinesForToday(): Either<Throwable, List<MedicineTaken>>

    suspend fun markMedicineAsTaken(medicine: Medicine, medicineTime: MedicineTime): Either<Throwable, Unit>
}