package com.echoist.linkedout

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.echoist.linkedout.api.BookMarkApi
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.api.SignUpApi
import com.echoist.linkedout.api.SocialSignUpApi
import com.echoist.linkedout.api.StoryApi
import com.echoist.linkedout.api.SupportApi
import com.echoist.linkedout.api.UserApi
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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserDataRepository(sharedPreferences: SharedPreferences): UserDataRepository {
        return UserDataRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(): TokenRepository {
        return TokenRepository()
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