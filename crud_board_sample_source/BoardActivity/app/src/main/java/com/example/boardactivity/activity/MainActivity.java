package com.example.boardactivity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.boardactivity.R;
import com.example.boardactivity.adapter.BoardRcvAdapter;
import com.example.boardactivity.helper.BoardDBHelper;

import static com.example.boardactivity.GlobalVar.*;

public class MainActivity extends AppCompatActivity {
    public static SwipeRefreshLayout SwipeRefresh;
    public static Button btnIntentBoard;
    public static Button btnInsert;
    public static RecyclerView rcyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bContext = getApplicationContext();

        SwipeRefresh = findViewById(R.id.swipe_refresh);
        btnInsert = findViewById(R.id.btn_insert);
        rcyView = findViewById(R.id.rcy_view);

        // 데이터베이스 생성
        boardHelper = new BoardDBHelper(this);

        rcyBoardAdapter = new BoardRcvAdapter(bContext, this);
        rcyView.setLayoutManager(new LinearLayoutManager(this));
        rcyView.setAdapter(rcyBoardAdapter);
        rcyView.setHasFixedSize(true);

        // 리사이클러뷰 당겨서 새로고침
        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rcyBoardAdapter.notifyDataSetChanged();
                SwipeRefresh.setRefreshing(false);
            }
        });

        // 글쓰기 페이지로 이동
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });

    }

    /*//<editor-fold desc="앱바 보이기">
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_search, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("제목으로 검색합니다.");
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    public SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            rcyBoardSearchList.clear();
            if (s.length() == 0) {
                rcyBoardSearchList.addAll(rcyBoardList);
                mLockListView = false;
            }
            else {
                mLockListView = true;
                for (int i=0; i<rcyBoardSearchList.size(); i++)
                {
                    if(rcyBoardSearchList.get(i).getDataTitle().contains(s))
                    {
                        Log.d("TAG", rcyBoardList.toString());
                        rcyBoardList.add(rcyBoardSearchList.get(i));
                    }
                }
            }

            rcyAdapter.notifyDataSetChanged();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            rcyBoardSearchList.clear();
            if (s.length() == 0) {
                mLockListView = false;
                rcyBoardSearchList.addAll(rcyBoardList);
                SearchCount = 0;
            }
            else {
                mLockListView = true;
                for (int i=0; i<rcyBoardSearchList.size(); i++)
                {
                    if(rcyBoardSearchList.get(i).getDataTitle().contains(s))
                    {
                        Log.d("TAG", rcyBoardList.toString());
                        rcyBoardList.add(rcyBoardSearchList.get(i));
                    }
                }
            }

            rcyAdapter.notifyDataSetChanged();
            return false;
        }
    };
// </editor-fold>*/
}