package com.example.myjetpackcomposeapp.data.repository

import com.example.myjetpackcomposeapp.data.db.AppDatabase
import com.example.myjetpackcomposeapp.data.db.entities.Item
import com.example.myjetpackcomposeapp.data.db.entities.Category
import kotlinx.coroutines.flow.Flow

class MainRepository(private val db: AppDatabase) {

    fun getAllCategories(): Flow<List<Category>> =
        db.categoryDao().getAllCategories()

    fun getItemsByCategory(categoryId: Int): Flow<List<Item>> =
        db.itemDao().getItemsByCategory(categoryId)

    suspend fun getItemById(id: Int): Item? =
        db.itemDao().getItemById(id)

    suspend fun insertItem(item: Item) {
        db.itemDao().insertItem(item)
    }

    suspend fun updateItem(item: Item) {
        db.itemDao().updateItem(item)
    }
    suspend fun insertCategory(category: Category) {
        db.categoryDao().insertCategory(category)
    }

    suspend fun deleteItem(id: Int) {
        db.categoryDao().deleteItem(id)
    }

    fun searchItems(categoryId: Int, query: String): Flow<List<Item>> =
        db.itemDao().searchItems(categoryId, query)

}
