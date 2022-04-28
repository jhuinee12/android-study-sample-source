package com.example.boardqactivity.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.boardqactivity.dataclass.MyQAData;

import java.util.ArrayList;

import static com.example.boardqactivity.GlobalVar.*;

public class MyQADBHelper extends SQLiteOpenHelper {
    public MyQADBHelper(Context context) {
        super(context, "My_QA_Data.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        // AUTOINCREMENT 속성 사용 시 PRIMARY KEY로 지정한다.
        String query = String.format("CREATE TABLE %s ("
                + "%s TEXT PRIMARY KEY, "
                + "%s TEXT, "
                + "%s TEXT, "
                + "%s TEXT, "
                + "%s TEXT, "
                + "%s TEXT);", MYQA_TABLE_NAME, MYQA_KEY_ID, MYQA_KEY_TITLE, MYQA_KEY_DESC, MYQA_KEY_CUSTID, MYQA_KEY_COMMENT, MYQA_KEY_DATE);
        db.execSQL(query);
    }

    // DB 스키마가 최근 것을 반경하게 해줌
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", MYQA_TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }

    public ArrayList<MyQAData> rcyMyQASelect() {
        rcyMyQAList.clear();

        // 읽기가 가능하게 DB 열기
        SQLiteDatabase sqlDb = getReadableDatabase();
        sqlDb.beginTransaction();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        String insertQuery = String.format(" SELECT * FROM %s", MYQA_TABLE_NAME);
        myQACursor = sqlDb.rawQuery(insertQuery, null);
        sqlDb.setTransactionSuccessful();

        while (myQACursor.moveToNext()) {
            MyQAData item = new MyQAData(myQACursor.getString(0)
                    ,myQACursor.getString(1)
                    ,myQACursor.getString(2)
                    ,myQACursor.getString(3)
                    ,myQACursor.getString(4)
                    ,myQACursor.getString(5));

            rcyMyQAList.add(item);
        }
        sqlDb.endTransaction();
        return rcyMyQAList;
    }
}