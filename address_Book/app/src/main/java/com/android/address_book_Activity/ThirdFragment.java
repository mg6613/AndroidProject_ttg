package com.android.address_book_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.Task.PeopleNetworkTask;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;

import java.util.ArrayList;

public class ThirdFragment extends Fragment {
    final static String TAG = "SelectAllActivity";
    String urlAddrBase = null;
    String urlAddr1 = null;
    ArrayList<People> searchArr;
    ArrayList<People> members;
    PeopleAdapter adapter;
    ListView listView;
    String macIP;
    String email;
    EditText search_EdT;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third, container, false);
        // Inflate the layout for this fragment

        listView = v.findViewById(R.id.lv_favorite);
        search_EdT = v.findViewById(R.id.search_ET_Third);
//        adapter = new PeopleAdapter(getContext(), R.layout.people_custom_layout, members);

        String urlAddr = "http://" + macIP + ":8080/test/";
//        listView.setAdapter(adapter);  // 리스트뷰에 어탭터에 있는 값을 넣어준다.
        urlAddr1 = urlAddr + "favorite_people_query_all_no.jsp?email=con@naver.com";



        email = getArguments().getString("useremail");
        macIP = getArguments().getString("macIP");


//        listView.setAdapter(adapter);  // 리스트뷰에 어탭터에 있는 값을 넣어준다.

        connectGetData(urlAddr1);
        searchArr = new ArrayList<People>();
        searchArr.addAll(members);
        search_EdT = v.findViewById(R.id.search_ET_Third);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewPeopleActivity.class);  // 원래는 회원정보로 가야한다 잠시 되는 곳 아무곳이나 보내놓음
                intent.putExtra("peopleno", members.get(position).getNo());
                intent.putExtra("useremail", members.get(position).getUseremail());
                intent.putExtra("phonetel", members.get(position).getTel());
                intent.putExtra("macIP", macIP);



                startActivity(intent);
            }
        });
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        urlAddrBase = "http://" + macIP + ":8080/test/";
        urlAddr1 = urlAddrBase + "favorite_people_query_all.jsp?email=" + email;
        connectGetData(urlAddr1);
        Log.v(TAG, "onResume()");
        searchArr.addAll(members);
    }

    // NetworkTask에서 값을 가져오는 메소드
    private void connectGetData(String urlAddr) {
        try {
            PeopleNetworkTask peopleNetworkTask = new PeopleNetworkTask(getContext(), urlAddr);
            Object obj = peopleNetworkTask.execute().get();
            members = (ArrayList<People>) obj;
            for(int i=0 ; i<members.size();i++){

                Log.v("here", "" + members.get(i).getImage());
            }
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