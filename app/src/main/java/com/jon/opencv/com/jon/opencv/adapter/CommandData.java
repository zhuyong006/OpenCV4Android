package com.jon.opencv.com.jon.opencv.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HASEE on 2019/3/3.
 */
public class CommandData implements CommandConstants{
    private long id;
    private String command;
    private String name;

    public CommandData(long id, String command) {
        this.id = id;
        this.command = command;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static List<CommandData> getCommandList() {
        int index = 0;
        List<CommandData> cList = new ArrayList<>();
        cList.add(new CommandData(index++,OpenCV_EnvTest));
        cList.add(new CommandData(index++,OpenCV_SaltNoise));
        cList.add(new CommandData(index++,OpenCV_InvertPixelSlow));
        cList.add(new CommandData(index++,OpenCV_InvertPixelFast));
        cList.add(new CommandData(index++,OpenCV_InvertBitmap));
        cList.add(new CommandData(index++,OpenCV_MatSubstract));
        cList.add(new CommandData(index++,OpenCV_MatAdd));
        cList.add(new CommandData(index++,OpenCV_BrightContrastAdjust));
        cList.add(new CommandData(index++,OpenCV_MatDemo));
        cList.add(new CommandData(index++,OpenCV_GetSubMat));
        cList.add(new CommandData(index++,OpenCV_MeanBlur));
        cList.add(new CommandData(index++,OpenCV_MediaBlur));
        cList.add(new CommandData(index++,OpenCV_GaussianBlur));
        cList.add(new CommandData(index++,OpenCV_BilateralFilter));
        cList.add(new CommandData(index++,OpenCV_CustomMeanBlur));
        cList.add(new CommandData(index++,OpenCV_EdgeDetect));
        cList.add(new CommandData(index++,OpenCV_Sharpen));
        cList.add(new CommandData(index++,OpenCV_Erode));
        cList.add(new CommandData(index++,OpenCV_Dilate));
        cList.add(new CommandData(index++,OpenCV_Morph_Open));
        cList.add(new CommandData(index++,OpenCV_Morph_Close));
        cList.add(new CommandData(index++,OpenCV_Morph_Line_Detect));
        cList.add(new CommandData(index++,OpenCV_Binary));
        cList.add(new CommandData(index++,OpenCV_AdaptiveBinary));
        cList.add(new CommandData(index++,OpenCV_HistogramEq));
        cList.add(new CommandData(index++,OpenCV_Gradient_X));
        cList.add(new CommandData(index++,OpenCV_Gradient_Y));
        cList.add(new CommandData(index++,OpenCV_Gradient_XY));
        cList.add(new CommandData(index++,OpenCV_Canny_Edge));
        cList.add(new CommandData(index++,OpenCV_Hough_LineDet));
        cList.add(new CommandData(index++,OpenCV_Hough_CircleDet));
        cList.add(new CommandData(index++,OpenCV_Template_Match));
        return cList;
    }
}
