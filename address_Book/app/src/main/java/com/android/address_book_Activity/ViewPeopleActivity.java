package com.android.address_book_Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.CUDNetworkTask;
import com.android.Task.NetworkTask;
import com.android.Task.PeopleNetworkTask;
import com.android.Task.SQLite;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

 /*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 연락처 보기 화면                                 =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class ViewPeopleActivity extends Activity {

    final static String TAG = "ViewPeopleActivity";
    String urlAddr, urlAddr2 = null;
    String macIP; // MainActivity에서 넘겨줌
    String useremail;
    String peoplename;
    String peopleemail;
    String peoplerelation;
    String peoplememo;
    String peopleimage;
    ArrayList<String> phonetel;
    String peopleno;
    ArrayList<Integer> phoneno;
    String peoplefavorite;
    String peopleemg;
    ArrayList<People> data = null;
    int result;
    Button btn_edit_addressView = null;
    Button backToList;
    ScrollView scrollview_people;
    WebView iv_viewPeople;
    ImageButton btn_view_favorite, btn_view_emergency, btn_view_dial, btn_view_message;
    TextView view_name, view_phone, view_email, view_relation, view_memo;
    PeopleAdapter adapter;
    ArrayList<People> members;
    String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_people);

        // intent
        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        peopleno = intent.getStringExtra("peopleno");
        useremail = intent.getStringExtra("useremail");

        // url 세팅
        urlAddr = "http://" + macIP + ":8080/test/";
        urlImage = urlAddr;
        urlAddr2 = urlAddr + "people_query_selectModify.jsp?email="+useremail+"&peopleno=" + peopleno;

        // Task 연결
        members = connectSelectedData(urlAddr2);

        // get Data // set Text
        scrollview_people = findViewById(R.id.scrollview_people);
        //phoneno = intent.getIntExtra("phoneno", 0);
        //urlAddr = "http://" + IP + ":8080/address/people_query_all.jsp";
//        peoplename = intent.getStringExtra("peoplename");
//        peopleemail = intent.getStringExtra("peopleemail");
//        useremail = intent.getStringExtra("useremail");
//        peoplerelation = intent.getStringExtra("peoplerelation");
//        peoplememo = intent.getStringExtra("peoplememo");
//        peopleimage = intent.getStringExtra("peopleimage");
//        phonetel = intent.getStringExtra("phonetel");
//        peoplefavorite = intent.getIntExtra("peoplefavorite", 0);
//        peopleemg = intent.getIntExtra("peopleemg", 0);

        // Web View에 이미지 띄움
        iv_viewPeople=findViewById(R.id.iv_viewPeople);
        iv_viewPeople.getSettings().setJavaScriptEnabled(true);
        imageCheck();
        WebSettings webSettings = iv_viewPeople.getSettings();
//
        // WebView 세팅
        webSettings.setUseWideViewPort(true);       // wide viewport를 사용하도록 설정
        webSettings.setLoadWithOverviewMode(true);  // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        //iv_viewPeople.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        iv_viewPeople.setBackgroundColor(0); //배경색
        iv_viewPeople.setHorizontalScrollBarEnabled(false); //가로 스크롤
        iv_viewPeople.setVerticalScrollBarEnabled(false);   //세로 스크롤
        iv_viewPeople.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 스크롤 노출 타입
        iv_viewPeople.setScrollbarFadingEnabled(false);

        // 웹뷰 멀티 터치 가능하게 (줌기능)
        webSettings.setBuiltInZoomControls(false);   // 줌 아이콘 사용
        webSettings.setSupportZoom(false);



        // DB에서 받아오기
        peoplename = members.get(0).getName();
        view_name = findViewById(R.id.view_name);
        view_name.setText(peoplename);
      //  view_name.setText(Html.fromHtml("<u>"+ peoplename +"</u>"));

        phonetel = members.get(0).getTel();
        view_phone = findViewById(R.id.view_phone);
        view_phone.setText(phonetel.get(0));

        peopleemail = members.get(0).getEmail();
        view_email = findViewById(R.id.view_email);
        view_email.setText(peopleemail);
       // view_email.setText(Html.fromHtml("<u>"+ peopleemail +"</u>"));

        peoplerelation = members.get(0).getRelation();
        view_relation = findViewById(R.id.view_relation);
        view_relation.setText(peoplerelation);

        peoplememo = members.get(0).getMemo();
        view_memo = findViewById(R.id.view_memo);
        view_memo.setText(peoplememo);


        // button 연결
        backToList = findViewById(R.id.btn_backToList);
        btn_edit_addressView = findViewById(R.id.btn_edit_addressView);
        btn_view_dial = findViewById(R.id.btn_view_dial);
        btn_view_message = findViewById(R.id.btn_view_message);
        btn_view_favorite = findViewById(R.id.btn_view_favorite);
        btn_view_emergency = findViewById(R.id.btn_view_emergency);

        btn_view_dial.setImageResource(R.drawable.ic_dial);
        btn_view_message.setImageResource(R.drawable.ic_message);

        // 클릭리스너
        backToList.setOnClickListener(OnclickListener);
        btn_edit_addressView.setOnClickListener(OnclickListener);
        btn_view_dial.setOnClickListener(OnclickListener);
        btn_view_message.setOnClickListener(OnclickListener);
        btn_view_favorite.setOnClickListener(OnclickListener);
        btn_view_emergency.setOnClickListener(OnclickListener);

        // emergency&favorite 값 받아와서 띄워주기
        phoneno = members.get(0).getPhoneno();
        peoplefavorite = members.get(0).getFavorite();
        peopleemg = members.get(0).getEmergency();
        onCreateFavoriteCheck();


    } // onCreate 끝 -----------------------------------------------------------------------

    View.OnClickListener OnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.btn_backToList: // List로 이동
                    finish();
//                    intent = new Intent(ViewPeopleActivity.this, AddressListActivity.class); //화면 이동시켜주기
//                    intent.putExtra("IP", IP); //값 넘겨주기
//                    startActivity(intent); //이동시킨 화면 시작
                    break;
                case R.id.btn_edit_addressView: // 연락처 수정/삭제 페이지로 이동
                    intent = new Intent(ViewPeopleActivity.this, ModifyPeopleActivity.class); //화면 이동시켜주기
                    intent.putExtra("macIP", macIP); //값 넘겨주기

                    // peopleno & phoneno 값 넘겨주기
                    intent.putExtra("peopleno", peopleno); //값 넘겨주기
                    intent.putExtra("useremail", useremail); //값 넘겨주기
                  //  intent.putExtra("phoneno", phoneno); //값 넘겨주기
//                    intent.putExtra("peoplename", peoplename); //값 넘겨주기
//                    intent.putExtra("peopleemail", peopleemail); //값 넘겨주기
//                    intent.putExtra("phonetel", phonetel); //값 넘겨주기
//                    intent.putExtra("peoplememo", peoplememo); //값 넘겨주기

                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_view_dial: // 메인 phoneNumber 기준 전화로 이동
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phonetel));
                    startActivity(intent);
                    break;
                case R.id.btn_view_message: // 메인 phoneNumber 기준 문자로 이동
                    Uri uri = Uri.parse("smsto:" + phonetel); // 상대방 번호 연결 → 값 받아서 연결 추가
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", "The SMS text");
                    startActivity(it);
                    break;
                case R.id.btn_view_favorite: // 즐겨찾기 추가/해제
                    favoriteCheck();
                    break;
                case R.id.btn_view_emergency: // 긴급연락처 추가/해제
                    emergencyCheck();
                    break;
            }
        }
    };

    // 즐겨찾기 & emergency 1인지 0인지 판단
    public void onCreateFavoriteCheck() {
        if (peoplefavorite.equals("0")) {
            btn_view_favorite.setImageResource(R.drawable.ic_nonfavorite);
        } else if(peoplefavorite.equals("1")) {
            btn_view_favorite.setImageResource(R.drawable.ic_favorite);
        }
        if (peopleemg.equals("0")) {
            btn_view_emergency.setImageResource(R.drawable.ic_nonemg2);
        } else if(peopleemg.equals("1")) {
            btn_view_emergency.setImageResource(R.drawable.ic_emg2);
        }
    }


    // 즐겨찾기 1인지 0인지 판단
    public void favoriteCheck() {

         String urlAddr1 = "";

        if (peoplefavorite.equals("0")) { // 0이라면 1로 세팅
            urlAddr1 = urlAddr + "people_query_Favorite.jsp?peoplefavorite=1&peopleno=" + peopleno;
            connectCheckData(urlAddr1);
            peoplefavorite = "1";
            btn_view_favorite.setImageResource(R.drawable.ic_favorite);

            Toast.makeText(ViewPeopleActivity.this, peoplename + "님이 즐겨찾기에 등록되었습니다.", Toast.LENGTH_SHORT).show();

        } else if(peoplefavorite.equals("1")) { // 이미 있다면 0으로 세팅
            urlAddr1 = urlAddr + "people_query_Favorite.jsp?peoplefavorite=0&peopleno=" + peopleno;
            connectCheckData(urlAddr1);
            peoplefavorite = "0";
            btn_view_favorite.setImageResource(R.drawable.ic_nonfavorite);

            Toast.makeText(ViewPeopleActivity.this, peoplename + "님이 즐겨찾기에서 해제되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

        //connection FavoriteCheck Data
        private void connectCheckData (String urlAddr1){
            String result = null;
            try {
                CUDNetworkTask insertNetworkTask = new CUDNetworkTask(ViewPeopleActivity.this, urlAddr1, "favoriteCount");
                insertNetworkTask.execute().get();
//                result = (String) obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    // 긴급연락처 1인지 0인지 판단
    public void emergencyCheck() {
        String urlAddr1 = "";

        if (peopleemg.equals("0")) { // 0이라면 1로 세팅
            urlAddr1 = urlAddr + "people_query_Emergency.jsp?peopleemg=1&peopleno=" + peopleno;
            peopleemg = "1";
            btn_view_emergency.setImageResource(R.drawable.ic_emg2);
            connectCheckData(urlAddr1);

            Toast.makeText(ViewPeopleActivity.this, peoplename + "이 긴급연락처에 등록되었습니다.", Toast.LENGTH_SHORT).show();

        } else if(peopleemg.equals("1")) { // 이미 있다면 0으로 세팅
            urlAddr1 = urlAddr + "people_query_Emergency.jsp?peopleemg=0&peopleno=" + peopleno;
            connectCheckData(urlAddr1);
            peopleemg = "0";
            btn_view_emergency.setImageResource(R.drawable.ic_nonemg2);

            Toast.makeText(ViewPeopleActivity.this, peoplename + "이 긴급연락처에서 해제되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    // 이미지 불러오기
    public void imageCheck() {

        if (members.get(0).getImage() == null) { // DB에 이미지 없음
            urlImage = urlImage+"ic_defaultpeople.jpg";
            iv_viewPeople.loadUrl(urlImage);
            iv_viewPeople.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            iv_viewPeople.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용

        } else if(members.get(0).getImage() != null) { // DB에 이미지 있을 때
            urlImage = urlImage + members.get(0).getImage();
            iv_viewPeople.loadUrl(urlImage);
            iv_viewPeople.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            iv_viewPeople.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
        }
    }
    public static class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    // obj members 가져오기
    private ArrayList<People> connectSelectedData(String urlAddr2) {

        try {
            PeopleNetworkTask peopleNetworkTask = new PeopleNetworkTask(ViewPeopleActivity.this, urlAddr2);

            Object obj = peopleNetworkTask.execute().get();

            // members에 obj를 줄거야! type은 Arraylist!
            members = (ArrayList<People>) obj;

        } catch (Exception e){
            e.printStackTrace();
        }
        return members;
    }

} // 끝 ------------------------------------------------------------------------------------