package com.echoist.linkedout.room

import androidx.annotation.RequiresPermission.Read
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.echoist.linkedout.api.EssayApi

@Dao
interface EssayStoreDao {
    @Insert // 쿼리문 select 문이 내재되어있는듯? 데이터삽임
    fun insertEssay(essayItem: EssayApi.EssayItem) // 구현은 가져다쓸때

    @Read

    @Update
    fun updateEssay(essayItem: EssayApi.EssayItem)

    @Delete
    fun deleteEssay(essayItem: EssayApi.EssayItem)

    //데이터베이스의 전체데이터 조회. tododate가 가장 최신인 순으로 정렬해서 가지고온다.
//    @Query("SELECT * FROM EssayApi.EssayItem ORDER BY createdDate")
//    fun getAllReadData() : List<EssayApi.EssayItem>
}