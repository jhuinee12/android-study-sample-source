package com.testlogin.snslogin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.testlogin.snslogin.R;
import com.testlogin.snslogin.adapter.MyDBHelper;
import com.testlogin.snslogin.adapter.RecyclerviewAdapter;

import static com.testlogin.snslogin.GlobalVar.*;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        bContext = getApplicationContext();

        SwipeRefresh = findViewById(R.id.swipe_refresh);
        btnInsert = findViewById(R.id.btn_insert);
        rcyView = findViewById(R.id.rcy_view);

        // 데이터베이스 생성
        mHelper = new MyDBHelper(this);

        rcyAdapter = new RecyclerviewAdapter(bContext, this);
        rcyView.setLayoutManager(new LinearLayoutManager(this));
        rcyView.setAdapter(rcyAdapter);
        rcyView.setHasFixedSize(true);

        // 리사이클러뷰 당겨서 새로고침
        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rcyAdapter.notifyDataSetChanged();
                SwipeRefresh.setRefreshing(false);
            }
        });

        // 글쓰기 페이지로 이동
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });

    }

}