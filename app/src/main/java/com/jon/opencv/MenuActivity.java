package com.jon.opencv;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jon.opencv.com.jon.opencv.adapter.CommandData;
import com.jon.opencv.com.jon.opencv.adapter.MyListViewAdapter;

import com.jon.opencv.com.jon.opencv.adapter.CommandConstants;

/**
 * Created by HASEE on 2019/3/3.
 */
public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
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
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_EnvTest));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_SaltNoise));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_InvertPixelSlow));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_InvertPixelFast));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_InvertBitmap));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_MatSubstract));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_MatAdd));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_BrightContrastAdjust));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_MatDemo));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_GetSubMat));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_MeanBlur));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_MediaBlur));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_GaussianBlur));
        myListViewAdapter.getMode().add(new CommandData(index++,CommandConstants.OpenCV_BilateralFilter));
        myListViewAdapter.notifyDataSetChanged();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object obj = view.getTag();
        Log.e(TAG,"onItemClick");
        if(obj instanceof CommandData){
            CommandData commandData = (CommandData)obj;
            Log.e(TAG,"onItemClick CommandData");
            Intent intent = new Intent(this.getApplicationContext(),ProcessImageActivity.class);
            intent.putExtra("command",commandData.getCommand());
            startActivity(intent);
        }

    }
}
