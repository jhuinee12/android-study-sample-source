package com.example.ss210922_webview_webserver

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.ss210922_webview_webserver.databinding.ActivityWeb1Binding

class KotlinWeb1Activity : AppCompatActivity() {
    private var web1Binding: ActivityWeb1Binding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web1Binding = ActivityWeb1Binding.inflate(layoutInflater)
        setContentView(web1Binding!!.root)

        // 웹뷰 안에서 자바스크립스 실행 허용
        web1Binding!!.webview1.settings.javaScriptEnabled = true
        // 현재 화면(액티비티)에 출력되도록 설정 :: 이 코드가 없으면 웹브라우저 호출됨
        web1Binding!!.webview1.webViewClient = WebViewClient()
        // 웹뷰에서 해당 url 로드
        web1Binding!!.webview1.loadUrl("http://m.nate.com")

        // 일반적으로 로드할 시 오류 발생
        // net::ERR_CLEARTEXT_NOT_PERMITTED
        // 설정 1. AndroidMenifest 인터넷 접속 권한 추가
        // 설정 2. https 접속 가능 / http 접속 불가능 -> usesCleartextTraffic = "true" :: http 허용
    }

    override fun onDestroy() {
        web1Binding = null
        super.onDestroy()
    }
}