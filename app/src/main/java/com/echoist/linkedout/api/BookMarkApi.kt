package com.echoist.linkedout.api

import com.echoist.linkedout.data.EssayListResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookMarkApi {

    @GET("api/essays/bookmark")
    suspend fun readMyBookMark(
        @Header("Authorization") accessToken: String,
        @Query("page") page : Int? = null,
        @Query("limit") limit: Int? = null
    ): EssayListResponse

    @POST("api/essays/bookmarks/{essayId}")
    suspend fun addBookMark(
        @Header("Authorization") accessToken: String,
        @Path("essayId") essayId: Int
    ): Response<Unit>

    @DELETE("api/essays/bookmarks")
    suspend fun deleteBookMarks(
        @Header("Authorization") accessToken: String,
        @Path("essayIds") essayIds : List<Int>
    ): Response<Unit>

    @DELETE("api/essays/bookmarks")
    suspend fun deleteAllBookMarks(
        @Header("Authorization") accessToken: String
    ): Response<Unit>



}