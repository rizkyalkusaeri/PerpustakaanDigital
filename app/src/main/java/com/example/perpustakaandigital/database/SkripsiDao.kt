package com.example.perpustakaandigital.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SkripsiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSkripsi(skripsiEntity: SkripsiEntity): Long

    @Query("SELECT * FROM tSkripsi")
    fun getAllSkripsi(): Array<SkripsiEntity>

    @Query("SELECT * FROM tSkripsi WHERE skripsiId = :skripsiId")
    fun getSkripsiById(skripsiId: String): Array<SkripsiEntity>

    @Query("DELETE FROM tSkripsi WHERE skripsiId = :skripsiId")
    fun deleteSkripsi(skripsiId: String)

}