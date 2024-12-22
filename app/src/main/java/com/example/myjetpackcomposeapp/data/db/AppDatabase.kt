package com.example.myjetpackcomposeapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myjetpackcomposeapp.data.db.dao.CategoryDao
import com.example.myjetpackcomposeapp.data.db.dao.ItemDao
import com.example.myjetpackcomposeapp.data.db.entities.Category
import com.example.myjetpackcomposeapp.data.db.entities.Item

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
                    "my_local_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
