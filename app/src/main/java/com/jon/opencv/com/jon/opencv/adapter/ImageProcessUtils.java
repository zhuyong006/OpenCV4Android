package com.jon.opencv.com.jon.opencv.adapter;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8U;

/**
 * Created by HASEE on 2019/3/9.
 */

public class ImageProcessUtils {
    public static final String TAG = "OpenCV.Jon";

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
    public static Bitmap InvertMatSlow(Bitmap bitmap){
        Log.e(TAG,"InvertMatSlow");
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
    public static Bitmap InvertMatFast(Bitmap bitmap){
        Log.e(TAG,"InvertMatFast");
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Core.bitwise_not(src,src);
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
    public static Bitmap MatSubstract(Bitmap bitmap){
        Log.e(TAG,"MatSubstract");
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat white = new Mat(src.size(),src.type(), Scalar.all(255));
        Core.subtract(white,src,src);
        Utils.matToBitmap(src, bitmap);
        white.release();
        src.release();
        return bitmap;
    }
    public static Bitmap MatAdd(Bitmap bitmap){
        Log.e(TAG,"MatAdd");
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);
        //Mat white = new Mat(src.size(),src.type(), Scalar.all(255));
        Mat ratio = new Mat(src.size(),src.type(),Scalar.all(20));
        //double weight = 0.5;
        //Core.addWeighted(white,weight,src,1-weight,0,src);
        Core.add(src,ratio,src);
        Utils.matToBitmap(src, bitmap);
        //white.release();
        ratio.release();
        src.release();
        return bitmap;
    }
    public static Bitmap MatBrightContrastAdjust(Bitmap bitmap){
        Log.e(TAG,"MatBrightContrastAdjust");
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);

        src.convertTo(src,CV_32F);
        Mat bright_ratio = new Mat(src.size(),src.type(),Scalar.all(10));
        Mat contrast_ratio = new Mat(src.size(),src.type(),Scalar.all(1.2));

        Core.add(src,bright_ratio,src);
        Core.multiply(src,contrast_ratio,src);

        src.convertTo(src,CV_8U);
        Utils.matToBitmap(src, bitmap);

        contrast_ratio.release();
        bright_ratio.release();
        src.release();
        return bitmap;
    }
    public static Bitmap MatDemoUsage(Bitmap bitmap){
        Log.e(TAG,"MatDemoUsage");


        Mat src = new Mat(bitmap.getHeight(),bitmap.getWidth(),CV_8U,new Scalar(100));

        //bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),Bitmap.Config.ARGB_8888);


        Utils.matToBitmap(src, bitmap);


        src.release();
        return bitmap;
    }
    public static Bitmap GetRoiArea(Bitmap bitmap){
        Log.e(TAG,"GetRoiArea");

        Rect rect = new Rect(200,150,400,300);
        Bitmap dst_bitmap = Bitmap.createBitmap(rect.width,rect.height, Bitmap.Config.ARGB_8888);

        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat dst = src.submat(rect);
        Imgproc.cvtColor(dst,dst,Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(dst,dst_bitmap);

        src.release();
        dst.release();
        return dst_bitmap;
    }
}
