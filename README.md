# 안드로이드 스튜디오를 이용한 주소록 앱






## 사소한(사람들의 소중한 주소록)
제작자 : 김대환, 박경미, 박인영, 송예진, 유민규




### G-Email 인증 시 설정
 1. libs 폴더에 아래 3개 jar 파일 추가
    1. activation.jar
    2. additionnal.jar
    3. mail.jar
   -> Gmail 라이브러리 폴더에서 다운로드

 2. FindPWActivity에서 사용할 Gmail 및 비밀번호 기입
 3. 사용할 Gmail 내 보안 > 보안 수준이 낮은 앱의 액세스 허용 설정
 


### build.gradle에 필요한 라이브러리

dependencies {

    //이메일 인증 보내기 위한 라이브러리 (gmail 사용)
    implementation files('lib/activation.jar')
    implementation files('lib/additionnal.jar')
    implementation files('lib/mail.jar')
    
    //사진을 서버에 올리기 위한 라이브러리
    implementation 'com.squareup.okhttp3:okhttp:4.10.0-RC1'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'me.relex:circleindicator:2.1.4'
    
}






### Manifest에 필요한 권한들

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.SEND_SMS" />
 




### Ip 변경

안드로이드 스튜디오를 통해 address_Book을 실행시키면 app/java/com.android/address_book_Activity/MainActivity 





### Tomcat을 연결하기 위한 XML

안드로이드 스튜디오를 통해 address_Book을 실행시키면 app/res/xml/network_security_config.jsp 를 통해 Tomcat과 연결을 할 수 있다.






### DB(MYSQL) 연결

MySQL Connector Download Link: [MySQL Connector][Connector]

[Connector]: https://dev.mysql.com/downloads/connector/j/8.0.html






### JSP 폴더 내 자료

JSP 폴더 내의 자료는 톰켓 서버 경로(/webapps/ROOT/test) 안에 'jsp파일'들을 넣어준다.






### JSP와 DB를 연결하기 위한 JSP내 사용자 환경에 맞는 소스 변경 요소들

String url_mysql = "jdbc:mysql://localhost/___데이터베이스 스키마 이름___?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";

String id_mysql = "**아이디**";

String pw_mysql = "**암호**";






### DB 구성요소
#### 스키마 이름 : address
|people|phone|register|relation|statuspeople|userinfo|
|------|------|------|------|------|------|
|peopleno|phoneno|userinfo_useremail|relationno|people_peopleno|useremail|
|peoplename|people_peopleno|registerdeletedate|userinfo_useremail|userinfo_useremail|username|
|peopleemail|phonetel|people_peopleno|relationname|peopleemg|userpw|
|peoplerelation||||peoplefavorite|userphone|
|peoplememo|||||userdeletedate|
|peopleimage|||||userimage|






### 구현 기능들
* 로그인 정보 저장 기능
* 원하는 그룹별 묶어 관리
* 핸드폰 내의 사진을 핸드폰 내에 임시파일로 저장하고 tomcat 서버에 올림
* 긴급 연락처, 즐겨찾는 연락처 분리 사용 가능
* Email로 인증번호 발송하여 본인 인증 가능
* 입력한 휴대폰 번호로 인증번호를 발송하여 본인 인증 
