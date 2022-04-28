package com.example.tosspayments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv = findViewById(R.id.web_view);
        WebSettings ws = wv.getSettings();

        // 새로운 웹브라우저 앱을 실행하여 그 곳에 보여줌
        wv.setWebViewClient( new WebViewClientClass() );

        // 웹뷰는 기본적으로 alert(), conform() 같은 별도의 모달 다이얼로그를 제한 합니다. 사용하게 하려면
        wv.setWebChromeClient( new WebChromeClient() );

        // 웹뷰는 기본적으로 js 사용을 제한함
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);

        ws.setDomStorageEnabled(true);

        // dothome url로 이동
        wv.loadUrl("http://anovicedp.dothome.co.kr/");

    } // onCreate method

    // 디바이스의 뒤로가기 버튼을 클릭했을때
    // 웹뷰의 웹 파이지를 이전으로 가도록..
    @Override
    public void onBackPressed() {
        // 웹뷰의 history의 이전 웹 문서 기록이 있는가?
        if(wv.canGoBack()) wv.goBack();
        else super.onBackPressed();
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            Intent intent = null;
            String url = request.getUrl().toString();

            if (url != null && url.startsWith("intent")) {
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    String strParams= intent.getDataString();
                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                    intent2.setData(Uri.parse(strParams));
                    startActivity(intent2);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                    marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(marketIntent);
                }
            } else if (url != null && url.startsWith("market://")) {
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    if (intent != null) {
                        startActivity(intent);
                    }
                    return true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            view.loadUrl(url);
            return false;
        }
    }
}
// 수정한거 푸시 가능한가요?