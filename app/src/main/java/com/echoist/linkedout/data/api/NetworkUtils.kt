package com.echoist.linkedout.data.api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            val response = apiCall()

            if (response.isSuccessful) {
                response.body()?.let { onSuccess(it)
                    Log.d("api success","${response.code()}")
                }
            } else {
                onError(Exception("API Error: ${response.code()}"))
            }

        } catch (e: Exception) {
            Log.e("api error", "${e.message}")
            onError(e)
        }
        finally {
            finally()
        }
    }
}

