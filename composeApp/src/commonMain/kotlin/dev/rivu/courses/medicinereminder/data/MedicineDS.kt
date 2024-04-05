package dev.rivu.courses.medicinereminder.data

import arrow.core.Either
import dev.rivu.courses.medicinereminder.data.models.Medicine
import dev.rivu.courses.medicinereminder.data.models.MedicineTaken
import dev.rivu.courses.medicinereminder.data.models.MedicineTime

interface MedicineDS {
    fun getAllMedicines(): Either<Throwable, List<Medicine>>
    fun getMedicineById(id: Long): Either<Throwable, Medicine>
    fun getMedicineByName(name: String): Either<Throwable, Medicine>
    fun addMedicine(medicine: Medicine): Either<Throwable, Unit>

    fun getMedicinesForToday(): Either<Throwable, List<MedicineTaken>>

    fun markMedicineAsTaken(medicine: Medicine, medicineTime: MedicineTime): Either<Throwable, Unit>
}