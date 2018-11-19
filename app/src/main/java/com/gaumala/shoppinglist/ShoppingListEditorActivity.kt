package com.gaumala.shoppinglist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.gaumala.shoppinglist.db.AppDatabase
import com.gaumala.shoppinglist.utils.AppCtx

import kotlinx.android.synthetic.main.activity_shopping_list_editor.*

class ShoppingListEditorActivity : AppCompatActivity() {

    lateinit var mainView: ShoppingListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this)
                .get(ShoppingListViewModel::class.java)
        if (viewModel.needsInitialization)
            initializeViewModel(viewModel)
        else
            setupView(viewModel)
    }

    private fun initializeViewModel(viewModel: ShoppingListViewModel) {
        val appCtx = AppCtx.Default(this)
        val appDB = AppDatabase.newInstance(this)
        val repo = ShoppingListRepository.Default(appCtx, appDB)

        val defaultListName = appCtx.getLocalizedString(
            R.string.my_shopping_list)

        repo.loadShoppingList(defaultListName) { shoppingList ->
            val newState = ShoppingListState(
                shoppingList.id,
                shoppingList.name,
                shoppingList.items)
            viewModel.initialize(newState, repo)

            setupView(viewModel)
        }
    }

    private fun setupView(viewModel: ShoppingListViewModel) {
        setContentView(R.layout.activity_shopping_list_editor)
        setSupportActionBar(toolbar)
        supportActionBar?.title = viewModel.listName
        mainView = ShoppingListView(this, viewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
