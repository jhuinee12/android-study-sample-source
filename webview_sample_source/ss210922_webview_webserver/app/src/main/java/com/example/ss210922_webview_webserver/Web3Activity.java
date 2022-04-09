package com.example.ss210922_webview_webserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ss210922_webview_webserver.databinding.ActivityWeb3Binding;

public class Web3Activity extends AppCompatActivity {
    private ActivityWeb3Binding web3Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web3Binding = ActivityWeb3Binding.inflate(getLayoutInflater());
        setContentView(web3Binding.getRoot());

        // WebSettings :: webview에 대한 설정 클래스
        WebSettings set = web3Binding.webview1.getSettings();
        set.setJavaScriptEnabled(true); // 자바스크립스 설정 허용
        set.setBuiltInZoomControls(true);   // 화면 확대/축소 허용
        web3Binding.webview1.loadUrl("http://google.com");  // 초기페이지 : 구글
        web3Binding.webview1.setWebViewClient(new WebViewClient()); // 현재 화면에 웹뷰 보이기
        // 진행률 표시
        web3Binding.webview1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                web3Binding.pb.setProgress(newProgress);
                if (newProgress == 100) {
                    web3Binding.pb.setVisibility(View.GONE);
                } else {
                    web3Binding.pb.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        // 확인 버튼 클릭 이벤트
        web3Binding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = web3Binding.edtUrl.getText().toString();
                if (!url.contains("http://")) {
                    url = "http://" + url;
                }
                web3Binding.webview1.loadUrl(url);
            }
        });
        // 취소 버튼 클릭 이벤트
        web3Binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web3Binding.edtUrl.setText("");
            }
        });

        // 이전 버튼 클릭 이벤트
        web3Binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (web3Binding.webview1.canGoBack()) { // 뒷 페이지 존재 여부 확인
                    web3Binding.webview1.goBack();  // 웹뷰에서 전 페이지로 돌아가기
                }
            }
        });
        // 다음 버튼 클릭 이벤트
        web3Binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (web3Binding.webview1.canGoForward()) {  // 앞 페이지 존재 여부 확인
                    web3Binding.webview1.goForward();   // 웹뷰에서 앞 페이지 호출하기
                }
            }
        });
    }
}