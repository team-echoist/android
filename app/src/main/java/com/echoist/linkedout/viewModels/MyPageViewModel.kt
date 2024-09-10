package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
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
import com.echoist.linkedout.getFileFromUri
import com.echoist.linkedout.page.myLog.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val essayApi: EssayApi,
    private val userApi: UserApi,
    private val exampleItems: ExampleItems
) : ViewModel() {
    var newProfile by mutableStateOf(UserInfo())
    var isClickedModifyImage by mutableStateOf(false)

    var isLevelUpSuccess by mutableStateOf(false)

    var isBadgeClicked by mutableStateOf(false)
    var badgeBoxItem: BadgeBoxItem? by mutableStateOf(null)

    var isLoading by mutableStateOf(false)

    private val _userProfile = MutableStateFlow(exampleItems.myProfile)
    val userProfile: StateFlow<UserInfo> = _userProfile

    private val _tempProfile = MutableStateFlow(userProfile.value)
    val tempProfile: StateFlow<UserInfo> = _tempProfile

    suspend fun uploadImage(uri: Uri, context: Context): String? { //서버에 이미지 업로드하고 url을 반환
        try {
            val file = getFileFromUri(uri, context)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val response = userApi.userImageUpload(body)

            if (response.isSuccessful) {
                _userProfile.value.profileImage = response.body()!!.data.imageUrl
                exampleItems.myProfile.profileImage = response.body()!!.data.imageUrl
                return response.body()!!.data.imageUrl
            } else
                return null

            // 서버 응답에서 URL 추출
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
        }
    }

    fun getRecentViewedEssayList(): List<EssayApi.EssayItem> {
        return exampleItems.recentViewedEssayList
    }

    fun requestMyInfo() {
        viewModelScope.launch {
            try {
                val response = userApi.getMyInfo()

                Log.d(TAG, "readMyInfo: suc1")
                exampleItems.myProfile = response.data.user
                exampleItems.myProfile.essayStats = response.data.essayStats
                Log.i(TAG, "readMyInfo: ${exampleItems.myProfile}")
                getMyInfo()
            } catch (e: Exception) {
                Log.d(TAG, "readMyInfo: error err")
                e.printStackTrace()
                Log.d(TAG, e.message.toString())
                Log.d(TAG, e.cause.toString())
            }
        }
    }

    fun getMyInfo(): UserInfo {
        Log.d("example item", "readMyInfo: ${exampleItems.myProfile}")
        return exampleItems.myProfile
    }

    fun readSimpleBadgeList() {
        //이게 작동되는지가 중요
        viewModelScope.launch {
            try {
                //readMyInfo()
                val response = userApi.readBadgeList()
                Log.d(TAG, "readSimpleBadgeList: ${response.body()!!.data.badges}")
                response.body()?.data?.badges!!.let { badges ->
                    exampleItems.simpleBadgeList =
                        badges.map { it.toBadgeBoxItem() }.toMutableStateList()
                }
                Log.d(TAG, "readSimpleBadgeList: ${exampleItems.simpleBadgeList}")
                Token.accessToken = response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                    ?: Token.accessToken
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.localizedMessage}")
            }
        }
    }

    fun getSimpleBadgeList(): List<BadgeBoxItem> {
        return exampleItems.simpleBadgeList.toList()
    }

    fun readDetailBadgeList(navController: NavController) {
        viewModelScope.launch {
            try {
                val response = userApi.readBadgeWithTagsList()
                response.body()?.data?.badges?.let { badges ->
                    // badges 리스트를 SnapshotStateList로 변환
                    exampleItems.detailBadgeList = badges.map { it.toBadgeBoxItem() }
                        .toMutableStateList() as SnapshotStateList<BadgeBoxItemWithTag>
                }
                Log.d(TAG, "readdetailList: ${exampleItems.detailBadgeList}")
                Token.accessToken = response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                    ?: Token.accessToken

                navController.navigate("BadgePage")
            } catch (e: Exception) {
                e.printStackTrace()
                // API 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            }
        }
    }

    fun getDetailBadgeList(): List<BadgeBoxItemWithTag> {
        return exampleItems.detailBadgeList.toList()
    }

    // badgeViewModel 분리
    fun requestBadgeLevelUp(badgeId: Int) {
        viewModelScope.launch {
            try {
                val response = userApi.requestBadgeLevelUp(badgeId)
                Log.d(TAG, "requestBadgeLevelUp: ${Token.accessToken}")

                Token.accessToken = response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                    ?: Token.accessToken

                if (response.body()!!.success) {
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

    fun updateMyInfo(navController: NavController) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = userApi.userUpdate(tempProfile.value)

                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    exampleItems.myProfile.profileImage = tempProfile.value.profileImage
                    exampleItems.myProfile.nickname = tempProfile.value.nickname
                    _userProfile.value = tempProfile.value
                    readSimpleBadgeList()
                    getMyInfo()
                    navController.navigate("SETTINGS")
                } else {
                    Log.e("업데이트 요청 실패", "updateMyInfo: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("업데이트 요청 실패", "updateMyInfo: ${e.printStackTrace()}")
            } finally {
                isLoading = false
            }
        }
    }

    var isApiFinished by mutableStateOf(false)
    fun readRecentEssays() {
        isApiFinished = false
        viewModelScope.launch {
            isLoading = true
            try {
                val response = essayApi.readRecentEssays()
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    exampleItems.recentViewedEssayList =
                        response.body()!!.data.essays.toMutableStateList()
                    isApiFinished = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("writeEssayApiFailed", "Failed to write essay: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun requestWithdrawal(reasons: List<String>, navController: NavController) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = UserApi.RequestDeactivate(reasons)
                val response = userApi.requestDeactivate(body)
                Log.d(TAG, "requestWithdrawal: $reasons")
                if (response.isSuccessful) {
                    Token.accessToken =
                        response.headers()["x-access-token"]?.takeIf { it.isNotEmpty() }
                            ?: Token.accessToken
                    navController.navigate("LoginPage") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                } else {
                    Log.e("탈퇴 실패", "코드 :  ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // api 요청 실패
                Log.e("탈퇴 요청 실패", "${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    var nicknameCheckCode by mutableStateOf(200)

    fun onNicknameChange(nickname: String) {
        _tempProfile.value.nickname = nickname
        requestNicknameDuplicated(nickname)  // 닉네임 중복 체크
    }

    fun onImageChange(url: String) {
        _tempProfile.value.profileImage = url
    }

    private fun requestNicknameDuplicated(nickname: String) {
        viewModelScope.launch {
            try {
                val nicknameRequest = UserApi.NickName(nickname)
                val response = userApi.requestNicknameDuplicated(nicknameRequest)
                if (response.isSuccessful) {
                    nicknameCheckCode = response.code()
                } else {
                    nicknameCheckCode = response.code()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ApiFailed", "Failed to check nickname: ${e.message}")
            }
        }
    }
}