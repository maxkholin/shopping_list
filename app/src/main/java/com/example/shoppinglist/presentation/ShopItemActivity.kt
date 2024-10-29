package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shoppinglist.R

class ShopItemActivity : AppCompatActivity() {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.shop_item_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        parseIntent()
        choiceMode()
    }

    /**
     * Выбор и установка фрагмента в зависимости от режима экрана
     *
     * В зависимости от значения screenMode создает фрагмент для редактирования
     * или добавления элемента и добавляет его в контейнер
     */
    private fun choiceMode() {

        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditShopItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddShopItem()
            else -> throw RuntimeException("Неизвестный режим $screenMode")
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.shop_item_container, fragment)
            .commit()
    }

    /**
     * Метод для обработки intent, устанавливающий режим экрана
     * и ID элемента списка для редактирования.
     * @throws RuntimeException если не передан необходимый параметр
     * или если передано неизвестное значение режима экрана.
     */
    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Не передан режим экрана")
        }

        screenMode = intent.getStringExtra(EXTRA_SCREEN_MODE) ?: MODE_UNKNOWN
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Неизвестный режим $screenMode")
        }

        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Не передан ID редактируемого элемента")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val UNDEFINED_ID = -1
        private const val MODE_UNKNOWN = ""

        fun newIntentAddShopItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditShopItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)

            return intent
        }
    }
}