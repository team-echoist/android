package com.echoist.linkedout.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.api.essay.EssayApi
import com.echoist.linkedout.data.EssayItem
import com.echoist.linkedout.page.Token
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
interface MyLogView1Model {
    val myEssayList: List<EssayItem>
    val publishedEssayList: List<EssayItem>
    var detailEssay: EssayItem
    var accessToken: String
    var isActionClicked: Boolean

    fun readEssay(published: Boolean)
}
object FakeMyLogViewModel : MyLogView1Model {
    override val myEssayList: List<EssayItem> = emptyList()
    override val publishedEssayList: List<EssayItem> = emptyList()
    override var detailEssay: EssayItem = EssayItem(
        content = "Fake content",
        createdDate = "2024-05-15",
        id = 0,
        linkedOut = false,
        linkedOutGauge = 0,
        published = false,
        thumbnail = "",
        title = "Fake title",
        updatedDate = "2024-05-15"
    )
    override var accessToken: String = ""
    override var isActionClicked: Boolean = false

    override fun readEssay(published: Boolean) {
        // 가짜 데이터를 반환하므로 아무 동작도 수행하지 않습니다.
    }
}


class MyLogViewModel : ViewModel(),MyLogView1Model {
    override var myEssayList by mutableStateOf(mutableStateListOf<EssayItem>())
    override var publishedEssayList by mutableStateOf(mutableStateListOf<EssayItem>())
    override var detailEssay by mutableStateOf(EssayItem(
            content = "이 에세이는 예시입니다.",
    createdDate = "2024-05-15",
    id = 1,
    linkedOut = true,
    linkedOutGauge = 5,
    published = true,
    thumbnail = "https://example.com/thumbnail.jpg",
    title = "예시 에세이",
    updatedDate = "2024-05-15"
    ))
    override var accessToken : String = "token"
    override var isActionClicked by mutableStateOf(false)



    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()


    private val api = Retrofit
        .Builder()
        .baseUrl("https://www.linkedoutapp.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(EssayApi::class.java)

    //나만의글, 발행한 글 나눠서 가져오기. Published로
    override fun readEssay(published : Boolean){
        myEssayList.clear()
        publishedEssayList.clear()

        viewModelScope.launch {

            try {
                val response = api.readEssay(accessToken = Token.accessToken,published = published)
                Log.d("essaylist data",response.body()!!.path + response.body()!!.data)


                if (response.isSuccessful){
                    accessToken = (response.headers()["authorization"].toString())
                    Token.accessToken = accessToken
                    if (!published){

                        response.body()!!.data.essays.forEach {
                            myEssayList.add(it)
                        }
                    }
                    else{
                        response.body()!!.data.essays.forEach {
                            publishedEssayList.add(it)
                        }
                    }

                }
                myEssayList.forEach{
                    Log.d("essaylistmylist",it.title)

                }
                publishedEssayList.forEach{
                    Log.d("essaylistpublishedmylist",it.title)

                }
            }catch (e:Exception){
                e.printStackTrace()
                Log.d("exception",e.localizedMessage.toString()+Token.accessToken)

                Log.d("exception",e.message.toString()+Token.accessToken)
            }

        }
    }
}