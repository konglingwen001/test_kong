package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by CHT1HTSH3236 on 2018/5/28.
 */

public class PopupAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<String> list;

    public PopupAdapter(Context context, List<String> list) {
        this.context = context;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        TextView tvName = convertView.findViewById(android.R.id.text1);
        tvName.setText(list.get(position));

        return convertView;
    }
}
