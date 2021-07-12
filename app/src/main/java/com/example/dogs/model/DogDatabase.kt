package com.example.dogs.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [DogBreed::class])
abstract class DogDatabase : RoomDatabase(){
    abstract fun dogDao(): DogDao

    companion object {
        @Volatile private var instance: DogDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context): DogDatabase = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context): DogDatabase =
            Room.databaseBuilder(context.applicationContext, DogDatabase::class.java, "dogdatabase").build()
    }
}