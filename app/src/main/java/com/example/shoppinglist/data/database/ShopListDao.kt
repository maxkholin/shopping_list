package com.example.shoppinglist.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_items")
    fun getShopList(): LiveData<List<ShopItemDbEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItem: ShopItemDbEntity)

    @Query("DELETE FROM shop_items WHERE id = :shopItemEntityId")
    suspend fun removeShopItemEntity(shopItemEntityId: Int)

    @Query("SELECT * FROM shop_items WHERE id = :shopItemId LIMIT 1")
    suspend fun getShopIteById(shopItemId: Int): ShopItemDbEntity

}
