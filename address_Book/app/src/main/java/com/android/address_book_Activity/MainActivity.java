package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.Task.NetworkTask;
import com.android.address_book.R;
import com.android.address_book.SectionPageAdapter;
import com.google.android.material.textfield.TextInputLayout;


/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 로그인 화면                                     =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    private boolean saveLoginData;
    String urlAddr = null;
    EditText loginId;
    EditText loginPw;
    Button loginBtn, findIdBtn, findPwBtn, joinBtn;
    String useremail, userpw, macIP;
    String urlAddrLoginCheck = null;
    CheckBox savechb;
    int count = 0;
    private SharedPreferences appData;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    private Context mContext;
    private ViewPager mViewPager;
    SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);




        findIdBtn = findViewById(R.id.findId_btn);
        findPwBtn = findViewById(R.id.findPW_btn);
        findIdBtn.setOnClickListener(findClickListener);
        findPwBtn.setOnClickListener(findClickListener);

        joinBtn = findViewById(R.id.join_btn);
        loginBtn = findViewById(R.id.login_btn);
        loginId = findViewById(R.id.login_id);
        loginPw = findViewById(R.id.login_pw);
        savechb = (CheckBox) findViewById(R.id.save_chb);

        TextInputLayout inputLayoutPW = findViewById(R.id.InputLayoutPw_login);
        inputLayoutPW.setPasswordVisibilityToggleEnabled(true);

        if (saveLoginData) {
            loginId.setText(useremail);
            loginPw.setText(userpw);
            savechb.setChecked(saveLoginData);
        }



        macIP = "192.168.219.191";






        joinBtn.setOnClickListener(mClickListener);
        loginBtn.setOnClickListener(onClickListener);
        useremail = loginId.getText().toString();
        userpw = loginPw.getText().toString();

    }




    View.OnClickListener findClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.findId_btn:
                    Intent intent = new Intent(MainActivity.this, FindIDActivity.class);
                    startActivity(intent);
                    break;
                case R.id.findPW_btn:
                    Intent intent1 = new Intent(MainActivity.this, FindPWActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    };


    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, JoinActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            urlAddr = "http://" + macIP + ":8080/test/logincheck.jsp?";
            useremail = loginId.getText().toString();
            userpw = loginPw.getText().toString();

            urlAddr = urlAddr + "useremail=" + useremail + "&userpw=" + userpw;


            count = loginCount();

            Log.v("여기", "" + loginCount());
            Log.v("아이디", "login : " + useremail + userpw);

            if (count == 1) {
                save();
                //connectUpdateData();
                Toast.makeText(MainActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddressListActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "아이디와 비밀번호를 확인하세요!", Toast.LENGTH_SHORT).show();

            }


        }
    };


    private int loginCount() {
        try {
            NetworkTask networkTask = new NetworkTask(MainActivity.this, urlAddr, "loginCount");
            Object obj = networkTask.execute().get();

            count = (int) obj;
            Log.v("여기", "loginCount : " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


//    private String connectUpdateData() {
//
//        String result = null;
//        try {
//            NetworkTask updnetworkTask = new NetworkTask(MainActivity.this, urlAddr, "select");
//            ///////////////////////////////////////////////////////////////////////////////////////
//            // Date : 2020.12.24
//            //
//            // Description:
//            // - 수정 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
//            //
//            ///////////////////////////////////////////////////////////////////////////////////////
//
//            Object obj = updnetworkTask.execute().get();
//            result = (String) obj;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();
        Log.v(TAG, loginId.getText().toString().trim());
        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", savechb.isChecked());
        editor.putString("useremail", loginId.getText().toString().trim());
        editor.putString("userpw", loginPw.getText().toString().trim());
        editor.putString("macIP", macIP);



        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        useremail = appData.getString("useremail", "");
        userpw = appData.getString("userpw", "");


    }

    // 화면 touch 시 키보드 숨기
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }



}//---------------------------
