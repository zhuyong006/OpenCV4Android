package com.jon.opencv.com.jon.opencv.adapter;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.List;

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

    //局部二值化会对不同的区域产生不同的阈值
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
    //直方图均衡化，输入图像必须是单通道的，处理纯色背景效果不好
    public static Bitmap HistogramEq(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);

        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(src,dst);

        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
        return bitmap;
    }
    /*
    * X方向 : sobel算子    [-1 0 +1]                X方向 : Scharr算子     [-3 0 +3]
    *                      [-2 0 +2]   * I                               [-10 0 +10]
    *                      [-1 0 +1]                                     [-3 0 +3]
    * Y方向 : sobel算子    [-1 -2 -1]              Y方向 : Scharr算子     [-3 -10 -3]
    *                      [ 0  0 0 ]  * I                               [0   0   0]
    *                      [+1 +2 +1]                                    [+3 +10 +3]
    * XY方向 : G = |Gx| + |Gy|
    *  type = 0 :       X方向
    *  type = 1 :       Y方向
    *  type = 2 :       XY方向
    * */
    public static Bitmap GradientImg(Bitmap bitmap,int type) {
        Mat gradient_x = new Mat();
        Mat gradient_y = new Mat();
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);

        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        //这里的CvType.CV_16S，可以防止计算的结果超过255(8位)，产生截断效应，从而使得该点图像亮度为0
        if(type == 0)
            Imgproc.Sobel(src,src,CvType.CV_16S,1,0);
        else if(type == 1)
            Imgproc.Sobel(src,src,CvType.CV_16S,0,1);
        else if(type == 2) {
            Imgproc.Scharr(src,gradient_x,CvType.CV_16S,1,0);
            Imgproc.Scharr(src,gradient_y,CvType.CV_16S,0,1);
            Core.convertScaleAbs(gradient_x,gradient_x);
            Core.convertScaleAbs(gradient_y,gradient_y);
            Core.addWeighted(gradient_x,0.5,gradient_y,0.5,0,src);
        }
        Core.convertScaleAbs(src,src);

        Utils.matToBitmap(src, bitmap);
        src.release();
        gradient_x.release();
        gradient_y.release();
        return bitmap;
    }
    /*
    *  之前有学过可以通过拉普拉斯算子求图像边缘，那个方法比较粗糙，尽量选择Canny
    *  1. 高斯模糊--GaussianBlur
    *  2. 灰度转换--cvtColor
    *  3. 计算梯度--Sobel/Scharr
    *  4. 非最大信号抑制
    *  5. 高低阈值输出二值图像
     * */
    public static Bitmap CannyEdge(int val,Bitmap bitmap) {


        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);

        Imgproc.GaussianBlur(src,src,new Size(3,3),0,0,Imgproc.BORDER_DEFAULT);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(src,dst,val/2,val,3,false);

        Utils.matToBitmap(dst, bitmap);
        src.release();


        return bitmap;
    }

    public static Bitmap HoughLineDet(int val,Bitmap bitmap) {


        Mat src = new Mat();
        Mat lines = new Mat();

        Utils.bitmapToMat(bitmap, src);

        Mat dst = new Mat(src.size(),src.type());

        if(val == 0)
            val = 1;

        Imgproc.GaussianBlur(src,src,new Size(3,3),0,0,Imgproc.BORDER_DEFAULT);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(src,src,112,224,3,false);

        //  线段检测，这里的rho和theta是指的画极坐标的时候的X轴和Y轴的步长，
        //  15指的是：必须15个像素以上才认为是线段否则放弃
        //  3指的是：线段间的间隔至少要有3个像素，否则认为是同一个线段
//        Imgproc.HoughLinesP(src,lines,1.0,Math.PI/180,val,15,3);
//        double[] linesp = new double[4];
//        for(int i=0;i<lines.cols();i++)
//        {
//            linesp = lines.get(0,i);
//            Point p1 = new Point(linesp[0], linesp[1]);
//            Point p2 = new Point(linesp[2], linesp[3]);
//            Core.line(dst,p1,p2,new Scalar(0,255,0),2);
//        }

        Imgproc.HoughLines(src,lines,1,Math.PI/180.0,val);
        double[] linesp = new double[2];
        for(int i=0;i<lines.cols();i++)
        {
            linesp = lines.get(0,i);
            double rho = linesp[0];
            double theta = linesp[1];
            double a = Math.cos(theta);   //获取角度cos值
            double b = Math.sin(theta);   //获取角度sin值
            double x0 = a * rho;    //获取x轴值
            double y0 = b * rho;    //获取y轴值　　x0和y0是直线的中点
            int x1 = (int)(x0 + 800*(-b));    //获取这条直线最大值点x1
            int y1 = (int)(y0 + 800*(a));     //获取这条直线最大值点y1
            int x2 = (int)(x0 - 800 * (-b));  //获取这条直线最小值点x2　　
            int y2 = (int)(y0 - 800 * (a));   //获取这条直线最小值点y2　　其中*1000是内部规则
            Core.line(dst,new Point(x1,y1),new Point(x2,y2),new Scalar(0,255,0),2,8,0);
        }

        Utils.matToBitmap(dst, bitmap);
        src.release();
        lines.release();
        dst.release();


        return bitmap;
    }
    public static Bitmap HoughCircleDet(int val,Bitmap bitmap) {


        Mat src = new Mat();
        Mat circles = new Mat();

        Utils.bitmapToMat(bitmap, src);

        Mat dst = new Mat(src.size(),src.type());
        src.copyTo(dst);

        if(val == 0)
            val = 1;

        Imgproc.GaussianBlur(src,src,new Size(3,3),0,0,Imgproc.BORDER_DEFAULT);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);

        //val：54 is the best for my img
        Imgproc.HoughCircles(src,circles,Imgproc.CV_HOUGH_GRADIENT,1,10,val,80,15,80);
        double[] circle = new double[3];
        for(int i=0;i<circles.cols();i++)
        {
            circle = circles.get(0,i);
            int a = (int)circle[0];
            int b = (int)circle[1];
            int r = (int)circle[2];

            Core.circle(dst,new Point(a,b),r,new Scalar(255,0,0),2);
        }

        Utils.matToBitmap(dst, bitmap);
        src.release();
        circles.release();
        dst.release();

        return bitmap;
    }
    public static Bitmap TemplateMatch(Bitmap tpl,Bitmap src) {

        Mat m_src = new Mat();
        Mat m_tpl = new Mat();

        Utils.bitmapToMat(src, m_src);
        Utils.bitmapToMat(tpl, m_tpl);

        int width = src.getWidth() - tpl.getWidth() + 1;
        int height = src.getHeight() - tpl.getHeight() + 1;
        Mat result = new Mat(width,height,CvType.CV_32FC1);

        Imgproc.matchTemplate(m_src,m_tpl,result,Imgproc.TM_CCOEFF_NORMED);
        Core.normalize(result,result,0,1,Core.NORM_MINMAX,-1);
        Core.MinMaxLocResult match = Core.minMaxLoc(result);
        Point pt1 = new Point(match.maxLoc.x,match.maxLoc.y);
        Point pt2 = new Point(match.maxLoc.x + tpl.getWidth(),match.maxLoc.y + tpl.getHeight());

        Core.rectangle(m_src,pt1,pt2,new Scalar(255,0,0),2);

        Utils.matToBitmap(m_src, src);
        m_src.release();
        m_tpl.release();
        result.release();
        return src;
    }
    public static Bitmap MeasureObject(Bitmap bitmap) {

        Mat src = new Mat();
        Mat hierarchy = new Mat();
        List<MatOfPoint> Counters = new ArrayList<MatOfPoint>();
        Utils.bitmapToMat(bitmap, src);
        Mat dst = new Mat(src.size(),src.type());

        Imgproc.GaussianBlur(src,src,new Size(3,3),0);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(src,src,0,255,Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        Imgproc.findContours(src,Counters,hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
        for(int i=0;i<Counters.size();i++)
        {
            Imgproc.drawContours(dst,Counters,i,new Scalar(255,0,0),2);
            double area = Imgproc.contourArea(Counters.get(i));
            double arcLength = Imgproc.arcLength(new MatOfPoint2f(Counters.get(i).toArray()),true);
            Rect rect = Imgproc.boundingRect(Counters.get(i));
            Core.rectangle(dst,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,255,0),2);
            Log.e(TAG,"Area:"+area);
            Log.e(TAG,"ArcLength:"+arcLength);
            Moments moments = Imgproc.moments(Counters.get(i));
            int x0 = (int)(moments.get_m10()/moments.get_m00());
            int y0 = (int)(moments.get_m01()/moments.get_m00());
            Core.circle(dst,new Point(x0,y0),3,new Scalar(0,0,255),-1);
        }

        Utils.matToBitmap(dst,bitmap);
        src.release();
        hierarchy.release();
        dst.release();
        return bitmap;
    }
    public static Bitmap FaceDect(CascadeClassifier cascade, Bitmap bitmap) {

        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap,src);
        Utils.bitmapToMat(bitmap,dst);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        MatOfRect mRects = new MatOfRect();
        cascade.detectMultiScale(src,mRects,1.1,3,0,new Size(10,10),new Size(0,0));
        List<Rect> Rects= mRects.toList();
        for(int i=0;i < Rects.size();i++)
        {
            Point pt1 = new Point(Rects.get(i).x,Rects.get(i).y);
            Point pt2 = new Point(Rects.get(i).x+Rects.get(i).width,Rects.get(i).y+Rects.get(i).height);
            Core.rectangle(dst,pt1,pt2,new Scalar(255,0,0),2);
        }
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        mRects.release();
        return bitmap;
    }
}
