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
    var essayList by mutableStateOf(mutableStateListOf<EssayItem>())

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
            try {
                val response = api.readEssay(accessToken = Token.accessToken,published = published)
                Log.d("essaylist",response.path + response.timestamp)
                Log.d("essaylist",response.path + response.data)


                if (response.success){
                    response.data.essays.forEach {
                        essayList.add(it)
                    }
                }
                essayList.forEach{
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