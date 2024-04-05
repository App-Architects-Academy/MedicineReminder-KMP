package dev.rivu.courses.medicinereminder.data.db

import app.cash.sqldelight.ColumnAdapter
import dev.rivu.courses.medicinereminder.data.models.FoodOrder
import dev.rivu.courses.medicinereminder.data.models.MedicineTime

val foodOrderAdapter = object : ColumnAdapter<FoodOrder, String> {
    override fun decode(databaseValue: String) = FoodOrder.valueOf(databaseValue)
    override fun encode(value: FoodOrder) = value.name
}
val timesAdapter = object : ColumnAdapter<List<MedicineTime>, String> {
    override fun decode(databaseValue: String) =
        if (databaseValue.isBlank())
            emptyList()
        else {
            databaseValue.split(",")
                .filterNot {
                    it.isBlank()
                }
                .map {
                    MedicineTime.valueOf(databaseValue)
                }
        }

    override fun encode(value: List<MedicineTime>) = value.map {
        it.name
    }.joinToString(separator = ",")
}