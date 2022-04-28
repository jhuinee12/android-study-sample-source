package com.example.boardqactivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView;

import com.example.boardqactivity.dataclass.BoardData;
import com.example.boardqactivity.dataclass.CommentData;
import com.example.boardqactivity.dataclass.MyQAData;
import com.example.boardqactivity.dataclass.QAData;
import com.example.boardqactivity.helper.BoardDBHelper;
import com.example.boardqactivity.helper.CommentDBHelper;
import com.example.boardqactivity.helper.MyQADBHelper;

import java.util.ArrayList;

public class GlobalVar {
    public static Boolean insertIntent = false;
    public static Bitmap boardImage;

    public static BoardDBHelper boardHelper;
    public static Cursor boardCursor;
    public static RecyclerView.Adapter rcyBoardAdapter;

    public static CommentDBHelper commentHelper;
    public static Cursor commentCursor;
    public static RecyclerView.Adapter rcyCommentAdapter;

    public static MyQADBHelper myQAHelper;
    public static Cursor myQACursor;
    public static RecyclerView.Adapter rcyMyQAAdapter;

    public static RecyclerView.Adapter rcyQAAdapter;

    public static SQLiteDatabase insertQuestionDB;
    public static SQLiteDatabase insertCommentDB;

    public static boolean updateIntent = false;

    public static String BOARD_TABLE_NAME = "TBL_BoardTest";
    public static String BOARD_KEY_ID = "BoardKeyId";
    public static String BOARD_KEY_TITLE = "BoardKeyTitle";
    public static String BOARD_KEY_DESC = "BoardKeyDesc";
    public static String BOARD_KEY_IMAGE = "BoardKeyImage";
    public static String BOARD_KEY_CUSTID = "BoardKeyCustId";
    public static String BOARD_KEY_DATE = "BoardKeyDate";

    public static String COMMENT_TABLE_NAME = "TBL_CommentTest";
    public static String COMMENT_KEY_ID = "CommentKeyId";
    public static String COMMENT_KEY_IMAGE = "CommentKeyImage";
    public static String COMMENT_KEY_DESC = "CommentKeyDesc";
    public static String COMMENT_KEY_DATE = "CommentKeyDate";
    public static String COMMENT_KEY_BOARDID = "CommentKeyBoardId";
    public static String COMMENT_KEY_CUSTID = "CommentKeyCustId";

    public static String MYQA_TABLE_NAME = "TBL_MyQATest";
    public static String MYQA_KEY_ID = "MyQAKeyId";
    public static String MYQA_KEY_TITLE = "MyQAKeyTitle";
    public static String MYQA_KEY_DESC = "MyQAKeyDesc";
    public static String MYQA_KEY_CUSTID = "MyQAKeyCustId";
    public static String MYQA_KEY_COMMENT = "MyQAKeyComment";
    public static String MYQA_KEY_DATE = "MyQAKeyDate";

    public static ArrayList<BoardData> rcyBoardList = new ArrayList<>();
    public static ArrayList<BoardData> BoardReaderList = new ArrayList<>();
    public static ArrayList<CommentData> rcyCommentList = new ArrayList<>();
    public static ArrayList<QAData> rcyQAList = new ArrayList<>();
    public static ArrayList<MyQAData> rcyMyQAList = new ArrayList<>();
}
