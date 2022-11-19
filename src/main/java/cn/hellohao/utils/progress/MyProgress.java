package cn.hellohao.utils.progress;

import java.util.ArrayList;
import java.util.List;

public class MyProgress {
    private int delSuccessCount=0;//已经成功的个数
    private List<String> delErrorImgListt =  new ArrayList<>();//已经失败的图片
    private int delOCT=0; //控制开关
    private List<Integer> delSuccessImgList =  new ArrayList<>(); //删除成功的图片

    public void InitializeDelImg(){
        this.delSuccessCount=0;//已经成功的个数
        this.delErrorImgListt =  new ArrayList<>();//已经失败的图片
        this.delOCT=0; //控制开关 0代表false 1代表true
        this.delSuccessImgList =  new ArrayList<>(); //删除成功的图片
    }

    public MyProgress() {
    }

    public MyProgress(int delSuccessCount, List<String> delErrorImgListt, int delOCT, List<Integer> delSuccessImgList) {
        this.delSuccessCount = delSuccessCount;
        this.delErrorImgListt = delErrorImgListt;
        this.delOCT = delOCT;
        this.delSuccessImgList = delSuccessImgList;
    }

    public int getDelSuccessCount() {
        return delSuccessCount;
    }

    public void setDelSuccessCount(int delSuccessCount) {
        this.delSuccessCount = delSuccessCount;
    }

    public List<String> getDelErrorImgListt() {
        return delErrorImgListt;
    }

    public void setDelErrorImgListt(List<String> delErrorImgListt) {
        this.delErrorImgListt = delErrorImgListt;
    }

    public int getDelOCT() {
        return delOCT;
    }

    public void setDelOCT(int delOCT) {
        this.delOCT = delOCT;
    }

    public List<Integer> getDelSuccessImgList() {
        return delSuccessImgList;
    }

    public void setDelSuccessImgList(List<Integer> delSuccessImgList) {
        this.delSuccessImgList = delSuccessImgList;
    }


}