package com.echoist.linkedout.api

import com.echoist.linkedout.data.EssayListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BookMarkApi{
    @GET("api/bookmarks")
    suspend fun readMyBookMark(
        @Header("Authorization") accessToken: String,
        @Header("x-refresh-token") refreshToken: String,
        @Query("page") page : Int? = null,
        @Query("limit") limit: Int? = null
    ): EssayListResponse

    @POST("api/bookmarks/{essayId}")
    suspend fun addBookMark(
        @Header("Authorization") accessToken: String,
        @Header("x-refresh-token") refreshToken: String,
        @Path("essayId") essayId: Int
    ): Response<Unit>

    @PUT("api/bookmarks")
    suspend fun deleteBookMarks(
        @Header("Authorization") accessToken: String,
        @Header("x-refresh-token") refreshToken: String,
        @Body essayIds : RequestDeleteBookMarks
    ): Response<Unit>

    data class RequestDeleteBookMarks(val essayIds: List<Int>)

    @DELETE("api/bookmarks/reset")
    suspend fun deleteAllBookMarks(
        @Header("Authorization") accessToken: String,
        @Header("x-refresh-token") refreshToken: String,
    ): Response<Unit>

}