package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.NetworkTask;
import com.android.address_book.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 My Page 변경 PW 입력 화면                                     =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/


public class MypagePWActivity02 extends AppCompatActivity {
    final static String TAG = "MypagePWActivity02";
    public static final String pattern1 = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$"; // 영문, 숫자, 특수문자

    EditText pw, pwCheck;
    TextView pwCheckMsg;
    String macIP, urlAddr, currentPW;
    String email;
    Matcher match;

    private int _beforeLenght = 0;
    private int _afterLenght = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mypage_pw02);

        Intent intent = getIntent();
        currentPW = intent.getStringExtra("pw");
//        macIP = intent.getStringExtra("macIP");
        SharedPreferences sf = getSharedPreferences("appData", MODE_PRIVATE);
        email = sf.getString("useremail","");
        macIP = sf.getString("macIP","");
        urlAddr = "http://" + macIP + ":8080/test/userInfoUpdate.jsp?email=" + email;

        TextInputLayout inputLayoutPW = findViewById(R.id.LayoutnewPw_mypagePW2);
        TextInputLayout inputLayoutPWCheck = findViewById(R.id.LayoutnewPwcheck_mypagePW2);

        inputLayoutPW.setPasswordVisibilityToggleEnabled(true);
        inputLayoutPWCheck.setPasswordVisibilityToggleEnabled(true);

        pw = findViewById(R.id.newPW_changePW2);
        pwCheck = findViewById(R.id.newPWcheck_changePW2);
        pwCheckMsg = findViewById(R.id.tv_fieldCheck_changePW2);

        findViewById(R.id.backBtn_changePW2).setOnClickListener(mClickListener);
        findViewById(R.id.btnNextChangePW_changePW2).setOnClickListener(mClickListener);

        pw.addTextChangedListener(changeListener);
        pwCheck.addTextChangedListener(changeListener1);

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                // backButton 클릭 시 화면 JoinActivity 종료
                case R.id.backBtn_changePW2:
                    finish();
                    break;

                // 완료 버튼 클릭 시
                case R.id.btnNextChangePW_changePW2:
                    checkField();
                    break;
            }
        }
    };

    // pw 입력란 text 변경 시 listener
    TextWatcher changeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // pw 입력 시
            String pwCheck =pw.getText().toString().trim();
            Boolean check = pwdRegularExpressionChk(pwCheck);

            if(pwCheck.length() == 0){
                pw.setError(null);

            } else {
                if (check == false) {
                    pw.setError("비밀번호는 영문, 특수문자 포함하여 최소 8자 이상 입력해주세요.");
                }
            }
        }
    };

    // pw 입력란 text 변경 시 listener
    TextWatcher changeListener1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // pwcheck 입력 시 일치 여부 message
            if(pwCheck.getText().toString().trim().length() != 0){
                if((pwCheck.getText().toString().trim()).equals(pw.getText().toString().trim())){
                    pwCheckMsg.setTextColor(getResources().getColor(R.color.blue));
                    pwCheckMsg.setText("비밀번호 일치");

                } else {
                    pwCheckMsg.setTextColor(getResources().getColor(R.color.red));
                    pwCheckMsg.setText("비밀번호 불일치");
                }
            }
        }
    };

    // 입력란 field check
    private void checkField() {
        String userPW = pw.getText().toString().trim();
        String userPWCheck = pwCheck.getText().toString().trim();
        pwCheckMsg.setText("");

        if(userPW.length() == 0){
            pwCheckMsg.setText("새로운 비밀번호를 입력해주세요.");

        } else if(userPW.length() != 0){
            if (currentPW.equals(userPW)) {
                pwCheckMsg.setText("기존 비밀번호와 동일합니다.");

            } else {
                Boolean check = pwdRegularExpressionChk(userPW);

                if (check == false) {
                    pwCheckMsg.setText("비밀번호를 영문, 특수문자 포함하여 \n최소 8자 이상 입력해주세요.");

                } else {
                    if (userPWCheck.length() == 0) {
                        pwCheckMsg.setText("새로운 비밀번호 확인을 입력해주세요.");

                    } else if ((pwCheck.getText().toString().trim()).equals(pw.getText().toString().trim())) {
                        updateUser(userPW);

                    } else {
                        pwCheckMsg.setText("비밀번호가 일치하지 않습니다. \n다시 확인해주세요.");
                        pwCheck.setText("");
                        Toast.makeText(MypagePWActivity02.this, "비밀번호가 일치하지 않습니다. \n다시 확인해주세요.", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        }


    }

    // user pw 수 data 송부
    private void updateUser(String userPW) {
        String urlAddr1 = "";
        urlAddr1 = urlAddr + "&pw=" + userPW;

        Log.v(TAG, urlAddr1);
        String result = connectUpdateData(urlAddr1);

        if (result.equals("1")) {
            Toast.makeText(MypagePWActivity02.this, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            Toast.makeText(MypagePWActivity02.this, "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();

        }
    }

    //connection update
    private String connectUpdateData(String urlAddr){
        String result = null;

        try{
            NetworkTask updateNetworkTask = new NetworkTask(MypagePWActivity02.this, urlAddr, "update");
            Object obj = updateNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e){
            e.printStackTrace();

        }
        return result;
    }

    // 비밀번호 영/숫/특 포함 설정
    public boolean pwdRegularExpressionChk(String newPwd){
        boolean chk = false;  // 특수문자, 영문, 숫자 조합 (8~10 자리)
        match = Pattern.compile(pattern1).matcher(newPwd);
        if (match.find()) {
            chk = true;
        }
        return chk;
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


}