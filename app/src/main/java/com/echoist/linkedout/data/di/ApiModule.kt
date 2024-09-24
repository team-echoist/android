package com.echoist.linkedout.data.di

import com.echoist.linkedout.data.api.AuthInterceptor
import com.echoist.linkedout.data.api.BookMarkApi
import com.echoist.linkedout.data.api.ErrorHandlingInterceptor
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.api.SignUpApi
import com.echoist.linkedout.data.api.SocialSignUpApi
import com.echoist.linkedout.data.api.StoryApi
import com.echoist.linkedout.data.api.SupportApi
import com.echoist.linkedout.data.api.UserApi
import com.echoist.linkedout.data.repository.TokenRepository
import com.echoist.linkedout.presentation.util.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideRetrofit(errorHandlingInterceptor: ErrorHandlingInterceptor): Retrofit {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // BODY는 요청과 응답의 모든 정보를 로그로 남깁니다.
//
        // OkHttpClient에 인터셉터를 추가
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor()) //전역 헤더 인터셉터 추가.
            .addInterceptor(errorHandlingInterceptor) // 전역 에러핸들링 인터셉터 추가
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) //로그 확인가능한 클라이언트 추가
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideErrorHandlingInterceptor(tokenRepository: TokenRepository): ErrorHandlingInterceptor {
        return ErrorHandlingInterceptor(tokenRepository)
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): EssayApi {
        return retrofit.create(EssayApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSignUpApiClient(retrofit: Retrofit): SignUpApi {
        return retrofit.create(SignUpApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleSignUpApiClient(retrofit: Retrofit): SocialSignUpApi {
        return retrofit.create(SocialSignUpApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiClient(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStoryApiClient(retrofit: Retrofit): StoryApi {
        return retrofit.create(StoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookMarkApiClient(retrofit: Retrofit): BookMarkApi {
        return retrofit.create(BookMarkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSupportApiClient(retrofit: Retrofit): SupportApi {
        return retrofit.create(SupportApi::class.java)
    }
}