package dev.rivu.courses.medicinereminder.data.models

import kotlinx.datetime.Instant

data class MedicineTaken(
    val medicine: Medicine,
    val medicineTime: MedicineTime,
    val isTaken: Boolean,
    val timeStamp: Instant?,
)