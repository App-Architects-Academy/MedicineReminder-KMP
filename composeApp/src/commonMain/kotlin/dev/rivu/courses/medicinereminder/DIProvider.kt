package dev.rivu.courses.medicinereminder

import dev.rivu.courses.medicinereminder.data.MedicineDS
import dev.rivu.courses.medicinereminder.data.MedicineRepository
import dev.rivu.courses.medicinereminder.data.db.Adapters
import dev.rivu.courses.medicinereminder.data.db.DBInitializer
import dev.rivu.courses.medicinereminder.data.local.LocalMedicineDS
import dev.rivu.courses.medicinereminder.db.MedicinesDB
import kotlinx.coroutines.sync.Mutex

class DIProvider private constructor(val dbInitializer: DBInitializer) {

    private var medicineRepository: MedicineRepository? = null
    private var medicineDS: MedicineDS? = null
    private var medicinesDB: MedicinesDB? = null

    fun getMedicinesRepository(medicineDS: MedicineDS): MedicineRepository {
        if (medicineRepository == null) {
            medicineRepository = MedicineRepository(medicineDS)
        }

        return medicineRepository!!
    }

    fun getMedicinesDS(db: MedicinesDB): MedicineDS {
        if (medicineDS == null) {
            medicineDS = LocalMedicineDS(db)
        }

        return medicineDS!!
    }

    fun getDB(): MedicinesDB {
        if (medicinesDB == null) {
            dbInitializer.init()
            medicinesDB = MedicinesDB(dbInitializer.dbDriver, Adapters.medicineTimesAdapter, Adapters.medicineAdapter)
        }

        return medicinesDB!!
    }

    companion object {
        private var instance: DIProvider? = null
        val mutex = Mutex()

        fun getOrCreateInstance(dbInitializer: DBInitializer): DIProvider {
            val currentInstance = instance
            return if (currentInstance == null) {
                instance = DIProvider(dbInitializer)
                instance!!
            } else {
                currentInstance
            }
        }
    }
}