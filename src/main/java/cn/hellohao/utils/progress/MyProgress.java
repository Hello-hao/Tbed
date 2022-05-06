package cn.hellohao.utils.progress;

import java.util.ArrayList;
import java.util.List;

public class MyProgress {

    //删除图片时候的数据
    private int delSuccessCount=0;//已经成功的个数
    private List<String> delErrorImgListt =  new ArrayList<>();//已经失败的图片
    private boolean delOCT=false; //控制开关
    private List<Integer> delSuccessImgList =  new ArrayList<>(); //删除成功的图片

    public void InitializeDelImg(){
        //删除图片时候的数据
        this.delSuccessCount=0;//已经成功的个数
        this.delErrorImgListt =  new ArrayList<>();//已经失败的图片
        this.delOCT=false; //控制开关
        this.delSuccessImgList =  new ArrayList<>(); //删除成功的图片
    }

    public MyProgress() {
    }

    public MyProgress(int delSuccessCount, List<String> delErrorImgListt, boolean delOCT, List<Integer> delSuccessImgList) {

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

    public boolean getDelOCT() {
        return delOCT;
    }

    public void setDelOCT(boolean delOCT) {
        this.delOCT = delOCT;
    }

    public List<Integer> getDelSuccessImgList() {
        return delSuccessImgList;
    }

    public void setDelSuccessImgList(List<Integer> delSuccessImgList) {
        this.delSuccessImgList = delSuccessImgList;
    }

    @Override
    public String toString() {
        return "MyProgress{" +
                ", delSuccessCount=" + delSuccessCount +
                ", delErrorImgListt=" + delErrorImgListt +
                ", delOCT=" + delOCT +
                ", delSuccessImgList=" + delSuccessImgList +
                '}';
    }
}