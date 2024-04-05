package dev.rivu.courses.medicinereminder.data.models

data class Medicine(
    val id: Int,
    val name: String,
    val foodOrder: FoodOrder,
    val times: List<MedicineTime>
)
