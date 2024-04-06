package dev.rivu.courses.medicinereminder.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import dev.rivu.courses.medicinereminder.db.MedicinesDB

actual object DBInitializer {
    actual val dbDriver: SqlDriver = NativeSqliteDriver(MedicinesDB.Schema, "medicines.db")

    actual fun init() {

    }
}