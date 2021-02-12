package com.android.address_book_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.address_book.R;
import com.android.address_book.SectionPageAdapter;

import me.relex.circleindicator.CircleIndicator;

public class IntroViewStart extends AppCompatActivity {

    private ViewPager mViewPager;
    SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro_view_layout);

        // 앱소개 뷰페이저
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);   // 뷰페이지 불러오기
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator); // 인디케이터 불러오기
        indicator.setViewPager(mViewPager);  // 인디케이터 안에 페이저처리
        btnStart = findViewById(R.id.btnStartMain);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroViewStart.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    // 앱 소개 프레그먼트 이동
    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new IntroViewFragment1(), "1");
        adapter.addFragment(new IntroViewFragment2(), "2");
        adapter.addFragment(new IntroViewFragment3(), "3");

        viewPager.setAdapter(adapter);
    }
}
