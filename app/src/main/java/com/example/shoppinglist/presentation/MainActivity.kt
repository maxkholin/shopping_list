package com.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var rvShopList: RecyclerView
    private lateinit var shopItemAdapter: ShopItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) { shopList ->
            shopItemAdapter.submitList(shopList)
        }

    }

    /**
     * Настройка Ресайклер Вью
     */
    private fun setupRecyclerView() {
        rvShopList = findViewById(R.id.rv_shop_list)
        shopItemAdapter = ShopItemAdapter()
        with(rvShopList) {
            adapter = shopItemAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopItemAdapter.VIEW_TYPE_ENABLED,
                ShopItemAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopItemAdapter.VIEW_TYPE_DISABLED,
                ShopItemAdapter.MAX_POOL_SIZE
            )
        }

        setupLongClickListener()
        setupClickListener()
        setupSwipeListener()
    }

    private fun setupSwipeListener() {
        val callback = object :ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopItemAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        shopItemAdapter.onShopItemClickListener = {
            Log.d(TAG, "$it")
        }
    }

    private fun setupLongClickListener() {
        shopItemAdapter.onShopItemLongClickListener = {
            viewModel.changeIsEnabledState(it)
        }
    }

}
