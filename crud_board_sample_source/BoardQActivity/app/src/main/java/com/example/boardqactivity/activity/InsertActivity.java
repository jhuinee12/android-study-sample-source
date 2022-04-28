package com.example.boardqactivity.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.boardqactivity.R;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.boardqactivity.GlobalVar.*;

public class InsertActivity extends AppCompatActivity {

    public static Button btnBoardInput;
    public static ImageView ivInsertImage;
    public static EditText edtBoardTitle;
    public static EditText edtBoardDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Date currentDateTime = Calendar.getInstance().getTime();

        SQLiteDatabase insertDB = boardHelper.getWritableDatabase();

        edtBoardTitle = findViewById(R.id.edt_board_title);
        edtBoardDesc = findViewById(R.id.edt_board_desc);
        btnBoardInput = findViewById(R.id.btn_board_input);
        ivInsertImage = findViewById(R.id.iv_insert_image);

        // 이미지 갤러리 호출해 추가하기
        ivInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //기기 기본 갤러리 접근
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //구글 갤러리 접근
                // intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,101);

            }
        });


        // 게시글 수정하기 버튼으로 인텐트 되었을 때
        if (updateIntent) {
            Intent intent = getIntent();
            String boardId = intent.getStringExtra("boardId");

            boardHelper.rcyReaderSelect(boardId);

            edtBoardTitle.setText(BoardReaderList.get(0).getBoardDataTitle());
            edtBoardDesc.setText(BoardReaderList.get(0).getBoardDataDesc());

            btnBoardInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String uDate = new SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime);
                    String uQuery = String.format(
                            "UPDATE %s SET %s = '%s', %s = '%s', %s = '%s' where %s = '%s'"
                            , BOARD_TABLE_NAME
                            , BOARD_KEY_TITLE, edtBoardTitle.getText().toString()
                            , BOARD_KEY_DESC, edtBoardDesc.getText().toString()
                            , BOARD_KEY_DATE, uDate
                            , BOARD_KEY_ID, boardId);
                    Log.e("TAG",uQuery);

                    insertDB.execSQL( uQuery );
                    boardHelper.rcyBoardSelect();
                    rcyBoardAdapter.notifyDataSetChanged();

                    updateIntent = false;
                    finish();
                }
            });
        }
        // 게시글 작성으로 인텐트 되었을 때
        else {
            btnBoardInput.setOnClickListener(v -> {
                // 키값 : 날짜시간
                String id = "BO" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(currentDateTime);
                String iDate = new SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime);
                String query = String.format(
                        "INSERT INTO %s VALUES ('%s', '%s', '%s', '%s' , '%s', '%s');"
                        , BOARD_TABLE_NAME, id, boardImage, edtBoardTitle.getText().toString(), edtBoardDesc.getText().toString(), "name", iDate);
                Log.e("TAG",query);

                /* 리사이클러뷰 업데이트 */
                insertDB.execSQL( query );
                boardHelper.rcyBoardSelect();
                rcyBoardAdapter.notifyDataSetChanged();

                finish();
            });
        }
    }

    //권한에 대한 응답이 있을때 작동하는 함수
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //권한을 허용 했을 경우 if(requestCode == 1){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int length = permissions.length;
        for (int i = 0; i < length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                // 동의
                Log.d("MainActivity","권한 허용 : " + permissions[i]);
            }
        }
    }

    public void checkSelfPermission() {
        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                boardImage = BitmapFactory.decodeStream(is);
                is.close();
                ivInsertImage.setImageBitmap(boardImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 101 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
        }
    }

}