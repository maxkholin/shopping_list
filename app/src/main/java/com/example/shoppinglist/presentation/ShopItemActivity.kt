package com.example.shoppinglist.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "ShopItemActivity"

class ShopItemActivity : AppCompatActivity() {

    //    private lateinit var viewModel: ShopItemViewModel
//
//    private lateinit var tilName: TextInputLayout
//    private lateinit var tilCount: TextInputLayout
//    private lateinit var etName: EditText
//    private lateinit var etCount: EditText
//    private lateinit var buttonSave: Button
//
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_shop_item)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        parseIntent()
//        initViews()
//
//        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
//        Log.d(TAG, "viewmodel init $viewModel")
//
//        addTextChangeListeners()
        choiceMode()
//        observeViewModel()

    }

//    /**
//     * Подписка на обьекты Вью Модели
//     */
//    private fun observeViewModel() {
//        Log.d(TAG, "observeViewModel")
//        viewModel.errorInputName.observe(this) {
//            tilName.error = if (it) getString(R.string.error_input_name) else null
//        }
//        viewModel.errorInputCount.observe(this) {
//            tilCount.error = if (it) getString(R.string.error_input_count) else null
//        }
//
//        viewModel.shouldCloseScreen.observe(this) {
//            finish()
//        }
//    }
//
    /**
     * Выбор режима экрана
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
//
//    /**
//     * Добавление слушателей изменения текста для скрытия ошибки
//     */
//    private fun addTextChangeListeners() {
//        Log.d(TAG, "addTextChangeListeners")
//        etName.addTextChangedListener {
//            viewModel.resetErrorInputName()
//            Log.d(TAG, "addTextChangeListeners name")
//        }
//        etCount.addTextChangedListener {
//            viewModel.resetErrorInputCount()
//            Log.d(TAG, "addTextChangeListeners count")
//        }
//    }
//
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

    //
//    /**
//     * Инициализация всех вью
//     */
//    private fun initViews() {
//        tilName = findViewById(R.id.til_name)
//        tilCount = findViewById(R.id.til_count)
//        etName = findViewById(R.id.et_name)
//        etCount = findViewById(R.id.et_count)
//        buttonSave = findViewById(R.id.button_save)
//    }
//
//    private fun launchAddMode() {
//        buttonSave.setOnClickListener {
//            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
//    private fun launchEditMode() {
//        Log.d(TAG, "launchEditMode")
//        viewModel.getShopItem(shopItemId)
//        viewModel.shopItem.observe(this) {
//            Log.d(TAG, "observe shopItem before")
//            etName.setText(it.name)
//            etCount.setText(it.count.toString())
//            Log.d(TAG, "observe shopItem after")
//        }
//        buttonSave.setOnClickListener {
//            Log.d(TAG, "buttonSave before")
//            viewModel.changeShopItem(etName.text?.toString(), etCount.text?.toString())
//            Log.d(TAG, "buttonSave after")
//        }
//    }
//
//
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