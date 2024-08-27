package com.echoist.linkedout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.page.myLog.Token
import kotlinx.coroutines.launch
import retrofit2.Response

// NetworkUtils.kt
fun <T> ViewModel.apiCall(
    onSuccess: suspend (T) -> Unit = {},
    onError: (Exception) -> Unit = { it.printStackTrace() },
    finally: () -> Unit = {},
    apiCall: suspend () -> Response<T>
) {
    viewModelScope.launch {
        try {
            var response = apiCall()

            if (!response.isSuccessful && response.code() == 401) { //에러코드가 401인경우
                if (response.headers()["x-access-token"].isNullOrEmpty()) { //x-access-token이 안들어오는경우 재로그인필요.
                    AuthManager.isReAuthenticationRequired.value = true
                } else {
                    Token.accessToken = response.headers()["x-access-token"]!!
                    // 갱신된 토큰으로 다시 API 호출
                    response = apiCall()
                }
            }

            if (response.isSuccessful) {
                response.body()?.let { onSuccess(it)
                    Log.d("api success","${response.code()}")
                }
            } else {
                onError(Exception("API Error: ${response.code()}"))
            }

        } catch (e: Exception) {
            if (e.message == "timeout") AuthManager.isReAuthenticationRequired.value = true
            Log.e("api error", "${e.message}")
            onError(e)
        }
        finally {
            finally()
        }
    }
}

