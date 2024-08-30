package com.echoist.linkedout

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.echoist.linkedout.api.BookMarkApi
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.SignUpApi
import com.echoist.linkedout.api.SocialSignUpApi
import com.echoist.linkedout.api.StoryApi
import com.echoist.linkedout.api.SupportApi
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.page.myLog.Token
import com.echoist.linkedout.room.EssayStorageDB
import com.echoist.linkedout.room.EssayStoreDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@HiltAndroidApp
class App : Application() {
    // 애플리케이션 클래스 안에서 모듈을 정의하고 설치합니다.
}
class ErrorHandlingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            // 여기서 response.code()나 response.body() 등을 활용해 에러 핸들링 로직을 작성합니다.
            when (response.code) {
                401 -> {
                    // 인증 오류 처리 (예: 토큰 만료 시 재로그인 유도)
                    Log.e("intercept err", "intercept: ${response.code}")
                    if (response.headers["x-access-token"].isNullOrEmpty()) { //x-access-token이 안들어오는경우 재로그인필요.
                        AuthManager.isReAuthenticationRequired.value = true
                    } else {
                        Log.e("intercept err", "intercept: ${response.headers["x-access-token"]!!}")
                        Token.accessToken = response.headers["x-access-token"]!!
                    }
                }
                500 -> {
                    // 서버 오류 처리
                    Log.e("intercept err", "intercept: ${response.code}")
                }
                else -> {
                    // 기타 에러 처리
                    Log.e("intercept err", "intercept: ${response.code}")
                }
            }
        }
        return response
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit{
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // BODY는 요청과 응답의 모든 정보를 로그로 남깁니다.
//
        // OkHttpClient에 인터셉터를 추가
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(ErrorHandlingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) //로그 확인가능한 클라이언트 추가
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): EssayApi {
        return retrofit.create(EssayApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSignUpApiClient(retrofit: Retrofit) : SignUpApi {
        return retrofit.create(SignUpApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleSignUpApiClient(retrofit: Retrofit) : SocialSignUpApi {
        return retrofit.create(SocialSignUpApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiClient(retrofit: Retrofit) : UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStoryApiClient(retrofit: Retrofit) : StoryApi {
        return retrofit.create(StoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookMarkApiClient(retrofit: Retrofit) : BookMarkApi {
        return retrofit.create(BookMarkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSupportApiClient(retrofit: Retrofit) : SupportApi {
        return retrofit.create(SupportApi::class.java)
    }
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): EssayStorageDB {
        return Room.databaseBuilder(
            context,
            EssayStorageDB::class.java,
            "todo-database"
        ).build()
    }

    @Provides
    fun provideEssayStoreDao(database: EssayStorageDB): EssayStoreDao {
        return database.essayStoreDao()
    }

}



