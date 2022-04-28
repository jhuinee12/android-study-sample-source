package com.testlogin.snslogin.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.util.Hex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.link.LinkClient;
import com.kakao.sdk.template.model.*;
import com.kakao.sdk.user.UserApiClient;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.testlogin.snslogin.GlobalVar;
import com.testlogin.snslogin.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import static com.android.billingclient.api.BillingClient.SkuType.INAPP;
import static com.testlogin.snslogin.GlobalVar.*;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;  // 파이어 베이스 인증 객체
    private GoogleSignInClient googleSignInClient;
    private final int REQ_SIGN_GOOGLE = 9001; // 구글 로그인 결과 코드

    // Billing Client : Google Play 결제 라이브러리와 나머지 앱 간의 통신을 위한 기본 인터페이스
    private PurchasesUpdatedListener purchasesUpdatedListener;
    private BillingClient billingClient;
    SkuDetails skuDetailsBuy;
    String skuID = "buy_test";
    String sku;
    BillingResult billingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Kakao Login","MainActivity.java - onCreate()");
        mContext = getApplicationContext();

        //<editor-fold desc="item 연결">
        // sns 로그인
        liBtnLogin = (LinearLayout)findViewById(R.id.li_btn_login);
        liBtnLogout = (LinearLayout)findViewById(R.id.li_btn_logout);
        txtLoginState = (TextView)findViewById(R.id.txt_login_state);
        btnNaverLogin = (OAuthLoginButton)findViewById(R.id.btn_naver_login);
        btnKakaoLogin = (ImageView)findViewById(R.id.btn_kakao_login);
        btnGoogleLogin = findViewById(R.id.btn_google_login);
        btnFaceBookLogin = (com.facebook.login.widget.LoginButton)findViewById(R.id.btn_facebook_login);
        btnLogout = (Button)findViewById(R.id.btn_logout);

        // 카카오링크
        btnKakaoMessage = (Button)findViewById(R.id.btn_kakao_message);

        // 솔라피
        liNumeric = (LinearLayout)findViewById(R.id.li_numeric);
        btnSolapi = (Button)findViewById(R.id.btn_solapi);
        btnKeyEnter = (Button)findViewById(R.id.btn_key_enter);
        edtMessageKey = (EditText) findViewById(R.id.edt_messageKey);
        txtTime = (TextView)findViewById(R.id.txt_time);

        // 인앱 결제
        liGooglePay = findViewById(R.id.li_google_pay);
        btnGooglePay = findViewById(R.id.btn_google_pay);

        // 게시판
        btnIntentBoard = findViewById(R.id.btn_intent_board);
        //</editor-fold>

        // 인증번호 레이아웃 숨기기 : 솔라피 문자 전송 후 보여줄 예정 (핸들러 호출)
        handlerMsg  = KeyLiINVisible.obtainMessage();
        KeyLiINVisible.sendMessage(handlerMsg);
        // 인앱결제&게시판이동 레이아웃 숨기기 : 인증 완료 후 보여줄 예정 (핸들러 호출)
        handlerMsg = PayLiINVisible.obtainMessage();
        PayLiINVisible.sendMessage(handlerMsg);

