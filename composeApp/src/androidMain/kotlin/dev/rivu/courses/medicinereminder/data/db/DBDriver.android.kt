package dev.rivu.courses.medicinereminder.data.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.rivu.courses.medicinereminder.db.MedicinesDB

actual class DBInitializer(
    private val context: Context
) {
    actual val dbDriver: SqlDriver = AndroidSqliteDriver(MedicinesDB.Schema, context)

    actual fun init() {

    }
}