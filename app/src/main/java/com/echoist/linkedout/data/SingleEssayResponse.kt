
import com.echoist.linkedout.api.EssayApi
import com.squareup.moshi.JsonClass

// data를 리스트형태가 아닌 아이템 하나 단일구조로 받을때의
@JsonClass(generateAdapter = true)
data class SingleEssayResponse(
    val data: EssayApi.EssayItem?,
    val path: String?,
    val success: Boolean,
    val timestamp: String?,
    val statusCode : Int?
)