//<editor-fold desc="SNS 로그인 기능 구현">
        //<editor-fold desc="초기 설정">
        // 로그인이 성공되면 login_state에 로그인 한 sns의 첫 스펠링을 저장함
        // 로그인을 하지 않았을 경우, default값은 logout임
        if(login_state == "logout") {
            txtLoginState.setText("로그인을 하지 않았습니다. : " + login_state);
            new Thread()
            {
                public void run()
                {
                    // 로그인을 하지 않았을 경우, sns 로그인 버튼 보여주기 (핸들러 호출)
                    handlerMsg  = btnVisibleHandler.obtainMessage();
                    btnVisibleHandler.sendMessage(handlerMsg);
                }
            }.start();
        } else {
            new Thread()
            {
                public void run()
                {
                    if (login_state == "n")
                        txtLoginState.setText("네이버 : " + login_name);
                    else if (login_state == "k")
                        txtLoginState.setText("카카오톡 : " + login_name);
                    else if (login_state == "g")
                        txtLoginState.setText("구글 : " + login_name);
                    else if (login_state == "f")
                        txtLoginState.setText("페이스북 : " + login_name);
                    else
                        txtLoginState.setText("login_state : " + login_state);

                    // 로그인이 완료되면 로그아웃 외 버튼 호출하기
                    handlerMsg = btnInVisibleHandler.obtainMessage();
                    btnInVisibleHandler.sendMessage(handlerMsg);
                }
            }.start();
        }
        //</editor-fold>

        //<editor-fold desc="네이버 로그인 객체">
        // 네이버 로그인 객체 초기화
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(mContext ,naver_client_id ,naver_client_secret ,naver_client_name);
        btnNaverLogin.setOAuthLoginHandler(mOAuthLoginHandler); // 네이버 핸들러 연결
        //</editor-fold desc>

        //<editor-fold desc="카카오 로그인 실행">
        //getAppKeyHash(); // 카카오 키 해시 받기
        /*// kakao login sdk ver1
        //Session.getCurrentSession().addCallback(mSessionCallBack);
        //Session.getCurrentSession().checkAndImplicitOpen();*/

        btnKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 카카오톡이 깔려있을 경우, 카카오톡을 호출해서 로그인 : loginWithKakaoTalk
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(mContext)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, kakaoCallback);
                } else {    // 카카오톡이 깔려있지 않을 경우, 웹 화면을 호출해서 로그인 : loginWithKakaoAccount
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, kakaoCallback);
                }
            }
        });
        //</editor-fold>

        //<editor-fold desc="구글 로그인 객체">
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        auth = FirebaseAuth.getInstance();  // 현재 인증상태를 확인 파이어베이스 인증 객체 초기화

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 구글 로그인 화면 intent
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_SIGN_GOOGLE);
            }
        });
        //</editor-fold>

        //<editor-fold desc="페이스북 로그인">
        btnFaceBookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();
            }
        });
        //</editor-fold>

        //<editor-fold desc="로그아웃">
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login_state != "logout") {
                    switch (login_state) {
                        case "n":
                            mOAuthLoginModule.logout(getApplicationContext());
                            txtLoginState.setText("네이버 로그아웃");
                            break;
                        case "k":
                            new Thread()
                            {
                                public void run()
                                {
                                    UserApiClient.getInstance().logout(error -> {
                                        if (error != null) {
                                            Log.e("TAG", "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                                        }else{
                                            Log.e("TAG", "로그아웃 성공, SDK에서 토큰 삭제됨");
                                            txtLoginState.setText("카카오톡 로그아웃");
                                        }
                                        return null;
                                    });
                                }
                            }.start();
                            break;
                        case "g":
                            FirebaseAuth.getInstance().signOut();
                            txtLoginState.setText("구글 로그아웃");
                            break;
                        case "f":
                            LoginManager.getInstance().logOut();
                            txtLoginState.setText("페이스북 로그아웃");
                            break;
                    }
                    login_state = "logout";
                    new Thread()
                    {
                        public void run()
                        {
                            handlerMsg = btnVisibleHandler.obtainMessage();
                            btnVisibleHandler.sendMessage(handlerMsg);
                        }
                    }.start();
                } else {
                    txtLoginState.setText("로그인을 하지 않았습니다. login_state : " + login_state);
                    new Thread()
                    {
                        public void run()
                        {
                            handlerMsg = btnInVisibleHandler.obtainMessage();
                            btnInVisibleHandler.sendMessage(handlerMsg);
                        }
                    }.start();
                }
            }
        });
        //</editor-fold>
//</editor-fold>

//<editor-fold desc="카카오 링크 기능 구현">
        btnKakaoMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                kakaoLink();
                //LinkClient.getInstance().defaultTemplate(mContext, defaultFeed);
                //TalkApiClient.getInstance().sendDefaultMemo(DefaultTemplate, KakaoMessageCallBack));
            }
        });
