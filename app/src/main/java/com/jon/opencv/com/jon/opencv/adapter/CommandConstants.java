package com.jon.opencv.com.jon.opencv.adapter;

/**
 * Created by HASEE on 2019/3/9.
 */

public interface CommandConstants {
    static public final String OpenCV_EnvTest = "环境测试-灰度";
    static public final String OpenCV_SaltNoise = "产生椒盐噪声";
    static public final String OpenCV_InvertPixelSlow = "Mat像素操作慢-反色";
    static public final String OpenCV_InvertPixelFast = "Mat像素操作快-反色";
    static public final String OpenCV_InvertBitmap = "Bitmap像素操作-反色";
    static public final String OpenCV_MatSubstract = "Mat像素操作-减法";
    static public final String OpenCV_MatAdd = "Mat像素操作-加法/图像融合";
    static public final String OpenCV_BrightContrastAdjust = "Mat像素操作-亮度对比度";
    static public final String OpenCV_MatDemo = "Mat操作-Demo演示";
    static public final String OpenCV_GetSubMat = "Mat操作-获取子图";
    static public final String OpenCV_MeanBlur = "OpenCV-均值模糊";
    static public final String OpenCV_MediaBlur = "OpenCV-中值模糊";
    static public final String OpenCV_GaussianBlur = "OpenCV-高斯模糊";
    static public final String OpenCV_BilateralFilter = "OpenCV-双边滤波";
    public static final String OpenCV_CustomMeanBlur = "OpenCV自定义算子-模糊";
    public static final String OpenCV_EdgeDetect = "OpenCV自定义算子-边缘";
    public static final String OpenCV_Sharpen = "OpenCV自定义算子-锐化";
    public static final String OpenCV_Erode = "OpenCV-腐蚀";
    public static final String OpenCV_Dilate = "OpenCV-膨胀";
    public static final String OpenCV_Morph_Open = "OpenCV-开操作";
    public static final String OpenCV_Morph_Close = "OpenCV-闭操作";
    public static final String OpenCV_Morph_Line_Detect = "OpenCV-形态学操作之直线检测";
    public static final String OpenCV_Binary = "OpenCV-二值化";
    public static final String OpenCV_AdaptiveBinary = "OpenCV-自适应局部二值化";
    public static final String OpenCV_HistogramEq = "OpenCV-直方图均衡";
    public static final String OpenCV_Gradient_X = "OpenCV-X方向梯度";
    public static final String OpenCV_Gradient_Y = "OpenCV-Y方向梯度";
    public static final String OpenCV_Gradient_XY = "OpenCV-XY方向梯度";
    public static final String OpenCV_Canny_Edge = "OpenCV-Canny边缘";
    public static final String OpenCV_Hough_LineDet = "OpenCV-霍夫直线检测";
}
