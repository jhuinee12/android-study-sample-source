package com.example.ss210922_webview_webserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;

import com.example.ss210922_webview_webserver.databinding.ActivityWeb1Binding;

public class Web1Activity extends AppCompatActivity {
    private ActivityWeb1Binding web1Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web1Binding = ActivityWeb1Binding.inflate(getLayoutInflater());
        setContentView(web1Binding.getRoot());

        // 웹뷰 안에서 자바스크립스 실행 허용
        web1Binding.webview1.getSettings().setJavaScriptEnabled(true);
        // 현재 화면(액티비티)에 출력되도록 설정 :: 이 코드가 없으면 웹브라우저 호출됨
        web1Binding.webview1.setWebViewClient(new WebViewClient());
        // 웹뷰에서 해당 url 로드
        web1Binding.webview1.loadUrl("http://m.nate.com");

        // 일반적으로 로드할 시 오류 발생
        // net::ERR_CLEARTEXT_NOT_PERMITTED
        // 설정 1. AndroidMenifest 인터넷 접속 권한 추가
        // 설정 2. https 접속 가능 / http 접속 불가능 -> usesCleartextTraffic = "true" :: http 허용
    }

    @Override
    protected void onDestroy() {
        web1Binding = null;
        super.onDestroy();
    }
}