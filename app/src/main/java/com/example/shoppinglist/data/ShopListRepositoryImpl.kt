package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.shoppinglist.data.database.AppDatabase
import com.example.shoppinglist.data.database.ShopListMapper
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl(
    application: Application
) : ShopListRepository {

    private val shopListDao = AppDatabase.getInstance(application).shopListDao()

     override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(ShopListMapper.mapShopItemToShopItemDbEntity(shopItem))
    }

     override suspend fun removeShopItem(shopItem: ShopItem) {
        shopListDao.removeShopItemEntity(shopItem.id)
    }

     override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(ShopListMapper.mapShopItemToShopItemDbEntity(shopItem))
    }

     override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val entity = shopListDao.getShopIteById(shopItemId)
        return ShopListMapper.mapShopItemDbEntityToShopItem(entity)
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListDao.getShopList().map {
            ShopListMapper.mapItemListDbEntityToItemList(it)
        }
    }

}