package com.example.dogs.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogDao {
    @Insert
    suspend fun insertAllDogs(vararg dogs: DogBreed): List<Long>

    @Query(value = "SELECT * FROM dog_breed")
    suspend fun getAllDogs(): List<DogBreed>

    @Query(value = "SELECT * FROM dog_breed WHERE uuid = :dogId")
    suspend fun getDogById(dogId: Long): DogBreed?

    @Query(value = "DELETE FROM dog_breed")
    suspend fun deleteAllDogs()
}