package com.android.address_book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.address_book_Activity.FirstFragment;

import java.util.ArrayList;

public class GroupAdapter extends BaseAdapter {
    Context mContext = null;
    FirstFragment firstFragment = null;
    int layout = 0;
    ArrayList<Group> data = null;
    LayoutInflater inflater = null;


    public GroupAdapter(Context mContext, int layout, ArrayList<Group> data) {
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
        return data.get(position).getGroupNo();
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

        TextView tv_group = convertView.findViewById(R.id.tv_group_custom);
        tv_group.setText(data.get(position).getGroupName());

        return convertView;
    }

}
