package com.android.address_book_Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.android.address_book.R;

public class UpdatePhoneNumber extends LinearLayout {


    public UpdatePhoneNumber(Context context) {
        super(context);

        init(context);
    }

    public UpdatePhoneNumber(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_update_phone_number, this, true);
    }


}