package com.android.address_book;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.address_book_Activity.FirstFragment;
import com.android.address_book_Activity.ViewPeopleActivity;

import java.util.ArrayList;

public class PeopleAdapter extends BaseAdapter {
    Context mContext = null;
    FirstFragment firstFragment = null;
    int layout = 0;
    ArrayList<People> data = null;
    LayoutInflater inflater = null;
    String urlImageReal;
    String urlImage;

    public PeopleAdapter(Context mContext, int layout, ArrayList<People> data, String urlImage) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
        this.urlImage = urlImage;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public PeopleAdapter(Context mContext, int layout, ArrayList<People> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getNo();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }

        WebView img_peopleImg = convertView.findViewById(R.id.imgPeople_custom);
        TextView tv_name = convertView.findViewById(R.id.tv_name_custom);
        ImageView img_favoirteImg = convertView.findViewById(R.id.imgFavorite_custom);
        ImageView img_emgImg = convertView.findViewById(R.id.imgEmg_custom);


        if(data.get(position).getImage().equals("null")){
            urlImageReal = urlImage+"ic_defaultpeople.jpg";
            img_peopleImg.loadUrl(urlImageReal);
            img_peopleImg.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            img_peopleImg.setWebViewClient(new ViewPeopleActivity.WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용


            img_peopleImg.setFocusable(false);
            img_peopleImg.setClickable(false);
            img_peopleImg.getSettings().setJavaScriptEnabled(false);
            if (img_peopleImg != null)
                img_peopleImg.loadUrl(urlImageReal);

            //img_peopleImg.setImageResource(R.drawable.ic_defaultpeople);

        } else {
//            img_peopleImg.setImageResource(Integer.parseInt(data.get(position).getImage()));

            urlImageReal = urlImage + data.get(position).getImage();

            //img_peopleImg.loadUrl(urlImageReal);

            img_peopleImg.setFocusable(false);
            img_peopleImg.setClickable(false);
            img_peopleImg.getSettings().setJavaScriptEnabled(false);
            if (img_peopleImg != null)
                img_peopleImg.loadUrl(urlImageReal);
            img_peopleImg.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            img_peopleImg.setWebViewClient(new ViewPeopleActivity.WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_peopleImg.getSettings().setMixedContentMode ( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
            }
            img_peopleImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            {// https 이미지.
//                wsetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            }

        }
        tv_name.setText(data.get(position).getName());
        WebSettings webSettings = img_peopleImg.getSettings();
//
        // 화면 비율
        webSettings.setUseWideViewPort(true);       // wide viewport를 사용하도록 설정
        webSettings.setLoadWithOverviewMode(true);  // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        img_peopleImg.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        img_peopleImg.setHorizontalScrollBarEnabled(false); //가로 스크롤
        img_peopleImg.setVerticalScrollBarEnabled(false);   //세로 스크롤

        img_peopleImg.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 스크롤 노출 타입
        img_peopleImg.setScrollbarFadingEnabled(false);


        WebSettings wsetting = img_peopleImg.getSettings();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {// https 이미지.
            wsetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        img_peopleImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        img_peopleImg.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        img_peopleImg.setWebViewClient(new WebViewClient());
        img_peopleImg.setWebChromeClient(new WebChromeClient());
        img_peopleImg.setNetworkAvailable(true);

        //// Sets whether the DOM storage API is enabled.
        img_peopleImg.getSettings().setDomStorageEnabled(true);
        // 웹뷰 멀티 터치 가능하게 (줌기능)
        webSettings.setBuiltInZoomControls(false);   // 줌 아이콘 사용
        webSettings.setSupportZoom(false);



//        img_peopleImg.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        img_peopleImg.setInitialScale(1);
//        img_peopleImg.getSettings().setLoadWithOverviewMode(true);
//        img_peopleImg.getSettings().setUseWideViewPort(true);

        img_peopleImg.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        img_peopleImg.getSettings().setJavaScriptEnabled(true);

        img_peopleImg.clearView();
        img_peopleImg.requestLayout();



        if(Integer.parseInt(data.get(position).getFavorite()) == 1 ) { // 즐겨찾기 적용되었을 때
            img_favoirteImg.setImageResource(R.drawable.ic_favorite);

        } else {
            img_favoirteImg.setImageResource(R.drawable.ic_nonfavorite);
        }

        if(Integer.parseInt(data.get(position).getEmergency()) == 1) { // 긴급연락처 적용되었을 때
            img_emgImg.setImageResource(R.drawable.ic_emg2);

        } else{
            img_emgImg.setImageResource(R.drawable.ic_nonemg2);
        }


//        TextView tv_no = convertView.findViewById(R.id.tv_no);
//        TextView tv_name = convertView.findViewById(R.id.tv_name);
//        TextView tv_tel = convertView.findViewById(R.id.tv_tel);
//        TextView tv_email = convertView.findViewById(R.id.tv_email);
//        TextView tv_relation = convertView.findViewById(R.id.tv_relation);
//        TextView tv_memo = convertView.findViewById(R.id.tv_memo);
//        TextView tv_image = convertView.findViewById(R.id.tv_image);
//
//        tv_no.setText("번호 : " + data.get(position).getNo());
//        tv_name.setText("이름 : " + data.get(position).getName());
//        tv_tel.setText("전화번호 : " + data.get(position).getTel());
//        tv_email.setText("이메일 : " + data.get(position).getEmail());
//        tv_relation.setText("관계 : " + data.get(position).getRelation());
//        tv_memo.setText("메모 : " + data.get(position).getMemo());
//        tv_image.setText("이미지 : " + data.get(position).getImage());

//        if ((position % 2) == 1) {
//            convertView.setBackgroundColor(0x50000000);
//        } else {
//            convertView.setBackgroundColor(0x50dddddd);
//        }

        return convertView;
    }

    static class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
