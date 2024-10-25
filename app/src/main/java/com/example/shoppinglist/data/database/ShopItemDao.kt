package com.example.shoppinglist.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.domain.ShopItem

@Dao
interface ShopItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItem: ShopItem)

    @Delete
    suspend fun removeShopItem(shopItemId: Int)

    @Update
    suspend fun editShopItem(shopItem: ShopItem)

    @Query("SELECT * FROM shop_items WHERE id = :shopItemId")
    fun getShopIteById(shopItemId: Int): LiveData<ShopItem>

    @Query("SELECT * FROM shop_items")
    fun getShopList(): LiveData<List<ShopItem>>

}
