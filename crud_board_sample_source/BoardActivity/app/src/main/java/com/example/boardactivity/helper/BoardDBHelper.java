package com.example.boardactivity.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.boardactivity.BoardData;

import static com.example.boardactivity.GlobalVar.*;

import java.util.ArrayList;

public class BoardDBHelper extends SQLiteOpenHelper {
    public BoardDBHelper(Context context) {
        super(context, "My_Board_Data.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        // AUTOINCREMENT 속성 사용 시 PRIMARY KEY로 지정한다.
        String query = String.format("CREATE TABLE %s ("
                + "%s TEXT PRIMARY KEY, "
                + "%s BLOB, "
                + "%s TEXT, "
                + "%s TEXT, "
                + "%s TEXT, "
                + "%s TEXT);", BOARD_TABLE_NAME, BOARD_KEY_ID, BOARD_KEY_IMAGE, BOARD_KEY_TITLE, BOARD_KEY_DESC, BOARD_KEY_CUSTID, BOARD_KEY_DATE);
        db.execSQL(query);
    }

    // DB 스키마가 최근 것을 반경하게 해줌
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", BOARD_TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }

    public ArrayList<BoardData> rcyBoardSelect() {
        rcyBoardList.clear();

        // 읽기가 가능하게 DB 열기
        SQLiteDatabase sqlDb = getReadableDatabase();
        sqlDb.beginTransaction();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        String insertQuery = String.format(" SELECT * FROM %s", BOARD_TABLE_NAME);
        boardCursor = sqlDb.rawQuery(insertQuery, null);
        sqlDb.setTransactionSuccessful();

        while (boardCursor.moveToNext()) {
            BoardData item = new BoardData(boardCursor.getString(0)
                    ,null
                    ,boardCursor.getString(2)
                    ,boardCursor.getString(3)
                    ,boardCursor.getString(4)
                    ,boardCursor.getString(5));

            rcyBoardList.add(item);
        }
        sqlDb.endTransaction();
        return rcyBoardList;
    }

    public ArrayList<BoardData> rcyReaderSelect(String key_id) {
        BoardReaderList.clear();

        // 읽기가 가능하게 DB 열기
        SQLiteDatabase sqlDb = getReadableDatabase();
        sqlDb.beginTransaction();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        String insertQuery = String.format(" SELECT * FROM %s where %s = %s", BOARD_TABLE_NAME, BOARD_KEY_ID, key_id);
        boardCursor = sqlDb.rawQuery(insertQuery, null);
        sqlDb.setTransactionSuccessful();

        while (boardCursor.moveToNext()) {
            BoardData item = new BoardData(boardCursor.getString(0)
                    ,null
                    ,boardCursor.getString(2)
                    ,boardCursor.getString(3)
                    ,boardCursor.getString(4)
                    ,boardCursor.getString(5));

            BoardReaderList.add(item);
        }
        sqlDb.endTransaction();
        return BoardReaderList;
    }
}