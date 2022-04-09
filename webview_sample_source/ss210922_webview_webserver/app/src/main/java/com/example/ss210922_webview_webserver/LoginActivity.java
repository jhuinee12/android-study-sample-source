package com.example.ss210922_webview_webserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.example.ss210922_webview_webserver.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        // 자바스크립트 실행 가능하도록 설정
        loginBinding.webview1.getSettings().setJavaScriptEnabled(true);
        // 웹서버 :: 로그인 페이지 연결
        loginBinding.webview1.loadUrl("");
        // 웹뷰와 통신할 수 있는 인터페이스 클래스 설정
        // 안드로이드 -> 웹뷰 -> 서버
        // addJavascriptInterface(브릿지클래스, "별칭(브릿지클래스이름)")
        loginBinding.webview1.addJavascriptInterface(new AndroidBridge(), "android");

        // 로그인 버튼 클릭 이벤트
        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = loginBinding.edtId.getText().toString();
                String pwd = loginBinding.edtPwd.getText().toString();
                loginBinding.webview1.loadUrl("javascript:login('"+id+"' ,'"+pwd+"')");
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
                    loginBinding.txtResult.setText(arg.trim());
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        loginBinding = null;
        super.onDestroy();
    }
}