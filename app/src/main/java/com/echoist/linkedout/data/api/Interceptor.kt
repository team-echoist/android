package com.echoist.linkedout.data.api

import android.util.Log
import com.echoist.linkedout.data.repository.TokenRepository
import com.echoist.linkedout.presentation.myLog.Token
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class ErrorHandlingInterceptor @Inject constructor(private val tokenRepository: TokenRepository): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        // 에러코드 401 (Unauthorized) 처리
        if (response.code == 401) {
            // x-access-token이 없는 경우, 재로그인 필요
            if (response.headers["x-access-token"].isNullOrEmpty()) {
                tokenRepository.setReAuthenticationRequired(true)

            } else { //x-access-token이 있는경우
                // 새로운 토큰 갱신
                val newAccessToken = response.headers["x-access-token"]!!
                // 토큰 저장
                Token.accessToken = newAccessToken

                // 새로운 토큰으로 기존 요청을 다시 시도
                val newRequest = request.newBuilder()
                    .header("x-refresh-token", Token.refreshToken)
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()

                response = chain.proceed(newRequest) // 새로운 요청으로 다시 시도
            }
        }
        // 기타 에러 처리
        when (response.code) {
            200,201,202 -> Log.d("response 성공","${response.code}")
            500 -> { Log.e("intercept err", "Server error: ${response.code}") }
            else -> { Log.e("intercept err", "Unexpected error: ${response.code}") }
        }
        return response
    }
}

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Authorization과 x-refresh-token 헤더 추가
        val requestWithHeaders = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${Token.accessToken}")
            .header("x-refresh-token", Token.refreshToken)
            .build()

        return chain.proceed(requestWithHeaders)
    }
}