//</editor-fold>

//<editor-fold desc="솔라피 문자 보내기">
        //<editor-fold desc="문자 보내기 버튼 클릭">
        btnSolapi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        // 솔라피 문자보내기 메서드 호출
                        SolapiRequest();

                        // 인증번호 레이아웃 보이기
                        handlerMsg = KeyLiVisible.obtainMessage();
                        KeyLiVisible.sendMessage(handlerMsg);
                    }
                }.start();
                seconds[0] = 180;   // 인증시간 3분 제한
                TimeCheck();    // 인증 유효 시간 체크
            }
        });
        //</editor-fold>

        //<editor-fold desc="문자 인증버튼">
        btnKeyEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인증 시간 초과
                if (seconds[0] <= 0) {
                    Toast.makeText(mContext,"인증 유효 시간이 초과하였습니다.",Toast.LENGTH_LONG).show();
                    messageKey = ""; // 인증번호용으로 만들었던 messageKey 초기화
                }
                else { 
                    // 인증 성공
                    if (edtMessageKey.getText().toString().equals(messageKey)) {
                        Toast.makeText(mContext, "인증이 완료되었습니다.", Toast.LENGTH_LONG).show();
                        
                        edtMessageKey.setText(""); // 인증번호 입력칸 초기화
                        messageKey = ""; // 인증번호용으로 만들었던 messageKey 초기화
                        
                        // 인증번호 레이아웃 숨기기
                        handlerMsg = KeyLiINVisible.obtainMessage();
                        KeyLiINVisible.sendMessage(handlerMsg);
                        // 인앱결제&게시판이동 버튼 레이아웃 보이기
                        handlerMsg = PayLiVisible.obtainMessage();
                        PayLiVisible.sendMessage(handlerMsg);
                    }
                    else    // 인증번호 오류
                        Toast.makeText(mContext,"인증 번호를 확인하여주시기 바랍니다.",Toast.LENGTH_LONG).show();
                }

            }
        });
        //</editor-fold>
//</editor-fold>

//<editor-fold desc="인앱결제">
        initBillingClient();

        btnGooglePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBillingFlow(skuDetailsBuy);
            }
        });
//</editor-fold>
        
//<editor-fold desc="게시판 구현">
        btnIntentBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                startActivity(intent);
            }
        });
//</editor-fold>
    }

//<editor-fold desc="Visible - 레이아웃 숨기기/보이기 핸들러">
    final static Handler btnInVisibleHandler = new Handler()
    {
        public void handleMessage(Message msg) {
            liBtnLogout.setVisibility(View.INVISIBLE);
            liBtnLogin.setVisibility(View.VISIBLE);
        }
    };

    final static Handler btnVisibleHandler = new Handler()
    {
        public void handleMessage(Message msg) {
            liBtnLogout.setVisibility(View.VISIBLE);
            liBtnLogin.setVisibility(View.INVISIBLE);
        }
    };

    final static Handler KeyLiVisible = new Handler()
    {
        public void handleMessage(Message msg) {
            liNumeric.setVisibility(View.VISIBLE);
        }
    };

    final static Handler KeyLiINVisible = new Handler()
    {
        public void handleMessage(Message msg) {
            liNumeric.setVisibility(View.INVISIBLE);
        }
    };

    final static Handler PayLiVisible = new Handler()
    {
        public void handleMessage(Message msg) {
            liGooglePay.setVisibility(View.VISIBLE);
        }
    };

    final static Handler PayLiINVisible = new Handler()
    {
        public void handleMessage(Message msg) {
            liGooglePay.setVisibility(View.INVISIBLE);
        }
    };
//</editor-fold>

