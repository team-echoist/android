package com.echoist.linkedout.viewModels

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.data.BadgeBoxItem
import com.echoist.linkedout.data.BadgeBoxItemWithTag
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.data.toBadgeBoxItem
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val userApi: UserApi,
    private val exampleItems: ExampleItems
) : ViewModel() {
    var isClickedModifyImage by mutableStateOf(false)

    var newProfile by mutableStateOf(UserInfo())

    var isLevelUpSuccess by mutableStateOf(false)

    var isBadgeClicked by mutableStateOf(false)
    var badgeBoxItem: BadgeBoxItem? by mutableStateOf(null)

    var isLoading by mutableStateOf(false)

    fun getFileFromUri(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val fileName = getFileName(uri, contentResolver)
        val file = File(context.cacheDir, fileName.toString())
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    private fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
        var name: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

    suspend fun uploadImage(uri: Uri, context: Context): String? { //서버에 이미지 업로드하고 url을 반환

            try {
                val file = getFileFromUri(uri, context)
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                // 서버로 업로드 요청 보내기
                val response = userApi.userImageUpload(Token.accessToken, body)

                if (response.isSuccessful){
                    newProfile.profileImage = response.body()!!.data.imageUrl
                    exampleItems.myProfile.profileImage = response.body()!!.data.imageUrl
                    return response.body()!!.data.imageUrl
                }else
                    return  null

                // 서버 응답에서 URL 추출
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            } finally {

            }

    }
    fun getRecentViewedEssayList() : List<EssayApi.EssayItem>{
        return exampleItems.recentViewedEssayList
    }

    suspend fun requestMyInfo(){
        viewModelScope.launch {
            try {

                val response = userApi.getMyInfo(Token.accessToken)
                Log.d(TAG, "readMyInfo: suc1")
                exampleItems.myProfile = response.data.user
                exampleItems.myProfile.essayStats = response.data.essayStats
                Log.i(TAG, "readMyInfo: ${exampleItems.myProfile}")


            }catch (e: Exception){
                Log.d(TAG, "readMyInfo: error err")
                e.printStackTrace()
                Log.d(TAG, e.message.toString())
                Log.d(TAG, e.cause.toString())


            }
        }

    }


    fun getMyInfo() : UserInfo{
        Log.d("example item", "readMyInfo: ${exampleItems.myProfile}")
        return exampleItems.myProfile
    }

    fun readSimpleBadgeList() {
        //이게 작동되는지가 중요
        viewModelScope.launch {
            try {
                //readMyInfo()
                val response = userApi.readBadgeList( Token.accessToken)
                Log.d(TAG, "readSimpleBadgeList: ${response.body()!!.data.badges}")
                response.body()?.data?.badges!!.let{badges ->
                    exampleItems.simpleBadgeList = badges.map { it.toBadgeBoxItem() }.toMutableStateList()
                }
                Log.d(TAG, "readSimpleBadgeList: ${exampleItems.simpleBadgeList}")

                Token.accessToken = (response.headers()["authorization"].toString())


            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.localizedMessage}")

            }
        }
    }

    fun getSimpleBadgeList() : List<BadgeBoxItem>{
        return exampleItems.simpleBadgeList.toList()
    }

    fun readDetailBadgeList(navController: NavController) {
        viewModelScope.launch {
            try {
                val response = userApi.readBadgeWithTagsList(Token.accessToken)
                response.body()?.data?.badges?.let { badges ->
                    // badges 리스트를 SnapshotStateList로 변환
                    exampleItems.detailBadgeList = badges.map { it.toBadgeBoxItem() }.toMutableStateList() as SnapshotStateList<BadgeBoxItemWithTag>
                }
                Log.d(TAG, "readdetailList: ${exampleItems.detailBadgeList}")

                Token.accessToken = response.headers()["authorization"].toString()

                navController.navigate("BadgePage")
            } catch (e: Exception) {
                e.printStackTrace()
                // API 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun getDetailBadgeList() : List<BadgeBoxItemWithTag>{
        return exampleItems.detailBadgeList.toList()
    }

    fun requestBadgeLevelUp(badgeId : Int) {
        viewModelScope.launch {
            try {

                val response = userApi.requestBadgeLevelUp(Token.accessToken, badgeId)
                Log.d(TAG, "requestBadgeLevelUp: ${Token.accessToken}")

                Token.accessToken = (response.headers()["authorization"].toString())
                if (response.body()!!.success){
                    isLevelUpSuccess = true
                    delay(1000)
                    isLevelUpSuccess = false
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun updateMyInfo(userInfo: UserInfo,navController: NavController) {
        viewModelScope.launch {
            isLoading = true
            try {

                val response = userApi.userUpdate(Token.accessToken, userInfo)


                Log.d(TAG, "updateMyInfo: ${userInfo.profileImage}")
                Log.d(TAG, "updateMyInfo: ${newProfile.profileImage}")

                Log.d(TAG, "requestBadgeLevelUp: ${Token.accessToken}")
                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())

                    //todo api가 성공한다는 시점에서 바로 바뀌게 만듦.
                    exampleItems.myProfile.profileImage = newProfile.profileImage
                    exampleItems.myProfile.nickname = newProfile.nickname

                    readSimpleBadgeList()
                    getMyInfo()
                    navController.navigate("SETTINGS")
                    newProfile = UserInfo()

                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

    var isApiFinished by mutableStateOf(false)
    fun readRecentEssays(){
        isApiFinished = false
        viewModelScope.launch {
            isLoading = true

            try {
                val response = essayApi.readRecentEssays(Token.accessToken)

                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    exampleItems.recentViewedEssayList = response.body()!!.data.essays.toMutableStateList()
                    isApiFinished = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

    fun requestWithdrawal(reasons : List<String>, navController: NavController){
        viewModelScope.launch {
            isLoading = true
            try {

                val body = UserApi.RequestDeactivate(reasons)
                val response = userApi.requestDeactivate(Token.accessToken,body)
                Log.d(TAG, "requestWithdrawal: $reasons")

                if (response.isSuccessful){
                    Token.accessToken = (response.headers()["authorization"].toString())
                    navController.navigate("LoginPage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("ApiFailed", "Failed to write essay: ${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

}