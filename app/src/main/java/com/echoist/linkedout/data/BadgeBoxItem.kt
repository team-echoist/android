package com.echoist.linkedout.data


import com.echoist.linkedout.R

data class BadgeBoxItem(
    val badgeResourceId: Int,
    val badgeName: String,
    val badgeEmotion: String,
    val level: Int,
    val exp: Int
)

data class BadgeBoxItemWithTag(
    val badgeResourceId: Int,
    val badgeName: String,
    val badgeEmotion: String,
    val badgeId : Int? = null,
    val level: Int,
    val exp: Int,
    val tags : List<String>
)

fun Badge.toBadgeBoxItem(): BadgeBoxItem {
    val (badgeResourceId, badgeName, badgeEmotion) = when (name) {
        "angry" -> Triple(R.drawable.badge_anger, "Lv${level} 분노할 용기", "싫은")
        "sad" -> Triple(R.drawable.badge_sad, "Lv${level} 슬퍼할 용기", "슬픈")
        "complicated" -> Triple(R.drawable.badge_complicated, "Lv${level} 복잡할 용기", "복잡한")
        "surprised" -> Triple(R.drawable.badge_surprised, "Lv${level} 놀랄 용기", "예상하지 못한")
        "loving" -> Triple(R.drawable.badge_love, "Lv${level} 사랑할 용기", "좋은")
        else -> Triple(R.drawable.badge_sad, "Unknown", "Unknown")
    }
    return BadgeBoxItem(badgeResourceId, badgeName, badgeEmotion, level, exp)
}

fun BadgeWithTag.toBadgeBoxItem(): BadgeBoxItemWithTag {
    val (badgeResourceId, badgeName, badgeEmotion) = when (name) {
        "angry" -> Triple(R.drawable.badge_anger, "Lv${level} 분노할 용기", "싫은")
        "sad" -> Triple(R.drawable.badge_sad, "Lv${level} 슬퍼할 용기", "슬픈")
        "complicated" -> Triple(R.drawable.badge_complicated, "Lv${level} 복잡할 용기", "복잡한")
        "surprised" -> Triple(R.drawable.badge_surprised, "Lv${level} 놀랄 용기", "예상하지 못한")
        "loving" -> Triple(R.drawable.badge_love, "Lv${level} 사랑할 용기", "좋은")
        else -> Triple(R.drawable.badge_sad, "Unknown", "Unknown")
    }
    return BadgeBoxItemWithTag(badgeResourceId, badgeName, badgeEmotion,id, level, exp, tags)
}

