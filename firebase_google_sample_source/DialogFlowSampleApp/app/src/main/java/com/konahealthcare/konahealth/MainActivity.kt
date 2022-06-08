package com.konahealthcare.konahealth

import android.util.Log
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import com.google.common.collect.Lists
import com.konahealthcare.konahealth.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    //dialogFlow
    private var sessionsClient: SessionsClient? = null
    private var sessionName: SessionName? = null
    private val uuid = UUID.randomUUID().toString()
//    private val uuid = "1234"

    override fun initView() {
        Log.d("TAG", "initView: 이곳은 Main")

        binding.clickIntent.setOnClickListener {
            val message = binding.inputIntent.text.toString()
            if (message.isNotEmpty()) {
                binding.inputIntent.setText("")
                sendMessageToBot(message)
            } else {
                toast("보낼 내용을 입력해주세요.")
            }
        }

        setUpBot() //dialogflowAgent key정보 Setup
    }

    //credential(GoogleService 자격 증명서) 파일을 통해 session 설정
    private fun setUpBot() {
        try {
            // 자격증명파일(credential)을 지정하여 읽기
            val stream: InputStream = this.resources.openRawResource(R.raw.credential)
            val credentials: GoogleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"))
            val projectId: String = (credentials as ServiceAccountCredentials).projectId
            val settingsBuilder = SessionsSettings.newBuilder()
            val sessionsSettings = settingsBuilder.setCredentialsProvider(
                FixedCredentialsProvider.create(credentials)
            ).build()

            sessionsClient = SessionsClient.create(sessionsSettings)    // sessionsClient = com.google.cloud.dialogflow.v2.SessionsClient@a5b9a2d
            sessionName = SessionName.of(projectId, uuid)               // sessionName = projects/k--qgot/agent/sessions/{uuid}

            Log.d("TAG", "projectId : $projectId")             // projectId = k--qgot (test)
        } catch (e: Exception) {
            Log.d("TAG", "setUpBot: " + e.message)
        }
    }

    private fun sendMessageToBot(message: String) {
        val input = QueryInput.newBuilder()
            .setText(TextInput.newBuilder().setText(message).setLanguageCode("ko")).build()
        GlobalScope.launch {
            Log.d("TAG", "sendMessageToBot: sessionName=$sessionName , sessionsClient=$sessionsClient")
            sendMessageInBg(input)
        }
    }

    private suspend fun sendMessageInBg(queryInput: QueryInput) {
        withContext(Default) {
            try {
                //DetectIntentRequest를 통해 request 값을 담아서
                val detectIntentRequest = DetectIntentRequest.newBuilder()
                    .setSession(sessionName.toString())
                    .setQueryInput(queryInput)
                    .build()
                //sessionsClient의 detectIntent 메서드에 담아 호출 -> DetectIntentResponse 객체를 반환함.
                val result: DetectIntentResponse? = sessionsClient?.detectIntent(detectIntentRequest)
                if (result != null) {
                    runOnUiThread {
                        //dialogflowAgent와 통신 성공한 경우
                        updateUI(result)
                    }
                }
            } catch (e: java.lang.Exception) {
                Log.d("TAG", "doInBackground: " + e.message)
                e.printStackTrace()
            }
        }
    }

    private fun updateUI(response: DetectIntentResponse) {
        Log.d("TAG", "updateUI")
        val botReply: String = response.queryResult.fulfillmentText
        val request_intent = response.queryResult.intent.displayName
        val locale = response.queryResult.languageCode
        if (botReply.isNotEmpty()) {
            binding.outputIntent.text = botReply
        } else {
            toast("something went wrong")
        }
    }
}