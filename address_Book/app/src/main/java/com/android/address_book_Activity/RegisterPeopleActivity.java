package com.android.address_book_Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.Task.CUDNetworkTask;
import com.android.Task.GroupNetworkTask;
import com.android.address_book.Group;
import com.android.address_book.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 연락처 등록 화면                                 =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class RegisterPeopleActivity extends AppCompatActivity {

    String TAG = "Register";
    // 화면 구성 변수
    ImageView registerImage;
    Button add_view, registerButton;
    ImageButton registerBookMarkButton, registerEmergencyPhoneNumber;
    EditText registerName, registerMainTelNo, registerAddPhoneNumber1, registerAddPhoneNumber2, registerAddPhoneNumber3, registerAddPhoneNumber4, registerEmail, registerComment;
    Spinner spinner = null;
    // 입력을 받아오는 변수
    String strRegisterName=null;
    String strRegisterMainTelNo=null;
    String strRegisterAddPhoneNumber1=null;
    String strRegisterAddPhoneNumber2=null;
    String strRegisterAddPhoneNumber3=null;
    String strRegisterAddPhoneNumber4=null;
    String strRegisterEmail= null;
    String strRelation = null;
    String strRegisterComment= null;
    // url 변수
    String macIP, email, urlAddPeople, urlGetNumber, urlAddPhoneNumber, urlAddstatus, urlAddRegister, urlGetGroup, url;
    // DB에서 받아오는 변수
    String peopleNo, peopleInsertResult, phoneInsert, statusInsert, registerInsert;
    // ArrayList & Adapter
    ArrayList<String> totalPhoneNo = new ArrayList<String>();       // 전화번호를 여러개 받아서 저장하기 위해
    ArrayList<Group> totalGroup;
    ArrayList<String> groupName;
    ArrayAdapter<String> spinnerAdapter = null;
    // Check 변수
    int bookMark = 0;
    int emergencyStatus = 0;
    int phoneInsertResult = 0;
    int addPhoneCheck = 0;
    int imageCheck=0;

    // 사진 올리고 내리기 위한 변수들
    private final int REQ_CODE_SELECT_IMAGE = 100;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    String imageName = null;
    private String f_ext = null;
    File tempSelectFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_people);

        // 화면 구성 아이디 받아오기
        add_view = findViewById(R.id.addTelNoButton);
        registerButton = findViewById(R.id.registerButton);
        registerImage = findViewById(R.id.registerImage);
        registerBookMarkButton = findViewById(R.id.registerBookMarkButton);
        registerEmergencyPhoneNumber = findViewById(R.id.registerEmergencyPhoneNumber);
        registerName = findViewById(R.id.registerName);
        registerMainTelNo = findViewById(R.id.registerMainTelNo);
        registerEmail = findViewById(R.id.registerEmail);
        registerComment = findViewById(R.id.registerComment);
        // 쉐어 변수 값 받아오기
        SharedPreferences sf = getSharedPreferences("appData", MODE_PRIVATE);
        macIP = sf.getString("macIP","");
        email = sf.getString("useremail","");
        // url 변수 값 받아오기
        url = "http://"+macIP+":8080/test/multipartRequest.jsp";
        urlAddPeople = "http://"+macIP+":8080/test/peopleInsert.jsp?";
        urlGetNumber = "http://"+macIP+":8080/test/getPeopleNo.jsp";
        urlAddstatus = "http://"+macIP+":8080/test/statusInsert.jsp?";
        urlAddRegister = "http://"+macIP+":8080/test/registerInsert.jsp?";
        urlGetGroup = "http://"+macIP+":8080/test/group_query_all.jsp?email="+email;

        // 클릭 이벤트 모음
        add_view.setOnClickListener(mClickListener);
        registerButton.setOnClickListener(mClickListener);
        registerImage.setOnClickListener(mClickListener);
        registerBookMarkButton.setOnClickListener(mClickListener);
        registerEmergencyPhoneNumber.setOnClickListener(mClickListener);
        // Thread 사용
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        // 스피너 구성
        totalGroup = new ArrayList<Group>();
        connectGroupGetData();
        groupName = new ArrayList<String>();
        groupName.add("가족");
        groupName.add("친구");
        groupName.add("회사");
        for(int i = 0 ; i < totalGroup.size();i++){
            groupName.add(totalGroup.get(i).getGroupName());
        }
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, groupName);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.group_spiner);
        spinner.setAdapter(spinnerAdapter);

    }
    // 클릭 이벤트 method
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 화면의 값 받아오기
            strRegisterName = registerName.getText().toString();
            strRegisterEmail = registerEmail.getText().toString();
            strRelation = spinner.getSelectedItem().toString();
            strRegisterComment = registerComment.getText().toString();

            switch (v.getId()){
                case R.id.addTelNoButton:
                    RegisterAddPhoneNumber n_layout = new RegisterAddPhoneNumber(getApplicationContext());
                    LinearLayout con = (LinearLayout)findViewById(R.id.add_PhoneNumber_layout);
                    con.addView(n_layout);
                    add_view.setVisibility(View.INVISIBLE);
                    registerAddPhoneNumber1 = findViewById(R.id.registerAddPhoneNumber1);
                    registerAddPhoneNumber2 = findViewById(R.id.registerAddPhoneNumber2);
                    registerAddPhoneNumber3 = findViewById(R.id.registerAddPhoneNumber3);
                    registerAddPhoneNumber4 = findViewById(R.id.registerAddPhoneNumber4);
                    addPhoneCheck =1;       // 전화번호 추가 버튼 클릭을 했는지 확인
                    break;

                case R.id.registerButton:
                    Log.v(TAG, "registerButton");
                    // 순서 1. 네트워크 연결 및 이미지 서버에 전송, 이미지 이름 저장
                    if(strRegisterName.length()==0){
                        Toast.makeText(RegisterPeopleActivity.this, "성함을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else if(registerMainTelNo.getText().toString().length()==0){
                        Toast.makeText(RegisterPeopleActivity.this, "최소 하나의 전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }else {

                    if(imageCheck==1){          // 겔러리 버튼 클릭했는지 확인
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                doMultiPartRequest();
                            }       // 서버에 올림
                        }).start();
                    }
                    Log.v(TAG, "image Name : "+ imageName);

                    // 순서 2. DB와 연결(NetworkTask)해서 정보 insert
                    urlAddPeople = urlAddPeople+"peoplename="+strRegisterName+"&peopleemail="+strRegisterEmail+"&peoplememo="+strRegisterComment+"&peopleimage="+imageName+"&peoplerelation="+strRelation;
                    connectInsertData();

                    // 순서 3. insert 되서 생성된 peopleno 가져오기;
                    connectGetData();

                    // 순서 4. peopleno랑 전화번호 정보 insert
                    strRegisterMainTelNo = registerMainTelNo.getText().toString();
                    totalPhoneNo.add(strRegisterMainTelNo);

                    if(addPhoneCheck == 1) {        // 전화번호 추가 버튼을 클릭 했을때 값 추가
                        if (registerAddPhoneNumber1.getText().toString().length() != 0) {
                            strRegisterAddPhoneNumber1 = registerAddPhoneNumber1.getText().toString();
                            totalPhoneNo.add(strRegisterAddPhoneNumber1);
                        }
                        if (registerAddPhoneNumber2.getText().toString().length() != 0) {
                            strRegisterAddPhoneNumber2 = registerAddPhoneNumber2.getText().toString();
                            totalPhoneNo.add(strRegisterAddPhoneNumber2);
                        }
                        if (registerAddPhoneNumber3.getText().toString().length() != 0) {
                            strRegisterAddPhoneNumber3 = registerAddPhoneNumber3.getText().toString();
                            totalPhoneNo.add(strRegisterAddPhoneNumber3);
                        }
                        if (registerAddPhoneNumber4.getText().toString().length() != 0) {
                            strRegisterAddPhoneNumber4 = registerAddPhoneNumber4.getText().toString();
                            totalPhoneNo.add(strRegisterAddPhoneNumber4);
                        }
                    }
                    // 여러개 전화번호 입력 받은 값들을 db에 넣어주기 위해
                    for (int i = 0; i<totalPhoneNo.size();i++){
                        urlAddPhoneNumber = "http://"+macIP+":8080/test/phoneInsert.jsp?";
                        Log.v(TAG, "TelNo insert : "+totalPhoneNo.get(i));
                        urlAddPhoneNumber = urlAddPhoneNumber+"people_peopleno="+peopleNo+"&totalPhoneNo="+totalPhoneNo.get(i);
                        connectInsertPhoneNo();
                        if(phoneInsert.equals("1")){
                            phoneInsertResult++;
                        }
                    }

                    // 순서 5. 좋아요, 긴급연락처 추가
                    urlAddstatus = urlAddstatus+"people_peopleno="+peopleNo+"&userinfo_useremail="+email+"&peopleemg="+emergencyStatus+"&peoplefavorite="+bookMark;
                    connectInsertStatus();

                    // 순서 6. register에 peopleno 등록
                    urlAddRegister = urlAddRegister+"userinfo_useremail="+email+"&people_peopleno="+peopleNo;
                    connectInsertRegister();

                    // 입력이 제대로 됐는지 확인
                    if(peopleInsertResult.equals("1")&&phoneInsertResult==totalPhoneNo.size()){
                        Toast.makeText(RegisterPeopleActivity.this, "입력이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterPeopleActivity.this, "입력에 실패하였습니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                    }
                    // 리스트로 돌아가기
                    Intent intent = new Intent(RegisterPeopleActivity.this, AddressListActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                    }

                case R.id.registerImage:
                    // 이미지 버튼 클릭했을 때 겔러리로 가기
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    break;

                case R.id.registerBookMarkButton:
                    // 즐겨찾기 버튼을 클릭했을 때 발생하는 이벤트
                    if(bookMark == 0){
                        bookMark = 1;
                        registerBookMarkButton.setImageResource(R.drawable.ic_favorite);
                        Toast.makeText(RegisterPeopleActivity.this, "즐겨찾기로 설정하셨습니다.", Toast.LENGTH_SHORT).show();
                    }else if (bookMark ==1){
                        bookMark = 0;
                        registerBookMarkButton.setImageResource(R.drawable.ic_nonfavorite);
                        Toast.makeText(RegisterPeopleActivity.this, "즐겨찾기를 해제하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.registerEmergencyPhoneNumber:
                    // 긴급연락처 버튼을 클릭했을 때 발생하는 이벤트
                    if(emergencyStatus == 0){
                        emergencyStatus = 1;
                        registerEmergencyPhoneNumber.setImageResource(R.drawable.ic_emg2);
                        Toast.makeText(RegisterPeopleActivity.this, "긴급연락처로 설정하셨습니다.", Toast.LENGTH_SHORT).show();
                    }else if (emergencyStatus ==1){
                        emergencyStatus = 0;
                        registerEmergencyPhoneNumber.setImageResource(R.drawable.ic_nonemg2);
                        Toast.makeText(RegisterPeopleActivity.this, "긴급연락처를 해제하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 겔러리 가기
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {                     // 겔러리에 들어왔는지 확인
                try {
                    imageCheck=1;                                       // 겔러리에 들어왔는지 자바에서 확인하기 위한 변수
                    img_path = getImagePathToUri(data.getData());       //이미지의 URI를 얻어 경로값으로 반환.(method 확인 필요)

                    //이미지를 비트맵형식으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                    registerImage.setImageBitmap(image_bitmap_copy);

                    // 파일 이름 및 경로 바꾸기(임시 저장)
                    String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                    imageName = date+"."+f_ext;
                    tempSelectFile = new File("/data/data/com.android.address_book/", imageName);       // 경로는 자기가 원하는 곳으로 바꿀 수 있음
                    OutputStream out = new FileOutputStream(tempSelectFile);
                    image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);                        // 지정 경로로 임시 파일 보내기

                    // 임시 파일 경로로 위의 img_path 재정의
                    img_path = "/data/data/com.android.address_book/"+imageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);             // 무엇을 선택했는지
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);

        // 확장자 명 저장
        f_ext = imgPath.substring(imgPath.length()-3, imgPath.length());

        return imgPath;
    }//end of getImagePathToUri()

    //파일 변환
    private void doMultiPartRequest() {

        File f = new File(img_path);

        DoActualRequest(f);
    }

    //서버 보내기
   private void DoActualRequest(File file) {
       OkHttpClient client = new OkHttpClient();
       // body 구성
       RequestBody body = new MultipartBody.Builder()
               .setType(MultipartBody.FORM)
               .addFormDataPart("image", file.getName(),
                       RequestBody.create(MediaType.parse("image/*"), file))
               .build();
       // 서버에 요청
       Request request = new Request.Builder()
               .url(url)
               .post(body)
               .build();
       try {
           Response response = client.newCall(request).execute();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }


   // DB와 연결하기 위한 method들
    private String connectInsertData() {
        try {
            CUDNetworkTask insnetworkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlAddPeople, "Register");
            Object object = insnetworkTask.execute().get();
            peopleInsertResult = (String) object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return peopleInsertResult;
    }
    private String connectGetData(){
        try {
            CUDNetworkTask networkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlGetNumber, "Register");
            Object obj = networkTask.execute().get();
            peopleNo = (String) obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return peopleNo;
    }
    private String connectInsertPhoneNo() {
        try {
            CUDNetworkTask insTelNonetworkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlAddPhoneNumber, "Register");
            Object object = insTelNonetworkTask.execute().get();
            phoneInsert =(String) object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneInsert;
    }
    private String connectInsertStatus() {
        try {
            CUDNetworkTask insTelNonetworkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlAddstatus, "Register");
            Object object = insTelNonetworkTask.execute().get();
            statusInsert =(String) object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusInsert;
    }
    private String connectInsertRegister(){
        try {
            CUDNetworkTask insTelNonetworkTask = new CUDNetworkTask(RegisterPeopleActivity.this, urlAddRegister, "Register");
            Object object = insTelNonetworkTask.execute().get();
            registerInsert =(String) object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registerInsert;
    }

    private ArrayList<Group> connectGroupGetData() {
        try {
            GroupNetworkTask groupNetworkTask = new GroupNetworkTask(RegisterPeopleActivity.this, urlGetGroup, "select");
            Object obj = groupNetworkTask.execute().get();
            totalGroup = (ArrayList<Group>) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalGroup;
    }
}
