package com.example.ss210922_webview_webserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ss210922_webview_webserver.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            // url을 이용한 웹뷰 출력
            case R.id.btn_webview:
                intent = new Intent(this, Web1Activity.class);
                break;
            case R.id.btn_webview_kotlin:
                intent = new Intent(this, KotlinWeb1Activity.class);
                break;
            // html 코드를 이용해 직접 웹뷰 생성 후 출력
            case R.id.btn_webview_local:
                intent = new Intent(this, Web2Activity.class);
                break;
            // loadUrl 후 앞뒤 페이지 이동, 웹사이트 직접 호출
            case R.id.btn_webview_nb:
                intent = new Intent(this, Web3Activity.class);
                break;
            // 웹뷰 통신
            case R.id.btn_webview_server:
                intent = new Intent(this, Web4Activity.class);
                break;
            // 로그인 액티비티
            case R.id.btn_login:
                intent = new Intent(this, LoginActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        mBinding = null;
        super.onDestroy();
    }
}