//<editor-fold desc="SNS 로그인 기능 구현 메서드">

    //<editor-fold desc="네이버 로그인 기능 수행">
    // 네이버 로그인 쓰레드
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String accessToken = mOAuthLoginModule.getAccessToken(mContext); // 로그인 토큰 얻어오기
                        String header = "Bearer " +  accessToken;
                        Map<String, String> requestHeaders = new HashMap<>();
                        requestHeaders.put("Authorization", header);
                        String apiURL = "https://openapi.naver.com/v1/nid/me"; //엑세스 토큰으로 유저정보를 받아올 주소
                        String responseBody = get(apiURL,requestHeaders);
                        Log.d("responseBody 확인 ",responseBody); //주소로 얻은 유저정보 (제이슨 파싱)
                        NaverUserInfo(responseBody); // 위에서 얻은 responseBody를 가지고 필요한 데이터를 추출하는 메서드 호출

                        // 네이버 로그인이 완료되었으므로, 로그인 상태를 logout->n으로 변경
                        // 로그아웃 외 버튼 보이기
                        // 네이버 로그인 계정 이름으로 텍스트뷰 변경
                        login_state = "n";
                        new Thread()
                        {
                            public void run()
                            {
                                handlerMsg = btnInVisibleHandler.obtainMessage();
                                btnInVisibleHandler.sendMessage(handlerMsg);
                                txtLoginState.setText("네이버 : " + login_name);
                            }
                        }.start();
                    }
                }).start();

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 유저정보 ResponseBody에서 필요한 데이터 추출
    private void NaverUserInfo(String msg){
        // Json 파싱
        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
            String resultcode = jsonObject.get("resultcode").toString();
            Log.d("resultcode 확인 ",resultcode);

            // ResponseBody 내 데이터 중 "message"를 키값으로 갖는 value값 추출 => "success"
            String message = jsonObject.get("message").toString();
            Log.d("message 확인 ",message);

            // resultcode = "00" : 로그인 성공 코드
            if(resultcode.equals("00")){
                // message = "success" : 로그인 성공 메세지
                if(message.equals("success")){
                    JSONObject naverJson = (JSONObject) jsonObject.get("response");

                    // id(암호화), email, mobile(핸드폰번호), mobile_e164(+82), name 중 원하는 데이터 추출
                    login_name = naverJson.get("name").toString();
                    Log.d("이름 확인 ",login_name);
                }
            }
            else{   // 로그인 실패 시 토스트 메세지 띄우기
                Toast.makeText(getApplicationContext(),"로그인 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // ResponseBody를 읽어오기 위한 메서드
    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            java.net.URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    //</editor-fold>

    //<editor-fold desc="카카오 키해시 메소드">
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(),0));
                Log.e("Hash Key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
    }
    //</editor-fold>

    //<editor-fold desc="카카오톡 로그인 sdk v2">
    Function2<OAuthToken, Throwable, Unit> kakaoCallback = (oAuthToken, throwable) -> {
        if (oAuthToken != null) {
            Log.e("TAG", "로그인 성공");
        }
        if (throwable != null) {
            Log.e("TAG", "로그인 실패", throwable);
            Log.d("TAG", "Message : " + throwable.getLocalizedMessage());
        }
        getKaKaoProfile();  // 유저 정보 가져오는 메서드
        return null;
    };
    private void getKaKaoProfile() {
        UserApiClient.getInstance().me((user, throwable) -> {
            // 로그인 성공시
            if (user != null) {
                new Thread()
                {
                    public void run()
                    {
                        handlerMsg = btnInVisibleHandler.obtainMessage();
                        btnInVisibleHandler.sendMessage(handlerMsg);
                    }
                }.start();

                login_state = "k"; // 로그인 상태를 k로 변경
                login_name = user.getKakaoAccount().getProfile().getNickname(); // 유저 Nickname 추출
                Log.d("TAG", "Kakao id =" + login_name);
                txtLoginState.setText("카카오톡 : " + login_name);
                Toast.makeText(mContext,"환영합니다!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext,"계정 정보 없음!", Toast.LENGTH_LONG).show();
            }
            if (throwable != null) {
                Log.d("TAG", "invoke: " + throwable.getLocalizedMessage());
            }
            return null;
        });
    }
    //</editor-fold>

/*    //<editor-fold desc="카카오 로그인 sdk ver1">
    static private ISessionCallback mSessionCallBack = new ISessionCallback() {
        @Override
        public void onSessionOpened() { // access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태
            Log.d("Kakao Login","MainActivity.java - onSessionOpened()");
            // 로그인 요청
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    // 로그인 실패
                    Toast.makeText(mContext,"로그인 도중에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    // 세션이 닫힘..
                    Toast.makeText(mContext,"세션이 닫혔습니다. 다시 시도하세요", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Toast.makeText(mContext,"환영합니다!", Toast.LENGTH_LONG).show();
                    login_state = "k";
                    new Thread()
                    {
                        public void run()
                        {
                            Message msg = btnInVisibleHandler.obtainMessage();
                            btnInVisibleHandler.sendMessage(msg);
                        }
                    }.start();
                    login_name = result.getKakaoAccount().getProfile().getNickname();
                    txtLoginState.setText("카카오톡 : " + login_name);
                    //txtLoginState.setText("카카오톡 로그인");
                    Log.d("Kakao Login","카카오 로그인 성공");
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) { // 네트워크 등으로 로그인 실패
            Toast.makeText(mContext,exception.toString(), Toast.LENGTH_LONG).show();
            Log.d("Kakao Login",exception.toString());
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //</editor-fold>

    //<editor-fold desc="카카오 로그아웃 핸들러">
    final Handler KakaoLogoutHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            UserManagement.getInstance()
                    .requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            txtLoginState.setText("카카오톡 로그아웃");
                        }
                    });
        }

    };
    //</editor-fold>*/

    //<editor-fold desc="구글 로그인 인증 요청 -> 결과값">
    public void resultGoogleLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 로그인 성공 시
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "구글 로그인 성공", Toast.LENGTH_LONG).show();
                            login_state = "g";
                            new Thread()
                            {
                                public void run()
                                {
                                    handlerMsg = btnInVisibleHandler.obtainMessage();
                                    btnInVisibleHandler.sendMessage(handlerMsg);
                                }
                            }.start();
                            login_name = account.getDisplayName();  // 유저 정보 name 추출
                            txtLoginState.setText("구글 : " + login_name);
                        } else {
                            Toast.makeText(getApplicationContext(), "구글 로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    //</editor-fold>

    //<editor-fold desc="페이스북 로그인">
    public void facebookLogin() {
        // 페이스북 로그인 응답을 처리할 콜백 관리자
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
        fb = true;

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();

                        GraphRequest request = GraphRequest.newMeRequest(fbAccessToken, (object, response) -> {
                            try {
                                login_name = object.getString("name");
                                txtLoginState.setText("페이스북 :" + login_name);
                            } catch (JSONException je) {
                                Log.e("FB", "No key provided.");
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                        login_state = "f";
                        Log.w("TAG", "LoginManager - onSuccess() called ");
                        Log.w("TAG", callbackManager.toString());

                        new Thread()
                        {
                            public void run()
                            {
                                handlerMsg = btnInVisibleHandler.obtainMessage();
                                btnInVisibleHandler.sendMessage(handlerMsg);
                            }
                        }.start();
                    }

                    @Override
                    public void onCancel() {
                        txtLoginState.setText("LoginManager - onCancel() called ");
                        Log.w("TAG", "LoginManager - onCancel() called ");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        txtLoginState.setText("LoginManager - onError() called ");
                        Log.w("TAG", "LoginManager - onError() called ");
                    }
                });
    }
    //</editor-fold>

    //<editor-fold desc="onActivityResult : 로그인 화면으로 갔다가 돌아왔을 때 실행 메서드">
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (fb) {   // 페이스북 로그인
            callbackManager.onActivityResult(requestCode, resultCode, data);
            fb = false;
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (!fb) {
            if (requestCode == REQ_SIGN_GOOGLE) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    resultGoogleLogin(account); // 유저 정보 추출 메서드
                } catch (ApiException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("google login",e.toString());
                }
            }
        }
    }
    //</editor-fold>
    //</editor-fold>

