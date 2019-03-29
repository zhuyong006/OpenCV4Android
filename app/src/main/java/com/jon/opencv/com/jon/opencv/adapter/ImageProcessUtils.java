package com.jon.opencv.com.jon.opencv.adapter;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgproc.Imgproc.MORPH_CLOSE;
import static org.opencv.imgproc.Imgproc.MORPH_OPEN;
import static org.opencv.imgproc.Imgproc.MORPH_RECT;

/**
 * Created by HASEE on 2019/3/9.
 */

public class ImageProcessUtils implements CommandConstants{
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

    public static Bitmap AddSalt(Bitmap bitmap){
        Log.e(TAG,"AddSalt");
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);
        int width = src.width();
        int height = src.height();
        int cnt = src.channels();
        byte[] bgra = new byte[cnt];
        for(int i=0;i<3000;i++)
        {
            int x = (int)(Math.random()*width);
            int y = (int)(Math.random()*height);
            src.get(y, x, bgra);
            bgra[0]=(byte)(255);
            bgra[1]=(byte)(255);
            bgra[2]=(byte)(255);
            src.put(y, x, bgra);
        }
        Utils.matToBitmap(src, bitmap);
        src.release();
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
    public static Bitmap MeanBlur(Bitmap bitmap){
        Log.e(TAG,"MeanBlur");

        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);
        //卷积的内核必须是奇数
        //Imgproc.blur(src,src, new Size(5,5),new Point(-1,-1),Imgproc.BORDER_DEFAULT);
        // 水平方向有抖动感
        //Imgproc.blur(src,src, new Size(15,1),new Point(-1,-1),Imgproc.BORDER_DEFAULT);
        // 垂直方向有抖动感
        Imgproc.blur(src,src, new Size(1,15),new Point(-1,-1),Imgproc.BORDER_DEFAULT);
        Utils.matToBitmap(src,bitmap);
        src.release();
        return bitmap;
    }
    public static Bitmap MedianBlur(Bitmap bitmap){
        Log.e(TAG,"MedianBlur");

        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);

        Imgproc.medianBlur(src,src,3);
        Utils.matToBitmap(src,bitmap);
        src.release();
        return bitmap;
    }
    public static Bitmap GaussianBlur(Bitmap bitmap){
        Log.e(TAG,"GaussianBlur");

        Mat src = new Mat();
        Utils.bitmapToMat(bitmap,src);
        //掩模小点，或者sigma小点(1.0左右或1.0以下)可以消除高斯噪声
        //Imgproc.GaussianBlur(src,src,new Size(3,3),0);
        //掩模大点，或者sigma过大会导致毛玻璃特效
        Imgproc.GaussianBlur(src,src,new Size(91,91),0);
        Utils.matToBitmap(src,bitmap);
        src.release();
        return bitmap;
    }
    public static Bitmap BilateralFilter(Bitmap bitmap){
        Log.e(TAG,"BilateralFilter");

        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        //双边滤波的输入参数只接受单通道或者3通道数据
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2BGR);
        //这里如果d为0，那么d会通过sigmaSpace得出
        Imgproc.bilateralFilter(src,dst,13,150,25);

        // Mat kernel = new Mat(3,3, CvType.CV_16S);
        // kernel.put(0,0,0,-1,0,-1,5,-1,0,-1,0);
        // Imgproc.filter2D(dst,dst,-1,kernel,new Point(-1,-1),0.0,Imgproc.BORDER_DEFAULT);
        // kernel.release();
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        return bitmap;
    }

    public static void CustomFilter(String command, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Mat kernel = getCustomOperator(command);
        Imgproc.filter2D(src, dst, -1, kernel, new Point(-1, -1), 0.0, Imgproc.BORDER_DEFAULT);
        Utils.matToBitmap(dst, bitmap);
        kernel.release();
        src.release();
        dst.release();
    }

    private static Mat getCustomOperator(String command) {
        Mat kernel = new Mat(3, 3, CvType.CV_32FC1);
        if(OpenCV_CustomMeanBlur.equals(command)) {
            kernel.put(0, 0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0);
        } else if(OpenCV_EdgeDetect.equals(command)) {
            kernel.put(0, 0, -1, -1, -1, -1, 8, -1, -1, -1, -1);
        } else if(OpenCV_Sharpen.equals(command)) {
            kernel.put(0, 0, -1, -1, -1, -1, 9, -1, -1, -1, -1);
        }
        return kernel;
    }
    public static void ErodeAndDilate(String command, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        //获取结构元素，需要留意结构元素不一定都是矩形，它还可以是十字形等任意形状
        Mat kernel = Imgproc.getStructuringElement(MORPH_RECT,new Size(3,3),new Point(-1,-1));

        //腐蚀：用结构元素的最小值替换，最后一个参数是迭代次数
        if(OpenCV_Erode.equals(command)) {
            Imgproc.erode(src,dst,kernel,new Point(-1,-1),4);
        //膨胀: 用结构元素的最大值替换，最后一个参数是迭代次数
        } else if(OpenCV_Dilate.equals(command)) {
            Imgproc.dilate(src, dst, kernel, new Point(-1, -1), 4);
        }

        Utils.matToBitmap(dst, bitmap);
        kernel.release();
        src.release();
        dst.release();
    }
    public static void OpenAndClose(String command, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        //获取结构元素，需要留意结构元素不一定都是矩形，它还可以是十字形等任意形状
        Mat kernel = Imgproc.getStructuringElement(MORPH_RECT,new Size(3,3),new Point(-1,-1));

        //腐蚀：用结构元素的最小值替换，最后一个参数是迭代次数
        if(OpenCV_Morph_Open.equals(command)) {
            Imgproc.morphologyEx(src,dst,MORPH_OPEN,kernel,new Point(-1,-1),2);
            //膨胀: 用结构元素的最大值替换，最后一个参数是迭代次数
        } else if(OpenCV_Morph_Close.equals(command)) {
            Imgproc.morphologyEx(src,dst,MORPH_CLOSE,kernel,new Point(-1,-1),2);
        }

        Utils.matToBitmap(dst, bitmap);
        kernel.release();
        src.release();
        dst.release();
    }
    public static Bitmap MorphLineDetect(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        //获取结构元素，需要留意结构元素不一定都是矩形，它还可以是十字形等任意形状
        Mat kernel = Imgproc.getStructuringElement(MORPH_RECT,new Size(100,1),new Point(-1,-1));

        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(src,src,0,255,Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        Imgproc.morphologyEx(src,dst,MORPH_OPEN,kernel,new Point(-1,-1),1);

        Utils.matToBitmap(dst, bitmap);
        kernel.release();
        src.release();
        dst.release();
        return bitmap;
    }
    public static Bitmap ImageBinarization(int value,Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);

        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(src,dst,value,255,Imgproc.THRESH_BINARY);

        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
        return bitmap;
    }
    public static Bitmap ImageAdaptiveBinarization(int value,Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);

        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        //block_size 必须是奇数
        //Imgproc.adaptiveThreshold(src,dst,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,value,0);
        Imgproc.adaptiveThreshold(src,dst,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,value,0);

        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
        return bitmap;
    }
}
