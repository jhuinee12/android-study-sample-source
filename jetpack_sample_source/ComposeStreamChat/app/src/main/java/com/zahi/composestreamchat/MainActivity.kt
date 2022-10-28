package com.zahi.composestreamchat

import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.res.stringResource
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 1 - 오프라인 메시지 로드 및 전송 등 오프라인 기능 초기화
        val offlinePluginFactory = StreamOfflinePluginFactory(
            config = io.getstream.chat.android.offline.plugin.configuration.Config(),
            appContext = applicationContext
        )

        // Step 2 - ChatClient 초기화
        val client = ChatClient.Builder("kghe5dxg9dan", applicationContext)
            .withPlugin(offlinePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .build()

        // Step 3 - 유저 정보 초기화
        val user = User(
            id = "marvel",
            name = "Iron Man",
            image = "https://bit.ly/2TIt8NR"
        )

        val token = client.devToken(user.id) // developer 토큰 생성

        client.connectUser( // 유저 로그인
            user = user,
            token = token
        ).enqueue {
            // Step 4 - 새로운 그룹 (채널) 생성
            if (it.isSuccess) {
                client.createChannel(
                    channelType = "messaging",
                    channelId = "new_channel_01",
                    memberIds = listOf(user.id),
                    extraData = mapOf("name" to "My New Channel")
                ).enqueue()
            }
        }

        // Step 5 - 채널 목록 UI 구현
        setContent {
            ChatTheme {
                ChannelsScreen( // 채널 리스트 화면
                    title = stringResource(id = R.string.app_name),
                    onItemClick = { channel ->
                        startActivity(MessageListActivity.getIntent(this, channel.cid))
                    },
                    onBackPressed = { finish() }
                )
            }
        }
    }
}