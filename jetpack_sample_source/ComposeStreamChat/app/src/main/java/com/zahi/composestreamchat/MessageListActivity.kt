package com.zahi.composestreamchat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

class MessageListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 1 - intent에서 채널 아이디 꺼내기
        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)

        if (channelId == null) {
            finish()
            return
        }

        // Step 2 - 메세지 목록 화면 구현
        setContent {
            ChatTheme {
                MessagesScreen(
                    channelId = channelId,
                    messageLimit = 30,
                    onBackPressed = { finish() }
                )
            }
        }
    }

    // Step 3 - 채널 ID와 함께 MessageListActivity를 실행하는 intent 생성
    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, MessageListActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}