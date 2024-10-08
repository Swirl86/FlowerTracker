package com.swirl.flowertracker.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.time.LocalDate

object DatabaseMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Modify the table schema
            db.execSQL("ALTER TABLE flower_table ADD COLUMN waterInDays INTEGER DEFAULT 0 NOT NULL")
            db.execSQL("ALTER TABLE flower_table ADD COLUMN fertilizeInDays INTEGER DEFAULT 0 NOT NULL")

        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Add new columns for LocalDate (stored as epoch days)
            db.execSQL("ALTER TABLE flower_table ADD COLUMN newWaterDate INTEGER")
            db.execSQL("ALTER TABLE flower_table ADD COLUMN newFertilizeDate INTEGER")

            // Select the existing rows and convert the waterInDays and fertilizeInDays to LocalDate
            val cursor = db.query("SELECT id, waterInDays, fertilizeInDays FROM flower_table")
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))

                // Check and convert waterInDays to LocalDate as epoch day if it exists
                val waterInDays = cursor.getInt(cursor.getColumnIndexOrThrow("waterInDays"))
                val newWaterDate = waterInDays.takeIf { it > 0 }?.let {
                        LocalDate.now().plusDays(it.toLong()).toEpochDay()
                    }

                // Check and convert fertilizeInDays to LocalDate as epoch day if it exists
                val fertilizeInDays = cursor.getInt(cursor.getColumnIndexOrThrow("fertilizeInDays"))
                val newFertilizeDate = fertilizeInDays.takeIf { it > 0 }?.let {
                    LocalDate.now().plusDays(it.toLong()).toEpochDay()
                }

                // Update the new columns
                db.execSQL(
                    "UPDATE flower_table SET newWaterDate = ?, newFertilizeDate = ? WHERE id = ?",
                    arrayOf(newWaterDate, newFertilizeDate, id)
                )
            }
            cursor.close()

            // Remove the old columns
            db.execSQL("ALTER TABLE flower_table DROP COLUMN waterInDays")
            db.execSQL("ALTER TABLE flower_table DROP COLUMN fertilizeInDays")

            // Rename new columns to replace the old ones
            db.execSQL("ALTER TABLE flower_table RENAME COLUMN newWaterDate TO waterInDays")
            db.execSQL("ALTER TABLE flower_table RENAME COLUMN newFertilizeDate TO fertilizeInDays")
        }
    }
}