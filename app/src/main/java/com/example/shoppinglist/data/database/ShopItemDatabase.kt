package com.example.shoppinglist.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppinglist.domain.ShopItem

@Database(entities = [ShopItem::class], version = 1, exportSchema = false)
abstract class ShopItemDatabase : RoomDatabase() {

    companion object {
        private var instance: ShopItemDatabase? = null
        private const val DB_NAME = "shop_item.db"

        fun getInstance(application: Application): ShopItemDatabase {
            if (instance == null) {
                synchronized(ShopItemDatabase::class) {
                    instance = buildDataBase(application)
                }
            }
            return instance!!
        }

        private fun buildDataBase(application: Application): ShopItemDatabase {
            return Room.databaseBuilder(
                application.applicationContext,
                ShopItemDatabase::class.java,
                DB_NAME
            ).build()
        }
    }

    abstract fun shopItemDao(): ShopItemDatabase
}