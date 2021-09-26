package com.example.ss210812_webviewToPHP

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var mContext: Context
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = applicationContext

        /**
         * WebView에서 Javascript의 alert 함수로 메세지 띄우기
         */
        // 웹뷰는 기본적으로 alert(), conform() 같은 별도의 모달 다이얼로그를 제한함
        // 사용하려면 아래와 같은 코드가 필요
        web_view.webChromeClient = WebChromeClient()

        // Webview Setting
        // 기본적으로 WebView는 JavaScript를 실행하지 않도록 설정되어 있음.
        // WebSettings를 이용하여 Javascript를 실행하도록 설정을 변경해줌
        // 어차피 나는 아직 php를 사용하여 테스트할 예정임
        var webSettings: WebSettings = web_view.settings // java: web_view.getSettings()
        // 클릭을 위해선 javaScriptEnabled도 true로 변경해줘야함
        webSettings.javaScriptEnabled = true
        // 로컬 저장 허용 :: 클릭 허용
        webSettings.domStorageEnabled = true

        // 새로운 웹브라우저 앱을 실행하여 그곳에 보여줌
        web_view.setWebViewClient(WebViewClientClass())

        // 웹 뷰에서 보여주는 자바 스크립트와 연결할 객체 추가
        // 두번째 파라미터 : js에서 이 객체를 식별할 이름
        // WebViewConnector()를 통해 javaScript 내 함수 호출 가능
        web_view.addJavascriptInterface(WebViewConnector(), "Droid")

        // dothome url로 이동
        web_view.loadUrl("http://anovicedp.dothome.co.kr/")
        
    }   // onCreate method

    override fun onBackPressed() {
        // webview history에 이전 웹 문서 기록이 있는지 여부 확인
        if (web_view.canGoBack()) web_view.goBack()
        else super.onBackPressed()
    }   // onBackPressed method : 뒤로가 버튼 -> 웹뷰의 웹 페이지를 이전으로

    class WebViewClientClass: WebViewClient() {
        // shouldOverrideUrlLoading: WebView 내 특별한 동작은 컨트롤
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            lateinit var intent: Intent
            var url: String = request?.url.toString()   // 현재 url 가져오기

            if (url != null && url.startsWith("intent")) {
                try {
                    // 안드로이드에서 webview 구현 시 다른 앱으로 호출해야 하는 경우가 있음
                    // WebViewClient()를 상속 받지 않은경우 기본 브라우저가 동작하여 URI_INTENT_SCHEME 오류가 발생하지 않음
                    // 웹뷰내에서 WebViewClient()를 개발자으 수작업으로 구현하여 상속 받은 경우에는 shouldOverrideUrlLoading()에서 url을 먼저 수신에서 다른 처리를 해줘야함
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)

                    // Task : Stack 구조로 되어 있으며, 어플리케이션에서 실행되는 Activity를 보관하고 관리하는 역할
                    // addFlags() : 새로운 Flag를 기존 Flag에 붙임 || setFlags() : 오래된 Flag 전체를 대체
                    // FLAG_ACTIVITY_NEW_TASK : 새로운 Task를 생성하여 그 Task 안에 Activity를 추가할 때 사용
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    var strParams: String? = intent.dataString
                    var intent2: Intent = Intent(Intent.ACTION_VIEW)
                    intent2.setData(Uri.parse(strParams))
                    startActivity(mContext, intent2, null)
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                    var marketIntent: Intent = Intent(Intent.ACTION_VIEW)
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()))
                    marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(mContext, marketIntent, null)
                }
            } else if (url != null && url.startsWith("market://")) {
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    if (intent != null) startActivity(mContext, intent, null)
                    return true
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }
            }

            view?.loadUrl(url)
            return false
        }
    }

    // inner class
    // JavaScript 와 통신을 담당할 연결자 클래스 정의
    inner class WebViewConnector {
        // 웹뷰와 통신하는 메서드
        @JavascriptInterface
        @Throws(JSONException::class)
        // php 문서 안 javaScript에서 생성한 함수
        fun getSuccessDataMethod(jsonResult: String?) {
            val jsonObject = JSONObject(jsonResult)
            val builder = AlertDialog.Builder(mContext)
                    .setTitle("결제 성공")
                    .setMessage(jsonObject.getString("orderId"))
                    .create()
            builder.show()
        }
    }
}