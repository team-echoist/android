package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.Routes
import com.echoist.linkedout.TYPE_COMMUNITY
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.SupportApi
import com.echoist.linkedout.data.Alert
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.Inquiry
import com.echoist.linkedout.data.Notice
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.myLog.Token
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val exampleItems: ExampleItems,
    private val essayApi: EssayApi,
    private val supportApi: SupportApi
) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var alertList: SnapshotStateList<Alert> =  mutableStateListOf()
    var noticeList: SnapshotStateList<Notice> =  mutableStateListOf()
    var detailNotice : Notice? by mutableStateOf(null)

     private val _inquiryList = MutableStateFlow<List<Inquiry>>(emptyList())
    val inquiryList: StateFlow<List<Inquiry>>
        get() = _inquiryList.asStateFlow()

    fun readMyProfile() : UserInfo{
        return exampleItems.myProfile
    }

    fun readAlertsList(){
        viewModelScope.launch {
            isLoading = true
            try {
                val response = supportApi.readAlertsList(Token.accessToken)
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    alertList = response.body()!!.data.alerts.toMutableStateList()

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                isLoading = false

            }
        }
    }

    fun readAlert(alertId :Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = supportApi.readAlert(Token.accessToken,alertId)
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                isLoading = false
            }
        }
    }

    fun readDetailEssay(id: Int, navController: NavController) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = essayApi.readDetailEssay(Token.accessToken, id,TYPE_COMMUNITY)
                exampleItems.detailEssay = response.body()!!.data.essay
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data}")
                Log.d(TAG, "readdetailEssay: 성공인데요${response.body()!!.data.essay.title}")

                if (response.body()!!.data.anotherEssays != null) {
                    exampleItems.previousEssayList = response.body()!!.data.anotherEssays!!.essays.toMutableStateList()

                }
                Log.d(TAG, "readDetailEssay: previouse ${exampleItems.detailEssay}")

                Log.d(TAG, "readDetailEssay: anotherEssays ${exampleItems.previousEssayList}")
                navController.navigate("CommunityDetailPage")

                // API 호출 결과 처리 (예: response 데이터 사용)
            } catch (e: Exception) {

                // 예외 처리
                e.printStackTrace()
                Log.d(TAG, "readRandomEssays: ${e.message}")
                Log.d(TAG, "readRandomEssays: ${e.cause}")
                Log.d(TAG, "readRandomEssays: ${e.localizedMessage}")

            }
            finally {
                isLoading = false
            }
        }
    }

    fun writeInquiry(title : String, content : String, type : String, navController: NavController){
        viewModelScope.launch {
            isLoading = true
            try {
                val body = Inquiry(title = title,content = content,type = type)
                val response = supportApi.writeInquiry(Token.accessToken,body)
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    //todo alert 성공!
                    navController.navigate("${Routes.Home}/200")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                isLoading = false
            }
        }
    }

    fun readInquiryList(){
        viewModelScope.launch {
            isLoading = true
            try {
                val response = supportApi.readInquiries(Token.accessToken)
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    //todo alert 성공!
                    _inquiryList.emit(response.body()!!.data)
                    Log.d(TAG, "readInquiryList: $inquiryList")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                isLoading = false
            }
        }
    }

    suspend fun readDetailInquiry(inquiryId: Int): Inquiry? {
        isLoading = true
        return try {
            val response = supportApi.readInquiryDetail(Token.accessToken, inquiryId)
            if (response.isSuccessful) {
                Token.accessToken = response.headers()["authorization"].toString()
                response.body()?.data // 성공 시 response.body()?.data를 반환
            } else {
                null // 실패 시 null 반환
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // 예외 발생 시 null 반환
        } finally {
            isLoading = false
        }
    }

    fun requestNoticesList(){
        noticeList.clear()
        viewModelScope.launch {
            try {
                val response = supportApi.readNotices(Token.accessToken)
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    noticeList.addAll(response.body()!!.data.Notices)
                    Log.d("공지사항 목록 불러오기", "성공: $noticeList")
                }
                else{
                    Log.e("공지사항 목록 불러오기", "실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("공지사항 목록 불러오기", "실패: ${e.printStackTrace()}")
                e.printStackTrace()
            }
        }
    }

    fun requestDetailNotice(noticeId : Int,navController: NavController){

        viewModelScope.launch {
            try {
                val response = supportApi.readNoticeDetail(Token.accessToken,noticeId)
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    Log.d("공지사항 디테일 확인", "성공 내용 : ${response.body()!!.data.content}")
                    Log.d("공지사항 디테일 확인", "성공 시간 : ${response.body()!!.data.createdDate}")


                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val jsonAdapter = moshi.adapter(Notice::class.java)

                    val data = response.body()!!.data
                    val json = jsonAdapter.toJson(data)
                    val encodedJson = URLEncoder.encode(json, "UTF-8")

                    detailNotice = response.body()!!.data
                    navController.navigate("${Routes.NoticeDetailPage}/$encodedJson")
                }
                else{
                    Log.e("공지사항 디테일 확인", "실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("공지사항 디테일 확인", "실패: ${e.printStackTrace()}")
                e.printStackTrace()
            }
        }
    }
}