//<editor-fold desc="카카오 링크 기능 메서드">
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void kakaoLink() {
        // 템플릿 내에서 웹으로 연결할 링크
        Link link = new Link("https://jhuinee-diary.tistory.com/","https://jhuinee-diary.tistory.com/");

        // 템플릿 생성
        GlobalVar.content = new Content("재희 티스토리 오라구우우ㅜㅇ~!!!"
                , "https://img1.daumcdn.net/thumb/C428x428/?scode=mtistory2&fname=https%3A%2F%2Ftistory2.daumcdn.net%2Ftistory%2F4285962%2Fattach%2F4037b60ec2af4d2d8342ff55c4569e52"
                , link , "저에 대해 알고싶죠? 티스토리 놀러오세요. 저에 대한 모든 걸 알 수 있답니다♥");
        // 좋아요, 댓글, 공유
        GlobalVar.social = new Social(286,45,845);
        // 템플릿 하단 버튼 설정
        buttons = List.of(new com.kakao.sdk.template.model.Button("웹으로 보기",link));

        GlobalVar.defaultFeed = new FeedTemplate(GlobalVar.content, GlobalVar.social, buttons);

        LinkClient.getInstance().defaultTemplate(mContext, GlobalVar.defaultFeed,null, (linkResult, throwable) -> {
            if (throwable != null) {
                Log.e("TAG", "카카오링크 보내기 실패", throwable);
            }
            else if (linkResult != null) {
                mContext.startActivity(linkResult.getIntent());

                // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                Log.w("TAG", "Warning Msg: "+ linkResult.getWarningMsg());
                Log.w("TAG", "Argument Msg: "+ linkResult.getArgumentMsg());
            }
            return null;
        });
    }
    //</editor-fold>

