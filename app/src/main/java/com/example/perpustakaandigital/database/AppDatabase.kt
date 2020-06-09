package com.example.perpustakaandigital.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SkripsiEntity::class], version = 6,exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun skripsiDao(): SkripsiDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "perpus.db"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build()
                }
            }

            return INSTANCE as AppDatabase
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}