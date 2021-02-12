package com.android.address_book_Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.Task.GroupNetworkTask;
import com.android.Task.PeopleNetworkTask;
import com.android.address_book.Group;
import com.android.address_book.GroupAdapter;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FirstFragment extends Fragment {

    final static String TAG = "First";
    String urlAddrBase = null;
    String urlAddr1 = null;
    String urlAddr2 = null;
    ArrayList<People> members;
    ArrayList<Group> groups;
    PeopleAdapter adapter;
    GroupAdapter groupAdapter;
    ListView listView, groupList;
    String macIP;
    String email;
    TextView textView;
    String peopleNum;
    ArrayList<People> searchArr;
    EditText search_EdT;
    WebView webView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment는 Activity가 아니기때문에 리턴값과 레이아웃을 변수로 정해준다.
        View v = inflater.inflate(R.layout.fragment_first, container, false);

        // listView와 Ip, jsp를 불러온다
        listView = v.findViewById(R.id.lv_people);
        textView = v.findViewById(R.id.tv_sum_first);

        email = getArguments().getString("useremail");
        macIP = getArguments().getString("macIP");

        String urlAddr = "http://" + macIP + ":8080/test/";
        urlAddr1 = urlAddr + "people_query_all_no.jsp?email="+email;


        urlAddrBase = "http://" + macIP + ":8080/test/";
        urlAddr1 = urlAddrBase + "people_query_all_no.jsp?email=" + email;


        // 리스트 일반 클릭시
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewPeopleActivity.class);  // 원래는 회원정보로 가야한다 잠시 되는 곳 아무곳이나 보내놓음
                intent.putExtra("peopleno", members.get(position).getNo());
                intent.putExtra("useremail", members.get(position).getUseremail());
                intent.putExtra("phonetel", members.get(position).getTel());
                intent.putExtra("macIP", macIP);

                Toast.makeText(getActivity(), members.get(position).getNo(), Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });


        connectGetData(urlAddr1);
        int addressSum = members.size();

        textView.setText("(총 " + addressSum + "개의 연락처)");

        searchArr = new ArrayList<People>();
        search_EdT = v.findViewById(R.id.search_ET_First);
        search_EdT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_EdT.getText().toString();
                search(text);
            }
        });


        // 리스트 선택 리스너

        return v;

    }


    //////////////////////////////////////////////////////
    // 그룹별 horizontal 셋팅 - click 이벤트



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) // 메뉴 레이아웃 선택
    {
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.list_context_menu, menu);

    }


        // 리스트 길게 선택시
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                registerForContextMenu(listView); // 롱클릭시 이벤트와 메뉴 선
//                return false;
//            }
//        });
//
//
//
//        return v;




//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) // 메뉴 레이아웃 선택
//    {
//        MenuInflater inflater = new MenuInflater(getContext());
//        inflater.inflate(R.menu.list_context_menu, menu);
//    }
//
//    public boolean onContextItemSelected(MenuItem item)                                             // 메뉴 레이아웃에서 버튼 아이템 선택
//    {
//        String tel = "tel:" + peopleNum;
//        switch(item.getItemId())
//        {
//            case R.id.call:
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel));
//                startActivity(intent);
//                Toast.makeText(getActivity(), "전화 버튼 클릭됨", Toast.LENGTH_LONG).show();       // 전화 선택시
//                return true;
//            case R.id.message:                                                                      // 문자 선택시 ( 현재 문자 안됨.. 왜지..
//                Uri smsUri = Uri.parse("sms:" + tel);
//                Intent smsIntent = new Intent(Intent.ACTION_VIEW); // 보내는 화면이 팝업됨
//                smsIntent.putExtra("sms_body", "01086730811"); // 내용
////                smsIntent.setType("vnd.android-dir/mms-sms");
////                Intent smsIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("sms:"+tel));
//                smsIntent.setType("vnd.android-dir/mms-sms");
//                startActivity(smsIntent);
//
//                Toast.makeText(getActivity(), "문자 버튼 클릭됨", Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.kakao:
//                Toast.makeText(getActivity(), "카톡 버튼 클릭됨", Toast.LENGTH_LONG).show();       // 카톡 선택시
//                return true;
//        }
//
//        return super.onContextItemSelected(item);
//    }

    @Override
    public void onResume() {
        super.onResume();
        urlAddr1 = urlAddrBase + "people_query_all_no.jsp?email=" + email;
        connectGetData(urlAddr1);
        Log.v(TAG, "onResume()");
        Log.v(TAG, urlAddr1);
        searchArr.addAll(members);

    }

    // NetworkTask에서 값을 가져오는 메소드
    private void connectGetData(String urlAddr) {
        try {
            urlAddrBase = "http://" + macIP + ":8080/test/";
            PeopleNetworkTask peopleNetworkTask = new PeopleNetworkTask(getContext(), urlAddr);
            Object obj = peopleNetworkTask.execute().get();
            members = (ArrayList<People>) obj;
            Log.v(TAG, "" + members);
            adapter = new PeopleAdapter(getContext(), R.layout.people_custom_layout, members, urlAddrBase); // 아댑터에 값을 넣어준다.
            listView.setAdapter(adapter);  // 리스트뷰에 어탭터에 있는 값을 넣어준다.


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search(String charText) {
        members.clear();
        if (charText.length() == 0) {
            members.addAll(searchArr);
        }
        else {
            for (int i = 0; i < searchArr.size(); i++) {
                if (searchArr.get(i).getName().contains(charText)) {
                    members.add(searchArr.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }


}