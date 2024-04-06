package dev.rivu.courses.medicinereminder.data.models

data class Medicine(
    val id: Long,
    val name: String,
    val foodOrder: FoodOrder,
    val times: List<MedicineTime>
)
