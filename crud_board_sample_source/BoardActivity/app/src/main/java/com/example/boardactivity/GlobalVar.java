package com.example.boardactivity;

import android.content.Context;
import android.database.Cursor;

import androidx.recyclerview.widget.RecyclerView;

import com.example.boardactivity.helper.BoardDBHelper;
import com.example.boardactivity.helper.CommentDBHelper;

import java.util.ArrayList;

public class GlobalVar {
    public static Context bContext;
    public static Boolean insertIntent = false;

    public static BoardDBHelper boardHelper;
    public static Cursor boardCursor;
    public static RecyclerView.Adapter rcyBoardAdapter;

    public static CommentDBHelper commentHelper;
    public static Cursor commentCursor;
    public static RecyclerView.Adapter rcyCommentAdapter;

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

    public static ArrayList<BoardData> rcyBoardList = new ArrayList<>();
    public static ArrayList<BoardData> BoardReaderList = new ArrayList<>();
    public static ArrayList<BoardData> rcyBoardSearchList = new ArrayList<>();
    public static ArrayList<CommentData> rcyCommentList = new ArrayList<>();
}
