package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val shopItemId: Int = UNDEFINED_ID
) : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parseParams()
        initViews(view)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        addTextChangeListeners()
        choiceMode()
        observeViewModel()
    }


    /**
     * Подписка на обьекты Вью Модели
     */
    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            tilName.error = if (it) getString(R.string.error_input_name) else null
        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            tilCount.error = if (it) getString(R.string.error_input_count) else null
        }

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    /**
     * Выбор режима экрана
     */
    private fun choiceMode() {

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    /**
     * Добавление слушателей изменения текста для скрытия ошибки
     */
    private fun addTextChangeListeners() {
        etName.addTextChangedListener {
            viewModel.resetErrorInputName()
        }
        etCount.addTextChangedListener {
            viewModel.resetErrorInputCount()
        }
    }

    /**
     * Метод для обработки паметров, устанавливающих режим экрана
     * и ID элемента списка для редактирования.
     * @throws RuntimeException если не передан необходимый параметр
     * или если передано неизвестное значение режима экрана.
     */
    private fun parseParams() {
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Не передан режим экрана")
        }

        if (screenMode == MODE_EDIT && shopItemId == UNDEFINED_ID) {
            throw RuntimeException("Не передан ID редактируемого элемента")
        }

    }


    /**
     * Инициализация всех вью
     */
    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.button_save)
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.changeShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val UNDEFINED_ID = -1
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddShopItem(): ShopItemFragment {
            return ShopItemFragment(MODE_ADD)
        }

        fun newInstanceEditShopItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment(MODE_EDIT, shopItemId)
        }

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