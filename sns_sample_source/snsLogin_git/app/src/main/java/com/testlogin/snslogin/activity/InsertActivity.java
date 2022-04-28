package com.testlogin.snslogin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.testlogin.snslogin.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.testlogin.snslogin.GlobalVar.*;

public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        SQLiteDatabase insertDB = mHelper.getWritableDatabase();

        edtTitle = findViewById(R.id.edt_title);
        edtDesc = findViewById(R.id.edt_desc);
        btnInput = findViewById(R.id.btn_input);

        // 리사이클러뷰 내 아이템 선택 후 intent 되었을 때 : 수정화면
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
                            , TABLE_NAME, title, desc, id
                    );
                    Log.e("TAG",uQuery);

                    /* 리사이클러뷰 업데이트 */
                    insertDB.execSQL( uQuery );
                    mHelper.mSelect();
                    rcyAdapter.notifyDataSetChanged();

                    insertIntent = false;
                    finish();
                }
            });
        } else {    // 글쓰기 버튼 선택 후 intent 되었을 때 : 입력 화면
            btnInput.setOnClickListener(v -> {
                Date currentDateTime = Calendar.getInstance().getTime();
                // 키값 : 날짜시간
                String dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(currentDateTime);
                String query = String.format(
                        "INSERT INTO %s VALUES ('%s', null, '%s', '%s' , '%s');"
                        , TABLE_NAME, dateFormat, edtTitle.getText().toString(), edtDesc.getText().toString(), login_name);
                Log.e("TAG",query);

                /* 리사이클러뷰 업데이트 */
                insertDB.execSQL( query );
                mHelper.mSelect();
                rcyAdapter.notifyDataSetChanged();

                finish();
            });
        }
    }
}