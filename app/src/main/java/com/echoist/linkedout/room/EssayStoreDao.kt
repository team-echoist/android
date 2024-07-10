package com.echoist.linkedout.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.echoist.linkedout.api.EssayApi

@Dao
interface EssayStoreDao {
    @Insert
    suspend fun insertEssay(essayItem: EssayApi.EssayItem)

    @Query("SELECT * FROM EssayItem WHERE essayPrimaryId = :essayPrimaryId")
    fun getEssayById(essayPrimaryId: Int): EssayApi.EssayItem?

    @Update
    suspend fun updateEssay(essayItem: EssayApi.EssayItem)

    @Delete
    suspend fun deleteEssay(essayItem: EssayApi.EssayItem) : Int

    @Query("DELETE FROM EssayItem WHERE essayPrimaryId = :essayPrimaryId")
    suspend fun deleteEssayById(essayPrimaryId: Int) : Int

    @Query("DELETE FROM EssayItem WHERE essayPrimaryId IN (:essayIds)")
    suspend fun deleteEssaysByIds(essayIds: List<Int?>) : Int

    @Query("SELECT * FROM EssayItem ORDER BY createdDate DESC")
    fun getAllReadData(): List<EssayApi.EssayItem>

    @Transaction
    suspend fun updateOrInsertEssay(essayItem: EssayApi.EssayItem) {
        val existingEssay = essayItem.essayPrimaryId?.let { getEssayById(it) }
        if (existingEssay == null) {
            insertEssay(essayItem)
        } else {
            updateEssay(essayItem)
        }
    }

}
