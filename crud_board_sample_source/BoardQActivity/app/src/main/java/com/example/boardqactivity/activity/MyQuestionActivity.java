package com.example.boardqactivity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.boardqactivity.R;
import com.example.boardqactivity.adapter.MyQARcvAdapter;
import com.example.boardqactivity.helper.MyQADBHelper;
import com.example.boardqactivity.popup.QuestionInputPopup;

import static com.example.boardqactivity.GlobalVar.insertQuestionDB;
import static com.example.boardqactivity.GlobalVar.myQAHelper;
import static com.example.boardqactivity.GlobalVar.rcyMyQAAdapter;

public class MyQuestionActivity extends AppCompatActivity {

    RecyclerView rcyMyQA;
    Button btnGotoQa;
    public static Activity myqaActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);
        myqaActivity = MyQuestionActivity.this;

        btnGotoQa = (Button)findViewById(R.id.btn_goto_qa);
        rcyMyQA = (RecyclerView)findViewById(R.id.rcy_myqa);

        myQAHelper = new MyQADBHelper(this);
        insertQuestionDB = myQAHelper.getWritableDatabase();

        rcyMyQAAdapter = new MyQARcvAdapter(this);
        rcyMyQA.setLayoutManager(new LinearLayoutManager(this));
        rcyMyQA.setAdapter(rcyMyQAAdapter);
        rcyMyQA.setHasFixedSize(true);

        btnGotoQa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyQuestionActivity.this, QuestionInputPopup.class);
                startActivity(intent);
            }
        });
    }
}