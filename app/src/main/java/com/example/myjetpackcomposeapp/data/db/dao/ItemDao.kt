package com.example.myjetpackcomposeapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myjetpackcomposeapp.data.db.entities.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM item WHERE categoryId = :categoryId")
    fun getItemsByCategory(categoryId: Int): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE itemId = :id")
    suspend fun getItemById(id: Int): Item?

    @Query("SELECT * FROM item WHERE categoryId = :categoryId AND shortName LIKE '%' || :searchQuery || '%'")
    fun searchItems(categoryId: Int, searchQuery: String): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("DELETE FROM category WHERE categoryId = :id")
    suspend fun deleteItem(id: Int)
}
