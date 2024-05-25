package com.echoist.linkedout

import android.app.Application
import com.echoist.linkedout.api.ApiClient
import com.echoist.linkedout.api.SignUpApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class App : Application() {
    // 애플리케이션 클래스 안에서 모듈을 정의하고 설치합니다.
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient {
        return ApiClient()
    }

    @Provides
    @Singleton
    fun provideSignUpApiClient() : SignUpApiClient {
        return SignUpApiClient()
    }
}



