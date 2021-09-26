package com.example.ss210922_webview_webserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Web2Activity extends AppCompatActivity {
    // Web1Activity 레이아웃 그대로 사용
    private com.example.ss210922_webview_webserver.databinding.ActivityWeb1Binding web1Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web1Binding = com.example.ss210922_webview_webserver.databinding.ActivityWeb1Binding.inflate(getLayoutInflater());
        setContentView(web1Binding.getRoot());

        // html code를 웹뷰에 출력시킴
//        String html = "<html><body>안녕하세요</body></html>";
        // @param1 html :: html 코드 사용
        // @param2 mimeType :: 타입 종류 - html
//        web1Binding.webview1.loadData(html, "text/html; charset=utf-8", null);

        // 폴더 생성 : app -> folder -> assets folder -> html directory
        // test.html in assets folder
        web1Binding.webview1.loadUrl("file:///android_asset/html/hello.html");
    }

    @Override
    protected void onDestroy() {
        web1Binding = null;
        super.onDestroy();
    }
}