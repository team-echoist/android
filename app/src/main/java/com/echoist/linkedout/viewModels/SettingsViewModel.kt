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
import com.echoist.linkedout.page.myLog.Token.bearerAccessToken
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
                val response = userApi.userImageUpload(bearerAccessToken,Token.refreshToken, body)

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

    fun requestMyInfo(){
        viewModelScope.launch {
            try {

                val response = userApi.getMyInfo(bearerAccessToken,Token.refreshToken)

                Log.d(TAG, "readMyInfo: suc1")
                exampleItems.myProfile = response.data.user
                exampleItems.myProfile.essayStats = response.data.essayStats
                Log.i(TAG, "readMyInfo: ${exampleItems.myProfile}")
                getMyInfo()

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
                val response = userApi.readBadgeList( bearerAccessToken,Token.refreshToken)
                Log.d(TAG, "readSimpleBadgeList: ${response.body()!!.data.badges}")
                response.body()?.data?.badges!!.let{badges ->
                    exampleItems.simpleBadgeList = badges.map { it.toBadgeBoxItem() }.toMutableStateList()
                }
                Log.d(TAG, "readSimpleBadgeList: ${exampleItems.simpleBadgeList}")
                Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken


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
                val response = userApi.readBadgeWithTagsList(bearerAccessToken,Token.refreshToken)
                response.body()?.data?.badges?.let { badges ->
                    // badges 리스트를 SnapshotStateList로 변환
                    exampleItems.detailBadgeList = badges.map { it.toBadgeBoxItem() }.toMutableStateList() as SnapshotStateList<BadgeBoxItemWithTag>
                }
                Log.d(TAG, "readdetailList: ${exampleItems.detailBadgeList}")
                Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken


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

                val response = userApi.requestBadgeLevelUp(bearerAccessToken,Token.refreshToken, badgeId)
                Log.d(TAG, "requestBadgeLevelUp: ${Token.accessToken}")

                Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken

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

                val response = userApi.userUpdate(bearerAccessToken,Token.refreshToken, userInfo)

                if (response.isSuccessful){
                    Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken

                    Log.d("업데이트 요청 성공", "updateMyInfo: ${newProfile.profileImage}")

                    exampleItems.myProfile.profileImage = newProfile.profileImage
                    exampleItems.myProfile.nickname = newProfile.nickname

                    readSimpleBadgeList()
                    getMyInfo()
                    navController.navigate("SETTINGS")
                    newProfile = UserInfo()

                }
                else{
                    Log.e("업데이트 요청 실패", "updateMyInfo: ${response.code()}")

                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("업데이트 요청 실패", "updateMyInfo: ${e.printStackTrace()}")

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
                val response = essayApi.readRecentEssays(bearerAccessToken,Token.refreshToken)

                if (response.isSuccessful){
                                        Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken
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
                val response = userApi.requestDeactivate(bearerAccessToken,Token.refreshToken,body)
                Log.d(TAG, "requestWithdrawal: $reasons")

                if (response.isSuccessful){
                    Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken
                    navController.navigate("LoginPage")
                }
                else{
                    Log.e("탈퇴 실패", "코드 :  ${response.code()}")
                }
            }catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("탈퇴 요청 실패", "${e.message}")
            }
            finally {
                isLoading = false
            }
        }
    }

    var nicknameCheckCode by mutableStateOf(200)
    fun requestNicknameDuplicated(nickname : String){
        viewModelScope.launch {
            try {

                val nickname = UserApi.NickName(nickname)
                val response = userApi.requestNicknameDuplicated(bearerAccessToken,Token.refreshToken,nickname)
                Log.d("닉네임 중복검사", "requestNicknameDuplicated: $nickname")

                if (response.isSuccessful){ //실시간요청이기때문에 토큰사용 x
                    //                    Token.accessToken = response.headers()["authorization"]?.takeIf { it.isNotEmpty() } ?: Token.accessToken
                    Log.d("닉네임 중복검사 성공", "코드: ${response.code()}")
                    nicknameCheckCode = response.code()
                }
                else{
                    Log.e("닉네임 중복검사 실패", "코드: ${response.code()}")
                    nicknameCheckCode = response.code()

                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("ApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

}