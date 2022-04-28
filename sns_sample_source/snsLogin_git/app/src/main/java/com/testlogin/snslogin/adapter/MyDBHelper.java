package com.testlogin.snslogin.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.testlogin.snslogin.BoardData;

import java.util.ArrayList;

import static com.testlogin.snslogin.GlobalVar.*;

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        super(context, "My_Account_Data.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        // AUTOINCREMENT 속성 사용 시 PRIMARY KEY로 지정한다.
        String query = String.format("CREATE TABLE %s ("
                + "%s TEXT PRIMARY KEY, "
                + "%s BLOB, "
                + "%s TEXT, "
                + "%s TEXT, "
                + "%s TEXT);", TABLE_NAME, KEY_ID, KEY_IMAGE, KEY_TITLE, KEY_DESC, KEY_NAME);
        db.execSQL(query);
    }

    // DB 스키마가 최근 것을 반경하게 해줌
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }

    public ArrayList<BoardData> mSelect() {
        data_list.clear();

        // 읽기가 가능하게 DB 열기
        SQLiteDatabase sqlDb = getReadableDatabase();
        sqlDb.beginTransaction();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        String insertQuery = String.format(" SELECT * FROM %s", TABLE_NAME);
        cursor = sqlDb.rawQuery(insertQuery, null);
        sqlDb.setTransactionSuccessful();

        while (cursor.moveToNext()) {
            BoardData item = new BoardData(cursor.getString(0)
                    ,null
                    ,cursor.getString(2)
                    ,cursor.getString(3)
                    ,cursor.getString(4));

            data_list.add(item);
        }
        sqlDb.endTransaction();
        return data_list;
    }
}