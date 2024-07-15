package com.echoist.linkedout

import android.app.Application
import android.content.Context
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
    fun provideRetrofit() : Retrofit{
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
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



