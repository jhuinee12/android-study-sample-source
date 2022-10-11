package com.zahi.jsoupscrapingsample

data class LotteryItem(
    val rank : String,      // 순위
    val sumPrize : String,  // 총 당첨금액
    val people : String,    // 당첨된 사람 수
    val prize : String      // 1인당 당첨금액
)
