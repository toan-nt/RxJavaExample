package com.toantmt.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.toantmt.db.dao.ProductDao
import com.toantmt.db.entities.ProductEntity
import com.toantmt.db.entities.ProductDetailEntity

@Database(
    entities = [
        ProductEntity::class,
        ProductDetailEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var appDataBase: AppDatabase? = null

        fun initDatabase(context: Context) {
            if (appDataBase != null) return

            appDataBase = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "${context.applicationContext.packageName}.chat.sqlite3"
            )
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .build()
        }

        fun get(): AppDatabase {
            return appDataBase ?: throw Exception("AppDatabase is not init already")
        }
    }
}