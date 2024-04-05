package dev.rivu.courses.medicinereminder.data.models

import kotlinx.datetime.Instant

data class Medicine(
    val id: Long,
    val name: String,
    val foodOrder: FoodOrder,
    val times: List<MedicineTime>
)
data class MedicineTaken(
    val medicine: Medicine,
    val medicineTime: MedicineTime,
    val isTaken: Boolean,
    val timeStamp: Instant?,
)
