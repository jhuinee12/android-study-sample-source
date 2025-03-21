package com.zahi.mlkit

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

@Composable
fun MLKitScreen() {
    var inputText by rememberSaveable { mutableStateOf("") }
    var detectedLang by remember { mutableStateOf("und") }
    var translationResults by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val targetLanguages = listOf("ko", "en", "zh", "ja")

    LaunchedEffect(Unit) {
        val models = listOf(
            TranslateLanguage.KOREAN,
            TranslateLanguage.ENGLISH,
            TranslateLanguage.CHINESE,
            TranslateLanguage.JAPANESE
        )

        models.forEach { lang ->
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH) // 임의
                .setTargetLanguage(lang)
                .build()
            val translator = Translation.getClient(options)
            translator.downloadModelIfNeeded()
                .addOnSuccessListener {
                    Log.d("MLKit", "[$lang] 모델 다운로드 완료")
                }
                .addOnFailureListener {
                    Log.e("MLKit", "[$lang] 모델 다운로드 실패", it)
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(16.dp)
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            placeholder = { Text("10줄 입력 가능") },
            maxLines = 10
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (inputText.isBlank()) return@Button

                translationResults = emptyMap()

                val langClient = LanguageIdentification.getClient()
                langClient.identifyLanguage(inputText)
                    .addOnSuccessListener { languageCode ->
                        detectedLang = languageCode
                        val tempResults = mutableMapOf<String, String>()

                        val sourceLang = TranslateLanguage.fromLanguageTag(languageCode)
                            ?: TranslateLanguage.ENGLISH

                        targetLanguages.forEach { target ->
                            val targetLang = when (target) {
                                "ko" -> TranslateLanguage.KOREAN
                                "en" -> TranslateLanguage.ENGLISH
                                "zh" -> TranslateLanguage.CHINESE
                                "ja" -> TranslateLanguage.JAPANESE
                                else -> TranslateLanguage.ENGLISH
                            }

                            val options = TranslatorOptions.Builder()
                                .setSourceLanguage(sourceLang)
                                .setTargetLanguage(targetLang)
                                .build()

                            val translator = Translation.getClient(options)
                            val startTime = System.currentTimeMillis()

                            translator.downloadModelIfNeeded()
                                .addOnSuccessListener {
                                    translator.translate(inputText)
                                        .addOnSuccessListener { translatedText ->
                                            val duration = System.currentTimeMillis() - startTime
                                            tempResults[target] = "$translatedText\n(소요시간: ${duration}ms)"
                                            translationResults = tempResults.toMap()
                                        }
                                        .addOnFailureListener {
                                            tempResults[target] = "번역 실패 ❌"
                                            translationResults = tempResults.toMap()
                                        }
                                }
                                .addOnFailureListener {
                                    tempResults[target] = "모델 다운로드 실패 ❌"
                                    translationResults = tempResults.toMap()
                                }
                        }
                    }
                    .addOnFailureListener {
                        detectedLang = "und"
                    }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("확인")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (inputText.isNotBlank()) {
            Text("(원어: ${detectedLang.uppercase()})", modifier = Modifier.padding(bottom = 16.dp))

            targetLanguages.forEach { lang ->
                Text("${lang.uppercase()} 번역")
                Text(translationResults[lang] ?: "(로딩 중...)")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun MLKitScreenPreview() {
    MLKitScreen()
}