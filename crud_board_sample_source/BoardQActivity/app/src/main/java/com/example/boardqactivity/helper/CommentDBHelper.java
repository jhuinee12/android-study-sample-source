package com.example.boardqactivity.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.boardqactivity.dataclass.CommentData;

import java.util.ArrayList;

import static com.example.boardqactivity.GlobalVar.*;

public class CommentDBHelper extends SQLiteOpenHelper {
    public CommentDBHelper(Context context) {
        super(context, "My_Comment_Data.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        // AUTOINCREMENT 속성 사용 시 PRIMARY KEY로 지정한다.
        String query = String.format("CREATE TABLE %s ("
                + "%s TEXT PRIMARY KEY, "
                + "%s BITMAP, "
                + "%s TEXT, "
                + "%s TEXT, "
                + "%s TEXT, "
                + "%s TEXT);", COMMENT_TABLE_NAME, COMMENT_KEY_ID, COMMENT_KEY_IMAGE, COMMENT_KEY_DESC, COMMENT_KEY_DATE, COMMENT_KEY_BOARDID, COMMENT_KEY_CUSTID);
        db.execSQL(query);
    }

    // DB 스키마가 최근 것을 반경하게 해줌
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", BOARD_TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }

    public ArrayList<CommentData> rcyCommentSelect(String key_id) {
        rcyCommentList.clear();

        // 읽기가 가능하게 DB 열기
        SQLiteDatabase sqlDb = getReadableDatabase();
        sqlDb.beginTransaction();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        String insertQuery = String.format(" SELECT * FROM %s where %s = '%s'", COMMENT_TABLE_NAME, COMMENT_KEY_BOARDID, key_id);
        commentCursor = sqlDb.rawQuery(insertQuery, null);
        sqlDb.setTransactionSuccessful();

        while (commentCursor.moveToNext()) {
            CommentData item = new CommentData(commentCursor.getString(0)
                    ,null
                    ,commentCursor.getString(2)
                    ,commentCursor.getString(3)
                    ,commentCursor.getString(4)
                    ,commentCursor.getString(5));

            rcyCommentList.add(item);
        }
        sqlDb.endTransaction();
        return rcyCommentList;
    }
}