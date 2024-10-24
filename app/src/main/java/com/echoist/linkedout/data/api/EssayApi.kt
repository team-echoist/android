package com.echoist.linkedout.data.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.echoist.linkedout.data.dto.DetailEssayResponse
import com.echoist.linkedout.data.dto.EssayListResponse
import com.echoist.linkedout.data.dto.NextEssayResponse
import com.echoist.linkedout.data.dto.SingleEssayResponse
import com.echoist.linkedout.data.dto.Story
import com.echoist.linkedout.data.dto.UserInfo
import com.squareup.moshi.JsonClass
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface EssayApi{

    data class Tag(val id : Int, val name : String)

    @POST("api/essays")
    suspend fun writeEssay(
        @Body essayData: WritingEssayItem
    ): Response<SingleEssayResponse>


    @PUT("api/essays/{essayId}") //바디로 바꿔야함
    suspend fun modifyEssay(
        @Path("essayId") essayId: Int,
        @Body essayData: WritingEssayItem
    ): Response<SingleEssayResponse>


    @DELETE("api/essays/{essayId}")
    suspend fun deleteEssay(
        @Path("essayId") essayId: Int
    ): Response<Unit>

    @POST("api/essays/images")
    @Multipart
    suspend fun uploadThumbnail(
        @Part image: MultipartBody.Part,
        @Part("essayId") essayId : Int? = null
    ): Response<UserApi.ImageUrlResponse>

    @GET("api/essays")
    suspend fun readMyEssay(
        @Query("pageType") pageType: String? = null,
        @Query("storyId") storyId: Int? = null,
        @Query("limit") limit: Int = 100, //이 값은 기본 10 수정가능
    ): Response<EssayListResponse>

    @GET("api/essays/recommend")
    suspend fun readRandomEssays(
        @Query("limit") limit: Int = 20, //이 값은 기본 10 수정가능
    ): Response<EssayListResponse>

    @GET("api/essays/followings")
    suspend fun readFollowingEssays(
        @Query("limit") limit: Int = 20, //이 값은 기본 10 수정가능
    ): Response<EssayListResponse>

    @GET("api/essays/sentence")
    suspend fun readOneSentences(
        @Query("type") type : String,
        @Query("limit") limit: Int = 30, //이 값은 기본 10 수정가능
    ): Response<EssayListResponse>

    @GET("api/essays/{essayId}")
    suspend fun readDetailEssay(
        @Path("essayId") essayId: Int = 0,
        @Query("pageType") type : String,
        @Query("storyId") storyId : Int? = null
    ): Response<DetailEssayResponse>

    @GET("api/essays/recent")
    suspend fun readRecentEssays(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10

    ): Response<EssayListResponse>

    @GET("api/essays/search")
    suspend fun readSearchingEssays(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10

    ): EssayListResponse

    @GET("api/essays/next/{essayId}")
    suspend fun readNextEssay(
        @Path("essayId") essayId: Int,
        @Query("pageType") pageType: String,
        @Query("storyId") storyId: Int? = null,
    ):Response<NextEssayResponse>

    data class ReportRequest(
        val reason: String
    )

    @POST("api/reports/{essayId}")
    suspend fun reportEssay(
        @Path("essayId") essayId: Int,
        @Body reason : ReportRequest
    ): Response<Unit>

    @JsonClass(generateAdapter = true)
    data class WritingEssayItem(
        val title: String,
        val content: String,
        val status: String? = null,
        val categoryId: Int? = null,
        val thumbnail: String? = null,
        val linkedOutGauge: Int? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val location: String? = null,
        val tags: List<String>? = null,//보내는 용 string tag list
        val createdDate: String? = null,
        val updatedDate: String? = null,
        val id: Int? = null,
        val nickName: String? = null,
        val author: UserInfo? = null,
        val story: Story? = null
    )

    @Entity
    //받는용 Tag List id와 name으로 구별하여받음.
    @JsonClass(generateAdapter = true)
    data class EssayItem(
        var title: String? = null,
        var content: String? = null,
        var status: String? = null,
        var categoryId: Int? = null,
        var thumbnail: String? = null,
        var linkedOutGauge: Int? = null,
        var latitude: Double? = null,
        var longitude: Double? = null,
        var location: String? = null,
        var tags: List<Tag>? = null,
        var createdDate: String? = null,
        var updatedDate: String? = null,
        var id: Int? = null,
        var author: UserInfo? = null,
        var story: Story? = null,
        var isBookmarked: Boolean = false,

        @PrimaryKey(autoGenerate = true)
        var essayPrimaryId: Int =0
    )
}
fun EssayApi.EssayItem.toWritingEssayItem(): EssayApi.WritingEssayItem {
    return EssayApi.WritingEssayItem(
        title = this.title ?: "",
        content = this.content ?: "",
        status = this.status,
        categoryId = this.categoryId,
        thumbnail = this.thumbnail,
        linkedOutGauge = this.linkedOutGauge,
        latitude = this.latitude,
        longitude = this.longitude,
        location = this.location,
        tags = this.tags?.map { it.name },
        createdDate = this.createdDate,
        updatedDate = this.updatedDate,
        id = this.id,
        nickName = this.author?.nickname,
        author = this.author,
        story = this.story
    )
}