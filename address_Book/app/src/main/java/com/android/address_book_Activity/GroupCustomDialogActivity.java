package com.android.address_book_Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Task.GroupNetworkTask;
import com.android.Task.NetworkTask;
import com.android.address_book.Group;
import com.android.address_book.R;
import com.android.address_book.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupCustomDialogActivity{

    private Context context;
    private String urlAddr, macIP;
    ArrayList<Group> group = null;


    public GroupCustomDialogActivity(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(String email, String macIP) {

//        email = "qkr@naver.com";
//        macIP = "192.168.219.154";
        urlAddr = "http://" + macIP + ":8080/test/";

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_group_custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText message = (EditText) dlg.findViewById(R.id.groupAdd_menu);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.

                if(message.getText().toString().trim().length() == 0){
                    Toast.makeText(context, "그룹을 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else {
                    int count = 0;
                    String groupName = message.getText().toString();

                    String urlAddr1 = "";
                    urlAddr1 = urlAddr + "group_query_all.jsp?email=" + email;

                    ArrayList<Group> result = connectSelectData(urlAddr1);

                    for(int i =0; i<result.size(); i++){
                        if(groupName.equals(result.get(i).getGroupName())){
                            count ++;
                        }
                    }

                    if(count == 0) {
                       insertGroup(groupName, email);
//                        ((AddressListActivity)context).
                        // 커스텀 다이얼로그를 종료한다.

                        dlg.dismiss();
                    } else {
                        Toast.makeText(context, "동일한 그룹이 존재합니다.", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }

    // group insert action
    private void insertGroup(String relationname, String email) {
        String urlAddr2 = "";
        urlAddr2 = urlAddr + "relationInsert.jsp?email=" + email + "&relationname=" + relationname;

        String result = connectInsertData(urlAddr2);

        if (result.equals("1")) {
            Toast.makeText(context, "\"" +  relationname + "\" 그룹 추가되었습니다.", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "\"" +  relationname + "\" 입력 실패하였습니다.", Toast.LENGTH_SHORT).show();

        }
    }

    //connection Insert
    private String connectInsertData(String urlAddr){
        String result = null;

        try{
            GroupNetworkTask insertNetworkTask = new GroupNetworkTask(context, urlAddr, "insert");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e){
            e.printStackTrace();

        }
        return result;
    }

    //connection Select
    private ArrayList<Group> connectSelectData(String urlAddr){
        ArrayList<Group> result = null;

        try{
            GroupNetworkTask selectNetworkTask = new GroupNetworkTask(context, urlAddr, "select");
            Object obj = selectNetworkTask.execute().get();
            result = (ArrayList<Group>) obj;

        } catch (Exception e){
            e.printStackTrace();

        }
        return result;
    }

}