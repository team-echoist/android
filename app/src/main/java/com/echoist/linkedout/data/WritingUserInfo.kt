import com.echoist.linkedout.api.EssayApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WritingUserInfo(
    @Json(name = "data")
    val data: EssayApi.EssayItem?,
    @Json(name = "path")
    val path: String?,
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "timestamp")
    val timestamp: String?
)

