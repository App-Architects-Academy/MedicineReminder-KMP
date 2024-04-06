package dev.rivu.courses.medicinereminder.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.rivu.courses.medicinereminder.db.MedicinesDB

actual object DBInitializer {
    actual val dbDriver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:medicines.db")

    actual fun init() {
        MedicinesDB.Schema.create(dbDriver)
    }
}