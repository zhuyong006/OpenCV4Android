package com.jon.opencv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jon.opencv.com.jon.opencv.adapter.CommandConstants;
import com.jon.opencv.com.jon.opencv.adapter.ImageProcessUtils;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ThresholdProcessActivity extends AppCompatActivity implements View.OnClickListener ,CommandConstants,SeekBar.OnSeekBarChangeListener{
    private String TAG = "OpenCV.Jon";
    private boolean Status = false;
    private ImageView imageView = null;
    private Button process_button= null;
    private Button select_button= null;
    private Button save_button= null;
    private TextView duration = null;
    private TextView progres_value = null;
    private int REQUEST_GET_IMAGE = 1;
    private SeekBar myseekBar = null;
    private int MAX_SIZE = 768;
    private Bitmap SelectBitmap = null;
    private String command = null;
    private Bitmap bitmap = null;
    private File SavePicFile = null;
    private int seek_value = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threshold_process);

        command = this.getIntent().getStringExtra("command");
        process_button = (Button)findViewById(R.id.photo_process);
        process_button.setText(command);
        process_button.setTag("process_image");
        process_button.setOnClickListener(this);

        select_button = (Button)findViewById(R.id.photo_choose);
        select_button.setTag("select_image");
        select_button.setOnClickListener(this);

        save_button = (Button)findViewById(R.id.save_photo);
        save_button.setTag("save_image");
        save_button.setOnClickListener(this);

        progres_value = (TextView) findViewById(R.id.progres_value);
        duration = (TextView) findViewById(R.id.duration);

        myseekBar = (SeekBar)this.findViewById(R.id.seekBar);
        myseekBar.setEnabled(true);
        myseekBar.setOnSeekBarChangeListener(this);

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
        else if(obj == "save_image") {
            Log.e(TAG,"save_image");
            save_image();
        }
    }

    private void save_image() {

        FileOutputStream fos;

        if(bitmap == null)
            return;
        try {

            SavePicFile = new File("/sdcard/Pictures/Screenshots/"+command+".jpg");
            if (!SavePicFile.exists()) {
                SavePicFile.getParentFile().mkdirs();
                SavePicFile.createNewFile();
            }
            fos = new FileOutputStream(SavePicFile);
            boolean status = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            if(status != true)
                Log.e(TAG,"compress failed");
            fos.flush();
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
            return;
        }


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

        long cost = 0;
        bitmap = null;

        if(SelectBitmap == null) {
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.girl, options);
            bitmap = bitmap.copy(bitmap.getConfig(),true);
        }
        else
            bitmap = SelectBitmap.copy(SelectBitmap.getConfig(),true);

        cost = System.currentTimeMillis();

        if(command.equals(OpenCV_Binary))
            bitmap = ImageProcessUtils.ImageBinarization(seek_value,bitmap);
        else if(command.equals(OpenCV_AdaptiveBinary)) {
            if((seek_value % 2) == 0)
                seek_value = seek_value+1;
            if(seek_value == 1)
                seek_value = 3;

            bitmap = ImageProcessUtils.ImageAdaptiveBinarization(seek_value, bitmap);
        }
        else if(command.equals(OpenCV_Canny_Edge)) {
            bitmap = ImageProcessUtils.CannyEdge(seek_value, bitmap);
        }else if(command.equals(OpenCV_Hough_LineDet)) {
            bitmap = ImageProcessUtils.HoughLineDet(seek_value, bitmap);
        }

        cost = System.currentTimeMillis() - cost;
        duration.setText("耗时" + Long.toString(cost) + "ms");
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        seek_value = myseekBar.getProgress();
        progres_value.setText("当前阈值为: " + seek_value);
        process_image();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
