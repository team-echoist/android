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

class MyLogViewModel : ViewModel() {
    var myEssayList by mutableStateOf(mutableStateListOf<EssayItem>())
    var publishedEssayList by mutableStateOf(mutableStateListOf<EssayItem>())
    var detailEssay by mutableStateOf(EssayItem(
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
    var accessToken : String = "token"
    var isActionClicked by mutableStateOf(false)



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
    fun readEssay(published : Boolean){
        viewModelScope.launch {
            myEssayList.clear()
            publishedEssayList.clear()
            try {
                val response = api.readEssay(accessToken = Token.accessToken,published = published)
                Log.d("essaylist", response.body()!!.path + response.body()!!.timestamp)
                Log.d("essaylist",response.body()!!.path + response.body()!!.data)


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
                    Log.d("essaylist",it.title)

                }
            }catch (e:Exception){
                e.printStackTrace()
                Log.d("exception",e.localizedMessage.toString()+Token.accessToken)

                Log.d("exception",e.message.toString()+Token.accessToken)
            }

        }
    }
}