//<editor-fold desc="솔라피 문자 보내기 메서드">
    //<editor-fold desc="솔라피 문자 보내기 메서드">
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SolapiRequest() {
        try {
            String apiKey = "NCS4Q4MVPDJ4DPPE";
            String apiSecret = "LIMGDHWILP38CX01B768KO4PLPXZCZTS";

            /*
            date / salt / signature 값 받아오기
             */
            // salt값 생성 : 12 ~ 64바이트의 불규칙적이고 랜덤한 문자열을 생성
            String salt = UUID.randomUUID().toString().replaceAll("-", "");
            // DateTime : ISO 8601 규격의 날짜와 시간을 입력합니다. (안드로이드 7.0 버전 실행X)
            String date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString().split("\\[")[0];
            // Signature 값 생성 : <Date Time>과 <Salt>를 하나로 연결한 문자열을 데이터로 하고 API Secret을 Key로 만들어진 HMAC 해시코드
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            String signature = Hex.bytesToStringUppercase(sha256_HMAC.doFinal((date + salt).getBytes(StandardCharsets.UTF_8)));

            // 단일문자 전송 url
            String targetUrl = "http://api.solapi.com/messages/v4/send";

            /* 인증번호로 사용할 messageKey 생성 */
            messageKey = "";        // 인증번호로 사용할 messageKey 초기화
            for(int i=0;i<6;i++) {  // 6자리 랜덤숫자를 이용한 인증번호 생성
                messageKey += String.valueOf((int)(Math.random()*10000)%9);
            }

            // 문자를 전송할 텍스트
            String messageText = "[Solapi]인증번호는 " + messageKey + "입니다.";

            // Type : SMS(단문), LMS(장문), MMS(멀티), ATA(알림톡)
            String parameters = "{\"message\":{\"to\":\"01025545005\",\"from\":\"01050027700\",\"text\":\""+messageText+"\"}}";
            String Authorization = "HMAC-SHA256 apiKey=NCS4Q4MVPDJ4DPPE, date=" + date
                    + ", salt="+salt
                    +", signature=" + signature;

            URL url = new URL(targetUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");

            con.setRequestProperty("Authorization", Authorization);
            con.setRequestProperty("Content-Type", "application/json");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            // wr.writeByte()를 사용하면 한글 깨짐
            // write에서도 한글 깨짐을 방지하기 위해 "utf-8"로 변경
            wr.write(parameters.getBytes("utf-8"));
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            Log.d("HTTP response code : ", String.valueOf(responseCode));
            Log.d("HTTP body : ", response.toString());

        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            Log.e("Solapi IOException : ", e.toString());
        }
    }
    //</editor-fold>
    
    //<editor-fold desc="인증번호 유효시간 체크">
    public void TimeCheck() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (seconds[0] != 0) {
                    seconds[0]--;
                    int minute = seconds[0]/60;
                    int second = seconds[0]%60;
                    txtTime.setText(String.format("%02d",minute)  + ":" + String.format("%02d",second));
                }
            }
        };

        timer.schedule(timerTask,1000,1000);
    }
