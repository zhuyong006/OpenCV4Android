package com.jon.opencv.com.jon.opencv.adapter;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by HASEE on 2019/3/9.
 */

public class ImageProcessUtils {
    public static final String TAG = "OpenCV.Jon";

    public static Bitmap InvertMat(Bitmap bitmap){
        Log.e(TAG,"InvertMat");
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);
        int width = src.width();
        int height = src.height();
        int cnt = src.channels();
        byte[] bgra = new byte[cnt];

        for(int w=0;w<width;w++) {
            for (int h = 0; h < height; h++) {
                //这里的w，h不能反，否则图像有问题
                src.get(h, w, bgra);
                for (int n = 0; n < cnt; n++)
                    bgra[n] = (byte) (255 - bgra[n] & 0xff);
                src.put(h, w, bgra);
            }
        }
        Utils.matToBitmap(src, bitmap);
        src.release();
        return bitmap;
    }
    public static Bitmap InvertBitmap(Bitmap bitmap){
        Log.e(TAG,"InvertBitmap");


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width*height];
        bitmap.getPixels(pixels,0,width,0,0,width,height);
        for(int w=0;w<width;w++) {
            int index = w*height;
            for (int h = 0; h < height; h++) {
                int a = (pixels[index+h] >> 24) & 0xff;
                int r = (pixels[index+h] >> 16) & 0xff;
                int g = (pixels[index+h] >> 8) & 0xff;
                int b = (pixels[index+h]) & 0xff;
                a = 255 - a;
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                pixels[index+h] = (a << 24) | (r << 16)| (g << 8)| b;
            }
        }
        bitmap.setPixels(pixels,0,width,0,0,width,height);

        return bitmap;
    }
    public static Bitmap convert2Gray(Bitmap bitmap){
        Log.e(TAG,"convert2Gray");
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
        return bitmap;
    }
}