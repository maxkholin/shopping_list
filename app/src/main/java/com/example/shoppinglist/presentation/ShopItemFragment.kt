package com.example.shoppinglist.presentation

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

class ShopItemFragment : Fragment() {
    
    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        addTextChangeListeners()
        choiceMode()
        observeViewModel()
    }


    /**
     * Подписка на объекты ViewModel для обработки ошибок ввода и завершения экрана
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
     * Выбор и запуск режима экрана (добавление или редактирование)
     */
    private fun choiceMode() {

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    /**
     * Добавление слушателей изменения текста для скрытия ошибок ввода
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
     * Проверка параметров, устанавливающих режим экрана и ID элемента для редактирования
     * @throws RuntimeException если не передан необходимый параметр
     * или если передано неизвестное значение режима экрана
     */
    private fun parseParams() {
        val args = requireArguments()

        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Не передан режим экрана")
        }

        screenMode = args.getString(SCREEN_MODE).toString()
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Неизвестный режим $screenMode")
        }

        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Не передан ID редактируемого элемента")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, UNDEFINED_ID)
        }

    }

    /**
     * Инициализация всех View фрагмента
     */
    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.button_save)
    }

    /**
     * Запуск в режиме добавдления
     */
    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    /**
     * Запуск в режиме редактирования
     */
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
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNDEFINED_ID = -1
        private const val MODE_UNKNOWN = ""

        /**
         * Создает экземпляр фрагмента для добавления нового элемента списка
         * @return ShopItemFragment, настроенный для режима добавления
         */
        fun newInstanceAddShopItem(): ShopItemFragment {

            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }


        /**
         * Создает экземпляр фрагмента для редактирования существующего элемента списка
         * @param shopItemId ID элемента, который нужно редактировать
         * @return ShopItemFragment, настроенный для режима редактирования
         */
        fun newInstanceEditShopItem(shopItemId: Int): ShopItemFragment {

            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}