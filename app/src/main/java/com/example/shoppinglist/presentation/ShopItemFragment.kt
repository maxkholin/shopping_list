package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.databinding.FragmentShopItemBinding


class ShopItemFragment : Fragment() {

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var viewModel: ShopItemViewModel

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Активити должна реализовывать OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        addTextChangeListeners()
        choiceMode()
        observeViewModel()
    }


    /**
     * Подписка на объекты ViewModel для обработки ошибок ввода и завершения экрана
     */
    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
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
        binding.etName.addTextChangedListener {
            viewModel.resetErrorInputName()
        }
        binding.etCount.addTextChangedListener {
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
     * Запуск в режиме добавдления
     */
    private fun launchAddMode() {
        binding.buttonSave.setOnClickListener {
            viewModel.addShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
        }
    }

    /**
     * Запуск в режиме редактирования
     */
    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        binding.buttonSave.setOnClickListener {
            viewModel.changeShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
        }
    }


    interface OnEditingFinishedListener {

        fun onEditingFinished()
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