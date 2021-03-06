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
import android.widget.TextView;

import com.jon.opencv.com.jon.opencv.adapter.CommandConstants;
import com.jon.opencv.com.jon.opencv.adapter.ImageProcessUtils;

import org.opencv.android.OpenCVLoader;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProcessImageActivity extends AppCompatActivity implements View.OnClickListener ,CommandConstants{
    private String TAG = "OpenCV.Jon";
    private boolean Status = false;
    private ImageView imageView = null;
    private Button process_button= null;
    private Button select_button= null;
    private Button save_button= null;
    private TextView duration = null;
    private int REQUEST_GET_IMAGE = 1;
    private int MAX_SIZE = 768;
    private Bitmap SelectBitmap = null;
    private String command = null;
    private Bitmap bitmap = null;
    private File SavePicFile = null;
    private CascadeClassifier cascade = null;
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

        save_button = (Button)findViewById(R.id.save_photo);
        save_button.setTag("save_image");
        save_button.setOnClickListener(this);

        duration = (TextView) findViewById(R.id.duration);

        imageView = (ImageView)findViewById(R.id.imageView);
        initOpenCVLibs();

        cascade = new CascadeClassifier("/data/haarcascade_eye_tree_eyeglasses.xml");

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

    public ProcessImageActivity() {
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

        if(command.equals(OpenCV_EnvTest))
            bitmap = ImageProcessUtils.convert2Gray(bitmap);
        else if(command.equals(OpenCV_SaltNoise))
            bitmap = ImageProcessUtils.AddSalt(bitmap);
        else if(command.equals(OpenCV_InvertPixelSlow))
            bitmap = ImageProcessUtils.InvertMatSlow(bitmap);
        else if(command.equals(OpenCV_InvertPixelFast))
            bitmap = ImageProcessUtils.InvertMatFast(bitmap);
        else if(command.equals(OpenCV_InvertBitmap))
            bitmap = ImageProcessUtils.InvertBitmap(bitmap);
        else if(command.equals(OpenCV_MatSubstract))
            bitmap = ImageProcessUtils.MatSubstract(bitmap);
        else if(command.equals(OpenCV_MatAdd))
            bitmap = ImageProcessUtils.MatAdd(bitmap);
        else if(command.equals(OpenCV_BrightContrastAdjust))
            bitmap = ImageProcessUtils.MatBrightContrastAdjust(bitmap);
        else if(command.equals(OpenCV_MatDemo))
            bitmap = ImageProcessUtils.MatDemoUsage(bitmap);
        else if(command.equals(OpenCV_GetSubMat))
            bitmap = ImageProcessUtils.GetRoiArea(bitmap);
        else if(command.equals(OpenCV_MeanBlur))
            bitmap = ImageProcessUtils.MeanBlur(bitmap);
        else if(command.equals(OpenCV_MediaBlur))
            bitmap = ImageProcessUtils.MedianBlur(bitmap);
        else if(command.equals(OpenCV_GaussianBlur))
            bitmap = ImageProcessUtils.GaussianBlur(bitmap);
        else if(command.equals(OpenCV_BilateralFilter))
            bitmap = ImageProcessUtils.BilateralFilter(bitmap);
        else if(command.equals(OpenCV_CustomMeanBlur)
                || command.equals(OpenCV_EdgeDetect)
                || command.equals(OpenCV_Sharpen) )
            ImageProcessUtils.CustomFilter(command,bitmap);
        else if(command.equals(OpenCV_Erode)
                || command.equals(OpenCV_Dilate))
            ImageProcessUtils.ErodeAndDilate(command,bitmap);
        else if(command.equals(OpenCV_Morph_Open)
                || command.equals(OpenCV_Morph_Close))
            ImageProcessUtils.OpenAndClose(command,bitmap);
        else if(command.equals(OpenCV_Morph_Line_Detect))
            bitmap = ImageProcessUtils.MorphLineDetect(bitmap);
        else if(command.equals(OpenCV_HistogramEq))
            bitmap = ImageProcessUtils.HistogramEq(bitmap);
        else if(command.equals(OpenCV_Gradient_X))
            bitmap = ImageProcessUtils.GradientImg(bitmap,0);
        else if(command.equals(OpenCV_Gradient_Y))
            bitmap = ImageProcessUtils.GradientImg(bitmap,1);
        else if(command.equals(OpenCV_Gradient_XY))
            bitmap = ImageProcessUtils.GradientImg(bitmap,2);
        else if(command.equals(OpenCV_Template_Match)) {
            Bitmap tpl = BitmapFactory.decodeResource(this.getResources(), R.drawable.tpl, options);
            bitmap = ImageProcessUtils.TemplateMatch(tpl, bitmap);
        }
        else if(command.equals(OpenCV_Measure_Object)) {
            bitmap = ImageProcessUtils.MeasureObject(bitmap);
        }
        else if(command.equals(OpenCV_Face_Dect)) {
            bitmap = ImageProcessUtils.FaceDect(cascade,bitmap);
        }
        cost = System.currentTimeMillis() - cost;
        duration.setText("耗时" + Long.toString(cost) + "ms");
        imageView.setImageBitmap(bitmap);
    }
}
