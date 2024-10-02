package com.swirl.flowertracker.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Modify the table schema
            db.execSQL("ALTER TABLE flower_table ADD COLUMN waterInDays INTEGER DEFAULT 0 NOT NULL")
            db.execSQL("ALTER TABLE flower_table ADD COLUMN fertilizeInDays INTEGER DEFAULT 0 NOT NULL")

        }
    }
}