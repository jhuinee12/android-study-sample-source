package com.example.ss210922_webview_webserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.example.ss210922_webview_webserver.databinding.ActivityWeb4Binding;

public class Web4Activity extends AppCompatActivity {

    private ActivityWeb4Binding web4Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web4Binding = ActivityWeb4Binding.inflate(getLayoutInflater());
        setContentView(web4Binding.getRoot());

        // 웹뷰에서 자바스크립트 실행
        web4Binding.webview1.getSettings().setJavaScriptEnabled(true);
        // 웹뷰에 웹페이지 로딩
        web4Binding.webview1.loadUrl("file:///android_asset/html/test.html");
        // 웹뷰와 통신할 수 있는 인터페이스 클래스 설정
        // addJavascriptInterface(브릿지클래스, "별칭(브릿지클래스이름)")
        web4Binding.webview1.addJavascriptInterface(new AndroidBridge(), "android");

        // 전송 버튼 클릭 이벤트
        web4Binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 자바스크립트 함수 호출
                web4Binding.webview1.loadUrl("javascript:setMessage('"+web4Binding.edtInput.getText()+"')");
            }
        });
    }

    // 핸들러 선언 (Alt+Enter, import class, android.os 선택)
    // 안드로이드에서는 메인스레드 외의 다른 스레드가 메인 화면을 변경할 수 없음
    // main thread : 메인화면에서 실행되는 thread
    // background thread : 백그라운드에서 실행되는 thread (웹-앱 통신)
    // single thread : 1개의 작업단위
    // multiple thread : 2개 이상의 작업단위 (안드로이드)
    Handler handler = new Handler();
    // 내부클래스
    class AndroidBridge {
        @JavascriptInterface    // 자바스크립트에서 호출할 수 있는 method
        public void setMessage(final String arg) {  // 반드시 final로 선언
            // 핸들러에게 메시지 전달
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // 핸들러가 메시지를 받아서 메인화면을 변경시킴
                    web4Binding.txtReceive.setText("웹에서 받은 값 : " + arg);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        web4Binding = null;
        super.onDestroy();
    }
}