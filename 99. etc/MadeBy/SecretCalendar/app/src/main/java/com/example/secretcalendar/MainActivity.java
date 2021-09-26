package com.example.secretcalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    CalendarView calendar;
    Button bt_add, bt_money;
    EditText ed_title;
    static TextView tv_date, tx_total;
    ListView ls_todo;
    private String date=getToday_yM(), date2=getToday_d();

    final static String KEY_ID = "_id";
    final static String KEY_TITLE = "title";
    final static String TABLE_NAME = "MyScheduleList";
    final static String KEY_DATE = "date";
    final static String KEY_DATE2 = "date2";
    //    public static String View_DATE = getToday_date();
    public static String View_DATE = getToday_yM();
    public static String View_DATE2 = getToday_d();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        setTitle("Checklist Book");

        calendar = (CalendarView) findViewById(R.id.calendarView);
        ed_title = (EditText) findViewById(R.id.edt_Title);
        tv_date = (TextView) findViewById(R.id.txt_Date);
        tx_total = (TextView) findViewById(R.id.txt_total);
        bt_add = (Button) findViewById(R.id.btn_Add);
        bt_money = (Button) findViewById(R.id.btn_Money);
        ls_todo = (ListView) findViewById(R.id.lst_todo);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("WrongConstant")
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //선택 날짜 표시
                date = year + "/" + (month+1);
                date2 = String.valueOf(dayOfMonth);
                Log.d(TAG, "onSelectDayChange: date : "+date+"/"+date2);

                tv_date.setVisibility(View.VISIBLE);
                tv_date.setText(date+"/"+date2);
                ed_title.setVisibility(View.VISIBLE); // EditText 보이기
                bt_add.setVisibility(View.VISIBLE); // 추가버튼 보이기
                ls_todo.setVisibility(View.VISIBLE); // 리스트뷰 보이기

                ed_title.setText(""); //EditText에 공백값 넣기

                // 추가 버튼 클릭
                bt_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });

        // 가계부 버튼 클릭
        bt_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 리스트뷰 삭제
        ls_todo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("삭제");
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();

                return true;
            }
        });
    }

    static public String getToday_yM(){
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy/M", Locale.KOREA);
        Date currentTime = new Date();
        String Today_day = mSimpleDateFormat.format(currentTime).toString();
        return Today_day;
    }

    static public String getToday_d(){
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("d", Locale.KOREA);
        Date currentTime = new Date();
        String Today_day = mSimpleDateFormat.format(currentTime).toString();
        return Today_day;
    }

    static public String getThis_time(){
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HHMMSS", Locale.KOREA);
        Date currentTime = new Date();
        String This_time = mSimpleDateFormat.format(currentTime).toString();
        return This_time;
    }

}
