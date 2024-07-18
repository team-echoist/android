package com.echoist.linkedout.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.echoist.linkedout.PRIVATE_POPUP_URL
import com.echoist.linkedout.R
import com.echoist.linkedout.api.EssayApi
import java.util.Stack
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExampleItems @Inject constructor(){

    var detailEssayBackStack = Stack<EssayApi.EssayItem>()

    var storageEssay by mutableStateOf(EssayApi.EssayItem("",""))


    private var exampleBadge by mutableStateOf(
        BadgeBoxItem(R.drawable.badge_love,"love","emotion",1,5)
    )
    var simpleBadgeList : SnapshotStateList<BadgeBoxItem> = mutableStateListOf(
        exampleBadge,exampleBadge,exampleBadge,exampleBadge,exampleBadge
    )

    var exampleDetailBadge by mutableStateOf(
        BadgeBoxItemWithTag(0,"","",1, tags = listOf("1","2"), exp = 0, level = 1)
    )
    var detailBadgeList : SnapshotStateList<BadgeBoxItemWithTag> = mutableStateListOf(exampleDetailBadge)

    var exampleStroy by mutableStateOf(
        Story(2,"","20240605",2)
    )

    var storyList : SnapshotStateList<Story> = mutableStateListOf(exampleStroy,exampleStroy,exampleStroy,exampleStroy,exampleStroy)


    /**
     * MyLogPage items
     * 나만의 에세이, 발행된 에세이
     */

    var myEssayList by mutableStateOf(mutableStateListOf<EssayApi.EssayItem>())
    var publishedEssayList by mutableStateOf(mutableStateListOf<EssayApi.EssayItem>())


    var detailEssay by mutableStateOf(
        EssayApi.EssayItem(
            id = 0,
            author = UserInfo(1,"groove"),
            content = "이 에세이는 예시입니다.123123이 에세이는 예시입니다.123123이 에세이는 예시입니다.123123이 에세이는 예시입니다.123123",
            createdDate = "2024년 04월 28일 16:47",
            linkedOutGauge = 5,
            status = "published",
            thumbnail = "http 값 있어요~",
            title = "예시 에세이",
            updatedDate = "2024-05-15",
            tags = listOf(EssayApi.Tag(1,"tag"), EssayApi.Tag(1,"tag"))
        )
    )
    var userItem by mutableStateOf(
        UserInfo(
            id = 1,
            nickname = "구루브",
            profileImage = "http",
            password = "1234",
            gender = "male",
            birthDate = "0725"
        )
    )

    var myProfile by mutableStateOf(
        UserInfo(
            id = 1,
            nickname = "테스트",
            profileImage = PRIVATE_POPUP_URL,
            password = "1234",
            gender = "male",
            birthDate = "0725",
            essayStats = EssayStats(0,0,0)
        )
    )



    var subscribeUserList = mutableStateListOf( //구독유저 리스트 api통신해서 받을것.
        userItem.copy(id = 1, nickname = "꾸르륵"),
        userItem.copy(id = 2, nickname = "카프카"),
        userItem.copy(id = 3, nickname = "스물하나"),
        userItem.copy(id = 4, nickname = "호랑이"),
        userItem.copy(id = 5, nickname = "자두천사"),
        userItem.copy(id = 6, nickname = "무니"),
        userItem.copy(id = 7, nickname = "사자랑이")
    )

    var exampleEmptyEssayList = mutableStateListOf( //구독유저 리스트 api통신해서 받을것.
        detailEssay.copy(id = 1),
        detailEssay.copy(id = 2),
        detailEssay.copy(id = 3),
        detailEssay.copy(id = 4),
        detailEssay.copy(id = 5),
        detailEssay.copy(id = 6),
        detailEssay.copy(id = 7)
    )

    var randomList : SnapshotStateList<EssayApi.EssayItem> = mutableStateListOf(detailEssay,detailEssay,detailEssay,detailEssay)
    var followingList : SnapshotStateList<EssayApi.EssayItem> = mutableStateListOf(detailEssay)
    var firstSentences : SnapshotStateList<EssayApi.EssayItem> = mutableStateListOf(detailEssay,detailEssay,detailEssay,detailEssay,detailEssay)
    var lastSentences : SnapshotStateList<EssayApi.EssayItem> = mutableStateListOf(detailEssay,detailEssay,detailEssay,detailEssay,detailEssay)
    var previousEssayList: SnapshotStateList<EssayApi.EssayItem> =  mutableStateListOf(detailEssay,detailEssay,detailEssay,detailEssay,detailEssay)
    var recentViewedEssayList: SnapshotStateList<EssayApi.EssayItem> =  mutableStateListOf(EssayApi.EssayItem(title = "최근 본 글이 없습니다.", content = "empty"))
    var storageEssayList : SnapshotStateList<EssayApi.EssayItem> = mutableStateListOf(detailEssay,detailEssay,detailEssay,detailEssay)



}