package com.example.myjetpackcomposeapp.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myjetpackcomposeapp.data.db.dao.CategoryDao
import com.example.myjetpackcomposeapp.data.db.dao.ItemDao
import com.example.myjetpackcomposeapp.data.db.entities.Category
import com.example.myjetpackcomposeapp.data.db.entities.Item
import java.util.concurrent.Executors

@Database(
    entities = [Category::class, Item::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_db"
                )    .setQueryCallback({ sqlQuery, bindArgs ->
                    Log.d("RoomQuery", "Query: $sqlQuery, Args: $bindArgs")
                }, Executors.newSingleThreadExecutor()).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
