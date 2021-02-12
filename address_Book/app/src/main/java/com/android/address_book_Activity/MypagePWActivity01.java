package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.NetworkTask;
import com.android.address_book.R;
import com.android.address_book.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 My Page 기존 PW 입력 화면                                     =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class MypagePWActivity01 extends AppCompatActivity {

    final static String TAG = "MypagePWActivity01";

    EditText currentPW;
    String macIP, urlAddr;
    TextView field;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mypage_pw01);

        Log.v(TAG, "onCreate");
        currentPW = findViewById(R.id.currentPW_changePW);
        field = findViewById(R.id.tv_fieldCheck_changePW);
        TextInputLayout inputLayoutPW = findViewById(R.id.currentLayoutPw_mypagePW);
        inputLayoutPW.setPasswordVisibilityToggleEnabled(true);

        SharedPreferences sf = getSharedPreferences("appData", MODE_PRIVATE);
        macIP = sf.getString("macIP","");
        urlAddr = "http://" + macIP + ":8080/test/user_query_all.jsp";

        findViewById(R.id.backBtn_changePW).setOnClickListener(mClickListener);
        findViewById(R.id.btnNextChangePW_changePW).setOnClickListener(mClickListener);

        currentPW.addTextChangedListener(changeListener);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.backBtn_changePW:
                    finish();
                    break;

                case R.id.btnNextChangePW_changePW:
                    checkPW();
                    break;
            }
        }
    };


    // 비밀번호 일치 여부 확인
    private void checkPW(){
        // 로그인 시 아이디값 받아오기
        SharedPreferences sf = getSharedPreferences("appData", MODE_PRIVATE);
        String email = sf.getString("useremail","");
        String pw = currentPW.getText().toString().trim();
        int count = 0 ;

        if(pw.length() == 0){
            field.setText("비밀번호를 입력해주세요.");

        } else {

            ArrayList<User> users = connectSelectData(urlAddr);

            for (int i=0; i<users.size(); i++){
                // 아이디 일치 여부 확인 하기
                if(pw.equals(users.get(i).getUserPW()) && email.equals(users.get(i).getUserEmail())){
                    count++;
                }

                Log.v(TAG, Integer.toString(count));
            }

            if (count == 0) {
                field.setText("비밀번호가 일치하지 않습니다.");
                Toast.makeText(MypagePWActivity01.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

            } else {
                field.setText("");
                Toast.makeText(MypagePWActivity01.this, "비밀번호 변경을 위해 다음 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MypagePWActivity01.this, MypagePWActivity02.class);
                intent.putExtra("pw", pw);
                startActivity(intent);
                finish();
            }

        }

    }

    // connection select
    private ArrayList<User> connectSelectData(String urlAddr){
        ArrayList<User> users = null;

        try {
            NetworkTask networkTask = new NetworkTask(MypagePWActivity01.this, urlAddr, "select");
            Object obj = networkTask.execute().get();
            users = (ArrayList<User>) obj;

        } catch (Exception e){
            e.printStackTrace();
        }

        return users;
    }


    TextWatcher changeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            field.setText("");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(currentPW.getText().toString().trim().length()==0){
                field.setText("");
            }
        }
    };

}