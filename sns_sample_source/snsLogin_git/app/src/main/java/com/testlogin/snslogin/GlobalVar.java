package com.testlogin.snslogin;

import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.CallbackManager;
import com.google.android.gms.common.SignInButton;
import com.kakao.sdk.template.model.Content;
import com.kakao.sdk.template.model.FeedTemplate;
import com.kakao.sdk.template.model.Social;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.testlogin.snslogin.adapter.MyDBHelper;

import java.util.ArrayList;
import java.util.List;

public class GlobalVar {
    public static Message handlerMsg;

    //<editor-fold desc="로그인 기능 선언부">
    public static String login_name = "";
    public static String login_state = "logout";
    public static Boolean fb = false;
    public static TextView txtLoginState;   // xml textView
    public static Context mContext; // MainActivity context
    public static OAuthLogin mOAuthLoginModule; // login 객체
    public static LinearLayout liBtnLogin;
    public static LinearLayout liBtnLogout;
    public static OAuthLoginButton btnNaverLogin;   // 네이버 로그인 버튼
    public static ImageView btnKakaoLogin; // 카카오 로그인 버튼
    public static SignInButton btnGoogleLogin;    // 구글 로그인 버튼
    public static com.facebook.login.widget.LoginButton btnFaceBookLogin;    // 페이스북 로그인 버튼
    public static Button btnLogout;    // 로그아웃 버튼
    public static CallbackManager callbackManager;  // 페이스북 로그인 응답 처리

    public static String naver_client_id = "sJ_NV0293unogP9djrdD";
    public static String naver_client_secret = "cssDTqnSIz";
    public static String naver_client_name = "snslogintest";
    //</editor-fold>

    //<editor-fold desc="카카오 알림톡 선언부">
    public static Button btnKakaoMessage;
    public static Content content;
    public static Social social;
    public static List<com.kakao.sdk.template.model.Button> buttons;
    //public static List<com.kakao.sdk.template.model.Button> buttons;
    public static FeedTemplate defaultFeed;
    //</editor-fold>

    //<editor-fold desc="솔라피 문자 선언부">
    public static LinearLayout liNumeric;
    public static Button btnSolapi;
    public static Button btnKeyEnter;
    public static EditText edtMessageKey;
    public static TextView txtTime;

    public static final int[] seconds = {0};
    public static String messageKey = "";
    public static Boolean certification = false;
    //</editor-fold>

    //<editor-fold desc="인앱결제 선언부">
    public static LinearLayout liGooglePay;
    public static Button btnGooglePay;
    public static TextView txtGooglePay;
    //</editor-fold>

    //<editor-fold desc="게시판 구현 선언부">
    public static Context bContext;
    public static Boolean insertIntent = false;

    public static MyDBHelper mHelper;
    public static Cursor cursor;
    public static RecyclerView.Adapter rcyAdapter;

    public static String TABLE_NAME = "TBL_BoardTest";
    public static String KEY_ID = "_id";
    public static String KEY_TITLE = "title";
    public static String KEY_DESC = "desc";
    public static String KEY_IMAGE = "image";
    public static String KEY_NAME = "name";

    public static SwipeRefreshLayout SwipeRefresh;
    public static Button btnIntentBoard;
    public static Button btnInsert;
    public static Button btnInput;
    public static EditText edtTitle;
    public static EditText edtDesc;
    public static RecyclerView rcyView;

    public static ArrayList<BoardData> data_list = new ArrayList<>();
    //</editor-fold>
}
