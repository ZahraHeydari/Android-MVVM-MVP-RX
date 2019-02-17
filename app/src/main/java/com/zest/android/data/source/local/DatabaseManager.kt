package com.zest.android.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.zest.android.MainApplication
import com.zest.android.data.Category
import com.zest.android.data.Recipe
import com.zest.android.data.source.local.dao.RecipeDao

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {

    abstract val recipeDao: RecipeDao

    companion object {

        private val DB_NAME = "ZestDatabase.db"
        private val instance: DatabaseManager  by lazy { create(MainApplication.instance) }

        @Synchronized
        internal fun getInstance(): DatabaseManager {
            return instance
        }

        private fun create(context: Context): DatabaseManager {
            return Room.databaseBuilder(context, DatabaseManager::class.java, DB_NAME)
                    .allowMainThreadQueries()//to allow run tasks(queries) on main thread
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}
