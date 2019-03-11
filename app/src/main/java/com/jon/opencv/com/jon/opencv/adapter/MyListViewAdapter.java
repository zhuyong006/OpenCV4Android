package com.jon.opencv.com.jon.opencv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jon.opencv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HASEE on 2019/3/3.
 */
public class MyListViewAdapter extends BaseAdapter{
    private List<CommandData> commandDataList;
    private Context context;
    public MyListViewAdapter(Context context){
        this.context = context;
        commandDataList = new ArrayList<>();
    }

    public List<CommandData> getMode(){
        return this.commandDataList;
    }
    @Override
    public int getCount() {
        return commandDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return commandDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return commandDataList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.raw_view,parent,false);
        TextView textView = (TextView)view.findViewById(R.id.rawview);
        textView.setText(commandDataList.get(position).getCommand());
        view.setTag(commandDataList.get(position));
        textView.setTextColor(Color.BLACK);
        return view;
    }
}
