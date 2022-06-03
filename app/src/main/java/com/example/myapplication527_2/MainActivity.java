package com.example.myapplication527_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btnCall;
    EditText edtCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 이 함수는 액티비티가 실행될때 호출되는 함수 입니다.
         * 인텐트를 DeloyedMessageService 객체로 넘겨줍니다.
         * @param view 뷰 클래스의 객체를 인자로 넘겨받습니다.
         * @return void 타입 입니다.
         * @warning 주의사항 없습니다.
         * @see DeloyedMessageService
         *
         *
         */






        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CALL_LOG},MODE_PRIVATE);
        btnCall = (Button) findViewById(R.id.btnCall);
        edtCall = (EditText) findViewById(R.id.edtCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtCall.setText(getCallHistory());
            }
        });
    }
    public String getCallHistory(){
        String[] callSet = new String[]{CallLog.Calls.DATE,
        CallLog.Calls.TYPE, CallLog.Calls.NUMBER,
        CallLog.Calls.DURATION};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                callSet,null,null,null);
        if (c == null)
            return "통화기록 없음";

        StringBuffer callBuff = new StringBuffer();
        callBuff.append("\n날짜: 구분: 전화번호: 통화시간\n\n");
        c.moveToFirst();
        do {
            long callDate = c.getLong(0);
            SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM=dd");
            String date_str = datePattern.format(new Date(callDate));
            callBuff.append(date_str + ":");
            if (c.getInt(1) == CallLog.Calls.INCOMING_TYPE)
                callBuff.append("착신 : ");
            else
                callBuff.append("발신: ");
            callBuff.append(c.getString(2)+":");
            callBuff.append(c.getString(3)+"초\n");
        } while (c.moveToNext());
        c.close();
        return callBuff.toString();
    }
}