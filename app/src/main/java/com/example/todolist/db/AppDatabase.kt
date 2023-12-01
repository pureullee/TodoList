package com.example.todolist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist.DateConverter

@Database(entities = arrayOf(ToDoEntity::class), version = 3)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getTodoDao() : ToDoDao

    companion object {
        val databaseName = "db_todo"
        var appDatabase : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase? {
            if(appDatabase == null) {
                appDatabase = Room.databaseBuilder(context,
                    AppDatabase::class.java,
                    databaseName)
                    .addMigrations(migration_1_2)
                    .addMigrations(migration_2_3)
                    .build()
            }
            return appDatabase
        }

        private val migration_1_2 = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase){
                database.execSQL("ALTER TABLE ToDoEntity ADD COLUMN isVisible INTEGER NOT NULL DEFAULT false")
            }
        }

        private val migration_2_3 = object : Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase){
                database.execSQL("ALTER TABLE ToDoEntity ADD COLUMN isCheck INTEGER NOT NULL DEFAULT false")
            }
        }
    }


}