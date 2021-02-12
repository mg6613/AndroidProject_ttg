package com.android.address_book_Activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.android.address_book.R;

public class RegisterAddPhoneNumber extends LinearLayout {
    public RegisterAddPhoneNumber(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public RegisterAddPhoneNumber(Context context) {
        super(context);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_register_addphonenumber, this, true);
    }
}
