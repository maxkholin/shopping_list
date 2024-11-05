package com.example.shoppinglist.data.database

import com.example.shoppinglist.domain.ShopItem

object ShopListMapper {

    fun mapShopItemToShopItemDbEntity(shopItem: ShopItem): ShopItemDbEntity = ShopItemDbEntity(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        isEnabled = shopItem.isEnabled
    )

    fun mapShopItemDbEntityToShopItem(shopItemDbEntity: ShopItemDbEntity): ShopItem = ShopItem(
        id = shopItemDbEntity.id,
        name = shopItemDbEntity.name,
        count = shopItemDbEntity.count,
        isEnabled = shopItemDbEntity.isEnabled
    )

    fun mapItemListDbEntityToItemList(list: List<ShopItemDbEntity>): List<ShopItem> {
       return list.map {
            mapShopItemDbEntityToShopItem(it)
        }
    }
}