//</editor-fold>
//</editor-fold>

//<editor-fold desc="인앱결제 메서드">
//<editor-fold desc="BillingClient 초기화">
    private void initBillingClient() {
        purchasesUpdatedListener = (billingResult, list) -> {
            // onPurchaseUpdated()는 인터페이스를 구현하는 리스터(purchasesUpdatedListener)에 구매 작업 결과를 전송
            // onPurchaseUpdated() 람다식 표현..
            Log.d("TAG", "initBillingClient: " + billingResult.getResponseCode());
            // BillingResponseCode가 OK가 떨어지고 구매목록이 null이 아닐 때
            // 구매 작업이 진행될 때
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && list != null) {
                for (Purchase purchase : list) {
                    // 소비성(일회성) vs 정기결제 결정 메서드
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                Log.d("TAG", "user purchase cancel");
            } else {
                // Handle any other error codes.
                Log.d("TAG", "google purchase error");
            }
        };

        billingClient = BillingClient.newBuilder(mContext)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The billing client is ready. You can query purchases here.
                    // 구입 가능한 제품 표시
                    List<String> skuList = new ArrayList<>();
                    skuList.add(skuID);

                    // Google Play에 인앱 상품 세부 정보를 쿼리하려면 querySkuDetailsAsync() 호출
                    // querySkuDetailsAsync()를 호출할 때 SkuType과 함께 Google Play Console에서 생성된 제품 ID 문자열 목록을 지정하는 SkuDetailsParams의 인스턴스 전달
                    // SkuType : 일회성 제품->SkuType.INAPP, 정기결제->SkuType.SUBS
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(), (billingResult1, skuDetailsList) -> {
                        // Process the result.
                        if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                            for (SkuDetails skuDetailsObject  : skuDetailsList) {
                                sku = skuDetailsObject.getSku();
                                skuDetailsBuy = (SkuDetails)skuDetailsObject;
                            }
                        }
                    });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }
    //</editor-fold>

    /* 구매 흐름의 시작! */
    // querySkuDetailsAsync() 호출에서 얻은 관련 SkuDetails 객체가 포함된 BillingFlowParams 객체를 참조 : startConnection
    private void doBillingFlow(SkuDetails skuDetails) {
        BillingFlowParams flowParams;
        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
        int responseCode = billingClient.launchBillingFlow(MainActivity.this, flowParams).getResponseCode();
    }

    // 소비성(일회성) vs 정기결제 결정 메서드
    private void handlePurchase(Purchase purchase) {
        String purchaseToken;
        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();

        // 일회성 결제의 경우 consumeAsync() 메서드는 확인 요구사항을 추옺ㄱ하며 앱이 사용자에게 자격을 부여했음을 나타냄
        // 이 메서드를 사용하면 일회성 제품을 다시 구매 가능
        billingClient.consumeAsync(consumeParams, consumeListener);
    }

    // 소비 작업의 결과를 처리하는 객체
    ConsumeResponseListener consumeListener = (billingResult, purchaseToken) -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            // Handle the success of the consume operation.
            // For example, increase the number of coins inside the user's basket.
        }
    };

//</editor-fold>
}