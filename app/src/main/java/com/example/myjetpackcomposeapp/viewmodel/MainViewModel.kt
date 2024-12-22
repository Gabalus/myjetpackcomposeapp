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
        // Подгружаем категории
        viewModelScope.launch {
            repository.getAllCategories().collect { cats ->
                _uiState.value = _uiState.value.copy(categories = cats)
            }
        }
    }

    fun updateItems(categoryId: Int){
        viewModelScope.launch {
            repository.getItemsByCategory(categoryId).collect { list ->
                _uiState.value = _uiState.value.copy(items = list)
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

    fun deleteItem(itemId: Int) {
        viewModelScope.launch {
            repository.deleteItem(itemId)
        }
    }

    fun addNewItem(
        categoryId: Int,
        shortName: String,
        shortInfo: String?
    ) {
        viewModelScope.launch {
            try {
                val newItem = Item(
                    categoryId = categoryId,
                    shortName = shortName,
                    shortInfo = shortInfo,
                )
                repository.insertItem(newItem)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun onCategorySelected(category: Category) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(selectedCategory = category)
            repository.getItemsByCategory(category.categoryId).collect { list ->
                _uiState.value = _uiState.value.copy(items = list)
            }
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
            repository.searchItems(cat.categoryId, query).collect { list ->
                _uiState.value = _uiState.value.copy(items = list)
            }
        }
    }


    // Пример авторизации:
    fun authenticateUser(login: String, pass: String) {
        // Упрощённо – допустим, всё время успех
        _uiState.value = _uiState.value.copy(isUserAuthenticated = true)
    }

    fun deauthenticateUser() {
        _uiState.value = _uiState.value.copy(isUserAuthenticated = false)
    }

    // Сохранение/обновление данных
    fun updateItemDetail(updatedItem: Item) {
        viewModelScope.launch {
            try {
                repository.updateItem(updatedItem)
                repository.getAllCategories().collect { cats ->
                    _uiState.value = _uiState.value.copy(categories = cats)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }
}
