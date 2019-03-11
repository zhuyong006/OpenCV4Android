package com.jon.opencv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jon.opencv.com.jon.opencv.adapter.CommandConstants;
import com.jon.opencv.com.jon.opencv.adapter.ImageProcessUtils;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.io.InputStream;

public class ProcessImageActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "OpenCV.Jon";
    private boolean Status = false;
    private ImageView imageView = null;
    private Button process_button= null;
    private Button select_button= null;
    private TextView duration = null;
    private int REQUEST_GET_IMAGE = 1;
    private int MAX_SIZE = 768;
    private Bitmap SelectBitmap = null;
    private String command = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opencv_process_photo);

        command = this.getIntent().getStringExtra("command");
        process_button = (Button)findViewById(R.id.photo_process);
        process_button.setText(command);
        process_button.setTag("process_image");
        process_button.setOnClickListener(this);

        select_button = (Button)findViewById(R.id.photo_choose);
        select_button.setTag("select_image");
        select_button.setOnClickListener(this);

        duration = (TextView) findViewById(R.id.duration);

        imageView = (ImageView)findViewById(R.id.imageView);
        initOpenCVLibs();


    }

    private void initOpenCVLibs() {
        Status =  OpenCVLoader.initDebug();
        if(Status != true)
        {
            Log.e(TAG, "OpenCV Init Failed");
        }else{
            Log.e(TAG, "OpenCV Init Success");
        }
    }

    @Override
    public void onClick(View v) {
        String obj = (String)v.getTag();
        if(obj == "process_image")
            process_image();
        else if(obj == "select_image")
            select_image();

    }

    private void select_image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Browser Image..."),REQUEST_GET_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GET_IMAGE && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            try{
                InputStream inputStream = getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(inputStream, null, options);
                int width = options.outWidth;
                int height = options.outHeight;
                int sample_size = 1;
                int max = Math.max(width,height);
                if(max > MAX_SIZE)
                {
                    int nw = width/2;
                    int nh = height/2;
                    while(nw/sample_size > MAX_SIZE || nh/sample_size > MAX_SIZE)
                        sample_size = sample_size*2;
                }
                options.inSampleSize = sample_size;
                options.inJustDecodeBounds = false;
                SelectBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

                ImageView imageView = (ImageView)findViewById(R.id.imageView);
                imageView.setImageBitmap(SelectBitmap);
            }catch (IOException io) {
                    Log.e(TAG,io.getMessage());
            }
        }
    }
    private void process_image() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap;
        long cost = 0;

        if(SelectBitmap == null) {
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.girl, options);
            bitmap = bitmap.copy(bitmap.getConfig(),true);
        }
        else
            bitmap = SelectBitmap.copy(SelectBitmap.getConfig(),true);

        cost = System.currentTimeMillis();

        if(command.equals(CommandConstants.OpenCV_EnvTest))
            bitmap = ImageProcessUtils.convert2Gray(bitmap);
        else if(command.equals(CommandConstants.OpenCV_InvertPixel))
            bitmap = ImageProcessUtils.InvertMat(bitmap);
        else if(command.equals(CommandConstants.OpenCV_InvertBitmap))
            bitmap = ImageProcessUtils.InvertBitmap(bitmap);

        cost = System.currentTimeMillis() - cost;
        duration.setText(Long.toString(cost));
        imageView.setImageBitmap(bitmap);
    }
}
