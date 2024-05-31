package com.echoist.linkedout.data

import com.echoist.linkedout.R

sealed class BadgeBoxItem(
    val badgeResourceId: Int,
    val badgeName: String,
    val badgeEmotion: String
) {


    data object Anger1 :
        BadgeBoxItem(
            R.drawable.badge_anger,
            "Lv1 분노할 용기",
            "싫은" //
        )

    data object Sad1 :
        BadgeBoxItem(
            R.drawable.badge_sad,
            "Lv1 슬퍼할 용기",
            "슬픈" //
        )

    data object Surprised1 :
        BadgeBoxItem(
            R.drawable.badge_surprised,
            "Lv1 놀랄 용기",
            "예상하지 못한" //
        )

    data object Complicated1 :
        BadgeBoxItem(
            R.drawable.badge_complicated,
            "Lv1 복잡할 용기",
            "복잡한" //
        )
    data object Love1 :
        BadgeBoxItem(
            R.drawable.badge_love,
            "Lv1 사랑할 용기",
            "좋은" //
        )

    //todo lv2 도 같은방식으로 만들기.
}
