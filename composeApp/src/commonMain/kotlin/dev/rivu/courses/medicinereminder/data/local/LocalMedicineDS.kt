package dev.rivu.courses.medicinereminder.data.local

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import dev.rivu.courses.medicinereminder.data.MedicineDS
import dev.rivu.courses.medicinereminder.data.models.Medicine
import dev.rivu.courses.medicinereminder.data.models.MedicineTaken
import dev.rivu.courses.medicinereminder.data.models.MedicineTime
import dev.rivu.courses.medicinereminder.db.MedicinesDB
import dev.rivu.courses.medicinereminder.getTodayDateFormatted
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class LocalMedicineDS(
    private val medicinesDB: MedicinesDB
) : MedicineDS {
    override fun getAllMedicines(): Either<Throwable, List<Medicine>> = Either.catch {
        medicinesDB.transactionWithResult {
            medicinesDB.medicinesQueries.getAllMedicines().executeAsList()
                .map { medicine ->
                    val times = medicinesDB.medicinesQueries.getMedicineTimes(medicine.id).executeAsList()
                    Pair(medicine, times)
                }
                .map { medicinePair ->
                    Medicine(
                        id = medicinePair.first.id,
                        name = medicinePair.first.name,
                        foodOrder = medicinePair.first.foodOrder,
                        times = medicinePair.second.map {
                            it.medicineTime
                        }
                    )
                }
        }
    }

    override fun getMedicineById(id: Long): Either<Throwable, Medicine> = Either.catch {
        medicinesDB.medicinesQueries.getMedicineById(id).executeAsOne()
    }.map { medicine ->
        val times = medicinesDB.medicinesQueries.getMedicineTimes(medicine.id).executeAsList()
        Pair(medicine, times)
    }.map { medicinePair ->
        Medicine(
            id = medicinePair.first.id,
            name = medicinePair.first.name,
            foodOrder = medicinePair.first.foodOrder,
            times = medicinePair.second.map {
                it.medicineTime
            }
        )
    }

    override fun getMedicineByName(name: String): Either<Throwable, Medicine> = Either.catch {
        medicinesDB.medicinesQueries.getMedicineByName(name).executeAsOne()
    }.map { medicine ->
        val times = medicinesDB.medicinesQueries.getMedicineTimes(medicine.id).executeAsList()
        Pair(medicine, times)
    }.map { medicinePair ->
        Medicine(
            id = medicinePair.first.id,
            name = medicinePair.first.name,
            foodOrder = medicinePair.first.foodOrder,
            times = medicinePair.second.map {
                it.medicineTime
            }
        )
    }

    override fun addMedicine(medicine: Medicine) = Either.catch {
        medicinesDB.transaction {
            medicinesDB.medicinesQueries.addMedicineLocal(
                name = medicine.name,
                foodOrder = medicine.foodOrder,
                insertedOn = Clock.System.now().toEpochMilliseconds(),
                updatedOn = Clock.System.now().toEpochMilliseconds(),
            )
            val medicineId = medicinesDB.medicinesQueries.getMedicineByName(medicine.name).executeAsOne().id

            medicine.times.forEach { time ->
                medicinesDB.medicinesQueries.addMedicineTime(
                    medicineId = medicineId,
                    medicineTime = time
                )
            }
        }
    }

    override fun getMedicinesForToday(): Either<Throwable, List<MedicineTaken>> = Either.catch {
        medicinesDB.transactionWithResult {
            val medicines = medicinesDB.medicinesQueries.getAllMedicines().executeAsList()
                .map { medicineEntry ->
                    val times = medicinesDB.medicinesQueries.getMedicineTimes(medicineEntry.id).executeAsList()
                    val medicine = Medicine(
                        id = medicineEntry.id,
                        name = medicineEntry.name,
                        foodOrder = medicineEntry.foodOrder,
                        times = times.map {
                            it.medicineTime
                        }
                    )

                    Pair(medicine, times)
                }

            medicines.flatMap { medicineAndTimes ->
                medicineAndTimes.second.map { medicineTimes ->

                    val takenOn = medicinesDB.medicinesTakenQueries.isMedicineTakenForDate(
                        date = getTodayDateFormatted(),
                        medicineTimeId = medicineTimes.id
                    ).executeAsOneOrNull()

                    MedicineTaken(
                        medicine = medicineAndTimes.first,
                        timeStamp = takenOn?.let {
                            Instant.fromEpochMilliseconds(takenOn)
                        },
                        medicineTime = medicineTimes.medicineTime,
                        isTaken = takenOn != null
                    )
                }
            }

        }
    }

    override fun markMedicineAsTaken(medicine: Medicine, medicineTime: MedicineTime): Either<Throwable, Unit> = Either.catch {
        val timeId =
            medicinesDB.medicinesQueries.getMedicineTimeId(medicineId = medicine.id, medicineTime = medicineTime)
                .executeAsOneOrNull()

        either<Throwable, Unit> {
            ensure(timeId != null) {
                UnsupportedOperationException("Medicine is not supposed to be taken on the time mentioned")
            }

            medicinesDB.medicinesTakenQueries.markMedicineAsTakenLocal(
                medicineTimeId = timeId,
                date = getTodayDateFormatted(),
                time = Clock.System.now().toEpochMilliseconds()
            )
        }
    }
}