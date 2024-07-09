package com.echoist.linkedout.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.data.UserInfo
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

@Database(entities = [EssayApi.EssayItem::class], version = 1)
@TypeConverters(Converters::class) // 타입 컨버터 추가
abstract class EssayStorageDB : RoomDatabase() {

    abstract  fun essayStoreDao() : EssayStoreDao

}

class Converters {

    @TypeConverter
    fun fromTagList(tagList: List<EssayApi.Tag>?): String? {
        return Gson().toJson(tagList)
    }

    @TypeConverter
    fun toTagList(tagListString: String?): List<EssayApi.Tag>? {
        val listType = object : TypeToken<List<EssayApi.Tag>>() {}.type
        return Gson().fromJson(tagListString, listType)
    }

    @TypeConverter
    fun fromUserInfo(userInfo: UserInfo?): String? {
        return Gson().toJson(userInfo)
    }

    @TypeConverter
    fun toUserInfo(userInfoString: String?): UserInfo? {
        return Gson().fromJson(userInfoString, object : TypeToken<UserInfo>() {}.type)
    }
    @TypeConverter
    fun fromStory(story: Story?): String? {
        return Gson().toJson(story)
    }

    @TypeConverter
    fun toStory(storyString: String?): Story? {
        return Gson().fromJson(storyString, object : TypeToken<Story>() {}.type)
    }
}
