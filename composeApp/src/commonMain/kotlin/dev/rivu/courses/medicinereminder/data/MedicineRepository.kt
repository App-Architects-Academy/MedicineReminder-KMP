package dev.rivu.courses.medicinereminder.data

import arrow.core.Either
import dev.rivu.courses.medicinereminder.data.models.Medicine
import dev.rivu.courses.medicinereminder.data.models.MedicineTaken
import dev.rivu.courses.medicinereminder.data.models.MedicineTime

class MedicineRepository(
    val localDS: MedicineDS
) {
    suspend fun getAllMedicines(): Either<Throwable, List<Medicine>> {
        return localDS.getAllMedicines()
    }

    suspend fun getMedicineById(id: Long): Either<Throwable, Medicine> {
        return localDS.getMedicineById(id)
    }

    suspend fun getMedicineByName(name: String): Either<Throwable, Medicine> {
        return localDS.getMedicineByName(name)
    }

    suspend fun addMedicine(medicine: Medicine): Either<Throwable, Unit> {
        return localDS.addMedicine(medicine)
    }

    suspend fun getMedicinesForToday(): Either<Throwable, List<MedicineTaken>> {
        return localDS.getMedicinesForToday()
    }

    suspend fun markMedicineAsTaken(medicine: Medicine, medicineTime: MedicineTime): Either<Throwable, Unit> {
        return localDS.markMedicineAsTaken(medicine, medicineTime)
    }

}