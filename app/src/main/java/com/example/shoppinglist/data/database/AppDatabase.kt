package com.example.shoppinglist.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shopListDao(): ShopListDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "shop_item.db"

        fun getInstance(application: Application): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = buildDataBase(application)
                }
            }
            return INSTANCE!!
        }

        private fun buildDataBase(application: Application): AppDatabase {
            return Room.databaseBuilder(
                application.applicationContext,
                AppDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}