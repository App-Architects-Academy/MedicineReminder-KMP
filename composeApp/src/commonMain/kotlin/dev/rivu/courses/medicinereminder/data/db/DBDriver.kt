package dev.rivu.courses.medicinereminder.data.db

import app.cash.sqldelight.db.SqlDriver

expect class DBInitializer {
    val dbDriver: SqlDriver

    fun init()
}