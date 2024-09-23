package com.echoist.linkedout.data.repository

import android.util.Log
import com.echoist.linkedout.data.api.SupportApi
import com.echoist.linkedout.data.api.UserApi
import com.echoist.linkedout.data.dto.NotificationResponse
import com.echoist.linkedout.data.dto.UserGraphSummaryResponse
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.presentation.myLog.Token
import retrofit2.Response
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val supportApi: SupportApi,
    private val userApi: UserApi
) {

    suspend fun requestUserNotification(
        onSuccess: suspend (NotificationResponse) -> Unit,
        finally: () -> Unit
    ) {
        apiCall(onSuccess = onSuccess, finally = finally)
        { supportApi.readUserNotification() }
    }

    suspend fun requestFirstUserToExistUser() {
        val isNotFirst = UserInfo(isFirst = false)
        apiCall(
            onSuccess = {
                Log.d("첫유저 ->기존유저", "성공")
            },
            onError = { e ->
                Log.e("첫유저 ->기존유저", "실패 ${e.message}")
            }
        ) { userApi.userUpdate(isNotFirst) }
    }

    suspend fun requestUserGraphSummary(onSuccess: suspend (UserGraphSummaryResponse) -> Unit) {
        apiCall(
            onSuccess = onSuccess,
            onError = { e ->
                Log.e("유저 주간 링크드아웃 지수:", "${e.message}")
            }
        ) { userApi.requestUserGraphSummary() }
    }

}

suspend fun <T> apiCall(
    onSuccess: suspend (T) -> Unit = {},
    onError: (Exception) -> Unit = { it.printStackTrace() },
    finally: () -> Unit = {},
    apiCall: suspend () -> Response<T>
) {
    try {
        var response = apiCall()

        if (!response.isSuccessful && response.code() == 401) { //에러코드가 401인경우
            if (response.headers()["x-access-token"].isNullOrEmpty()) { //x-access-token이 안들어오는경우 재로그인필요.
            } else {
                Token.accessToken = response.headers()["x-access-token"]!!
                // 갱신된 토큰으로 다시 API 호출
                response = apiCall()
            }
        }

        if (response.isSuccessful) {
            response.body()?.let {
                onSuccess(it)
                Log.d("api success", "${response.code()}")
            }
        } else {
            onError(Exception("API Error: ${response.code()}"))
        }

    } catch (e: Exception) {
        Log.e("api error", "${e.printStackTrace()}")
        onError(e)
    } finally {
        finally()
    }
}



