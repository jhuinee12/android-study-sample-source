package com.example.boardqactivity.popup;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.boardqactivity.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.boardqactivity.GlobalVar.*;

public class QuestionInputPopup extends Activity {

    Button btnQuestionInput;
    EditText edtQuestionTitle;
    EditText edtQuestionDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_question_input);

        btnQuestionInput = (Button)findViewById(R.id.btn_question_input);
        edtQuestionTitle = (EditText)findViewById(R.id.edt_question_title);
        edtQuestionDesc = (EditText)findViewById(R.id.edt_question_desc);

        btnQuestionInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDateTime = Calendar.getInstance().getTime();
                // 키값 : 날짜시간
                String questionId = "QA" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(currentDateTime);
                String questionDate = new SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime);
                String query = String.format(
                        "INSERT INTO %s VALUES ('%s', '%s', '%s', '%s', '%s', '%s');"
                        , MYQA_TABLE_NAME, questionId
                        , edtQuestionTitle.getText().toString()
                        , edtQuestionDesc.getText().toString()
                        ,"0000"
                        ,"문의 접수가 성공적으로 완료되었습니다. 답변 대기 중입니다."
                        , questionDate);
                Log.e("TAG",query);

                /* 리사이클러뷰 업데이트 */
                insertQuestionDB.execSQL( query );
                myQAHelper.rcyMyQASelect();
                rcyMyQAAdapter.notifyDataSetChanged();

                finish();
            }
        });
    }
}