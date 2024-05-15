
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.essay.EssayApi
import com.echoist.linkedout.data.EssayItem
import com.echoist.linkedout.page.Token
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Stack

interface MyLogView1Model {
    val myEssayList: List<EssayItem>
    val publishedEssayList: List<EssayItem>
    var detailEssay: EssayItem
    var accessToken: String
    var isActionClicked: Boolean
    var detailEssayBackStack : Stack<EssayItem>

    fun readEssay(published: Boolean)
    fun deleteEssay(navController: NavController)
}

class MyLogViewModel : ViewModel(), MyLogView1Model {
    override var myEssayList by mutableStateOf(mutableStateListOf<EssayItem>())
    override var publishedEssayList by mutableStateOf(mutableStateListOf<EssayItem>())
    override var detailEssayBackStack = Stack<EssayItem>()
    override var detailEssay by mutableStateOf(
        EssayItem(
        content = "이 에세이는 예시입니다.",
        createdDate = "2024-05-15",
        id = 1,
        linkedOut = true,
        linkedOutGauge = 5,
        published = true,
        thumbnail = "https://example.com/thumbnail.jpg",
        title = "예시 에세이",
        updatedDate = "2024-05-15"
    )
    )
    override var accessToken: String = "token"
    override var isActionClicked by mutableStateOf(false)

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("https://www.linkedoutapp.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(EssayApi::class.java)

    override fun readEssay(published: Boolean) {
        myEssayList.clear()
        publishedEssayList.clear()

        viewModelScope.launch {
            try {
                val response = api.readEssay(accessToken = Token.accessToken, published = published)
                Log.d("essaylist data", response.body()!!.path + response.body()!!.data)

                if (response.isSuccessful) {
                    accessToken = (response.headers()["authorization"].toString())
                    Token.accessToken = accessToken
                    if (!published) {
                        response.body()!!.data.essays.forEach {
                            myEssayList.add(it)
                        }
                    } else {
                        response.body()!!.data.essays.forEach {
                            publishedEssayList.add(it)
                        }
                    }
                }
                myEssayList.forEach {
                    Log.d("essaylistmylist", it.title)
                }
                publishedEssayList.forEach {
                    Log.d("essaylistpublishedmylist", it.title)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("exception", e.localizedMessage.toString() + Token.accessToken)
                Log.d("exception", e.message.toString() + Token.accessToken)
            }
        }
    }

    override fun deleteEssay(navController: NavController) {
        viewModelScope.launch {
            try {
                val response = api.deleteEssay(accessToken,detailEssay.id.toString())

                Log.d("writeEssayApiSuccess2", "writeEssayApiSuccess: ${response.isSuccessful}")
                Log.d("writeEssayApiFailed", "deleteEssaytoken: ${Token.accessToken}")
                Log.d("writeEssayApiFailed", "deleteEssayid: ${detailEssay.id}")

                Log.d("writeEssayApiFailed", "deleteEssay: ${response.errorBody()}")
                Log.d("writeEssayApiFailed", "deleteEssay: ${response.code()}")


                if (response.isSuccessful) {
                    accessToken = (response.headers()["authorization"].toString())
                    Token.accessToken = accessToken
                    Log.e("writeEssayApiSuccess", "${response.headers()}")
                    Log.e("writeEssayApiSuccess", "${response.code()}")
                    isActionClicked = false
                    navController.navigate("MYLOG") {
                        popUpTo("MYLOG") {
                            inclusive = false
                        }
                    }
                }
                else{
                    Log.e("writeEssayApiFailed", "${response.errorBody()}")
                    Log.e("writeEssayApiFailed", "${response.code()}")
                }


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }
}
