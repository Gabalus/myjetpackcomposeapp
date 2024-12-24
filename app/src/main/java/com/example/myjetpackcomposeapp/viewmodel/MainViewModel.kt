package com.example.myjetpackcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjetpackcomposeapp.data.db.entities.Item
import com.example.myjetpackcomposeapp.data.db.entities.Category
import com.example.myjetpackcomposeapp.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class UiState(
    val categories: List<Category> = emptyList(),
    val items: List<Item> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedItem: Item? = null,
    val isUserAuthenticated: Boolean = false,
    val errorMessage: String? = null
)

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllCategories().collect { cats ->
                _uiState.value = _uiState.value.copy(categories = cats)
            }
        }
    }


    fun addCategory(name: String) {
        viewModelScope.launch {
            try {
                val newCategory = Category(name = name)
                repository.insertCategory(newCategory)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.deleteItem(item.itemId)
            _uiState.value = _uiState.value.copy(items = repository.getItemsByCategory(item.categoryId))
        }
    }

    fun addNewItem(
        categoryId: Int,
        shortName: String,
    ) {
        viewModelScope.launch {
            try {
                val newItem = Item(
                    categoryId = categoryId,
                    shortName = shortName,
                )
                repository.insertItem(newItem)
                _uiState.value = _uiState.value.copy(items = repository.getItemsByCategory(categoryId))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun onCategorySelected(category: Category) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(selectedCategory = category)
            _uiState.value = _uiState.value.copy(items = repository.getItemsByCategory(category.categoryId))
        }
    }

    fun onItemSelected(itemId: Int) {
        viewModelScope.launch {
            val item = repository.getItemById(itemId)
            _uiState.value = _uiState.value.copy(selectedItem = item)
        }
    }

    fun search(query: String) {
        val cat = _uiState.value.selectedCategory ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(items =repository.searchItems(cat.categoryId, query))
        }
    }


    fun authenticateUser(login: String, pass: String) {
        _uiState.value = _uiState.value.copy(isUserAuthenticated = true)
    }

    fun deauthenticateUser() {
        _uiState.value = _uiState.value.copy(isUserAuthenticated = false)
    }

    fun updateItemDetail(updatedItem: Item) {
        viewModelScope.launch {
            try {
                repository.updateItem(updatedItem)
                _uiState.value = _uiState.value.copy(items = repository.getItemsByCategory(updatedItem.categoryId))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }
}
