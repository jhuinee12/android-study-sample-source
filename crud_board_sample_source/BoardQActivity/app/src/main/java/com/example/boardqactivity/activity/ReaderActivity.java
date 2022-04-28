package com.example.boardqactivity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.boardqactivity.R;
import com.example.boardqactivity.adapter.CommentRcvAdapter;
import com.example.boardqactivity.helper.CommentDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.boardqactivity.GlobalVar.*;

public class ReaderActivity extends AppCompatActivity {

    TextView txtReaderTitle;
    TextView txtReaderName;
    TextView txtReaderDate;
    TextView txtReaderDesc;
    TextView txtCommentCount;
    EditText edtComment;
    Button btnBoardUpdate;
    ImageButton ibCommentInput;
    ImageButton ibCommentRenew;
    RecyclerView rcvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        txtReaderTitle = (TextView)findViewById(R.id.txt_reader_title);
        txtReaderName = (TextView)findViewById(R.id.txt_reader_name);
        txtReaderDate = (TextView)findViewById(R.id.txt_reader_date);
        txtReaderDesc = (TextView)findViewById(R.id.txt_reader_desc);
        txtCommentCount = (TextView)findViewById(R.id.txt_comment_count);
        edtComment = (EditText)findViewById(R.id.edt_comment);
        btnBoardUpdate = (Button) findViewById(R.id.btn_board_update);
        ibCommentInput = (ImageButton) findViewById(R.id.ib_comment_input);
        ibCommentRenew = (ImageButton) findViewById(R.id.ib_comment_renew);
        rcvComment = (RecyclerView) findViewById(R.id.rcy_comment);

        Intent intent = getIntent();
        String boardId = intent.getStringExtra("intentBoardId");

        boardHelper.rcyReaderSelect(boardId);

        // 데이터베이스 생성
        commentHelper = new CommentDBHelper(this);

        insertCommentDB = commentHelper.getWritableDatabase();
        commentHelper = new CommentDBHelper(this);

        rcyCommentAdapter = new CommentRcvAdapter(getApplicationContext(), this, boardId);
        rcvComment.setLayoutManager(new LinearLayoutManager(this));
        rcvComment.setAdapter(rcyCommentAdapter);
        rcvComment.setHasFixedSize(true);

        txtReaderTitle.setText(BoardReaderList.get(0).getBoardDataTitle());
        txtReaderDesc.setText(BoardReaderList.get(0).getBoardDataDesc());
        txtReaderName.setText(BoardReaderList.get(0).getBoardDataName());
        txtReaderDate.setText(BoardReaderList.get(0).getBoardDataDate());
        txtCommentCount.setText(String.valueOf(rcyCommentAdapter.getItemCount()));

        btnBoardUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIntent = true;
                Intent intent = new Intent(ReaderActivity.this, InsertActivity.class);
                intent.putExtra("boardId", boardId);
                startActivity(intent);
            }
        });

        ibCommentInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDateTime = Calendar.getInstance().getTime();
                // 키값 : 날짜시간
                String commentId = "CM" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(currentDateTime);
                String commentDate = new SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime);
                String query = String.format(
                        "INSERT INTO %s VALUES ('%s', null, '%s', '%s', '%s', '%s');"
                        , COMMENT_TABLE_NAME, commentId, edtComment.getText().toString(), commentDate, boardId,"0000");
                Log.e("TAG",query);

                /* 리사이클러뷰 업데이트 */
                insertCommentDB.execSQL( query );
                commentHelper.rcyCommentSelect(boardId);
                rcyCommentAdapter.notifyDataSetChanged();

                edtComment.setText("");
                txtCommentCount.setText(String.valueOf(rcyCommentAdapter.getItemCount()));
            }
        });

        ibCommentRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentHelper.rcyCommentSelect(boardId);
                rcyCommentAdapter.notifyDataSetChanged();
                txtCommentCount.setText(String.valueOf(rcyCommentAdapter.getItemCount()));
            }
        });
    }
}