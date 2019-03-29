package com.jon.opencv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jon.opencv.com.jon.opencv.adapter.CommandConstants;
import com.jon.opencv.com.jon.opencv.adapter.CommandData;
import com.jon.opencv.com.jon.opencv.adapter.MyListViewAdapter;

/**
 * Created by HASEE on 2019/3/3.
 */
public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , CommandConstants{
    private ListView listView = null;
    private MyListViewAdapter myListViewAdapter;
    private String TAG = "OpenCV.Jon";
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        myListViewAdapter = new MyListViewAdapter(this.getApplicationContext());
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(myListViewAdapter);
        listView.setOnItemClickListener(this);
        myListViewAdapter.getMode().addAll(CommandData.getCommandList());
        myListViewAdapter.notifyDataSetChanged();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object obj = view.getTag();
        Log.e(TAG,"onItemClick");
        if(obj instanceof CommandData){
            CommandData commandData = (CommandData)obj;
            Log.e(TAG,"onItemClick CommandData");

            Intent intent;
            if(commandData.getCommand().equals(OpenCV_Binary) || commandData.getCommand().equals(OpenCV_AdaptiveBinary))
                intent = new Intent(this.getApplicationContext(),ThresholdProcessActivity.class);
            else
                intent = new Intent(this.getApplicationContext(),ProcessImageActivity.class);

            intent.putExtra("command",commandData.getCommand());
            startActivity(intent);
        }

    }
}
