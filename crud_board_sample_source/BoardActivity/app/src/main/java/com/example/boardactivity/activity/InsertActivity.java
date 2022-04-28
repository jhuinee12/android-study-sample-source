package com.example.boardactivity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.boardactivity.R;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.boardactivity.GlobalVar.*;

public class InsertActivity extends AppCompatActivity {

    public static Button btnBoardInput;
    public static EditText edtBoardTitle;
    public static EditText edtBoardDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        SQLiteDatabase insertDB = boardHelper.getWritableDatabase();

        edtBoardTitle = findViewById(R.id.edt_board_title);
        edtBoardDesc = findViewById(R.id.edt_board_desc);
        btnBoardInput = findViewById(R.id.btn_board_input);

/*        // 리사이클러뷰 내 아이템 선택 후 intent 되었을 때 : 수정화면
        if (insertIntent) {
            Intent intent = getIntent();
            String title = intent.getStringExtra("itemTitle");
            String desc = intent.getStringExtra("itemDesc");
            String id = intent.getStringExtra("itemId");

            btnInput.setText("수정하기");
            edtTitle.setText(title);
            edtDesc.setText(desc);

            btnInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uQuery = String.format(
                            "UPDATE %s SET title = '%s', desc = '%s' where _id = '%s'"
                        , TABLE_NAME, edtTitle.getText(), edtDesc.getText(), id
                    );
                    Log.e("TAG",uQuery);

                    *//* 리사이클러뷰 업데이트 *//*
                    insertDB.execSQL( uQuery );
                    mHelper.mSelect();
                    rcyAdapter.notifyDataSetChanged();

                    insertIntent = false;
                    finish();
                }
            });
        } else {    // 글쓰기 버튼 선택 후 intent 되었을 때 : 입력 화면*/
            btnBoardInput.setOnClickListener(v -> {
                Date currentDateTime = Calendar.getInstance().getTime();
                // 키값 : 날짜시간
                String id = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(currentDateTime);
                String date = new SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime);
                String query = String.format(
                        "INSERT INTO %s VALUES ('%s', null, '%s', '%s' , '%s', '%s');"
                        , BOARD_TABLE_NAME, id, edtBoardTitle.getText().toString(), edtBoardDesc.getText().toString(), "name", date);
                Log.e("TAG",query);

                /* 리사이클러뷰 업데이트 */
                insertDB.execSQL( query );
                boardHelper.rcyBoardSelect();
                rcyBoardAdapter.notifyDataSetChanged();

                finish();
            });
/*        }*/
    }
}