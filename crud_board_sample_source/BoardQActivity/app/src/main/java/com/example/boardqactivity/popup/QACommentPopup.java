package com.example.boardqactivity.popup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.boardqactivity.R;

import static com.example.boardqactivity.GlobalVar.*;

public class QACommentPopup extends Activity {

    Button btnQacommentInput;
    EditText edtQacomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_qacomment);

        btnQacommentInput = (Button)findViewById(R.id.btn_qacomment_input);
        edtQacomment = (EditText)findViewById(R.id.edt_qacomment);

        Intent intent = getIntent();
        String myqaId = intent.getStringExtra("myqaId");

        btnQacommentInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uQuery = String.format(
                        "UPDATE %s SET %s = '%s' where %s = '%s'"
                        , MYQA_TABLE_NAME, MYQA_KEY_COMMENT, edtQacomment.getText().toString(), MYQA_KEY_ID, myqaId);
                Log.e("TAG",uQuery);

                insertQuestionDB.execSQL( uQuery );
                myQAHelper.rcyMyQASelect();
                rcyMyQAAdapter.notifyDataSetChanged();

                insertIntent = false;
                finish();
            }
        });
    }
}