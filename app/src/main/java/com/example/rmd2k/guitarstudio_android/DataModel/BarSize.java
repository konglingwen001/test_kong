package com.example.rmd2k.guitarstudio_android.DataModel;

/**
 * Created by CHT1HTSH3236 on 2018/4/19.
 */

public class BarSize {

    private float barWidth;
    private int minimNum;
    private int crotchetaNum;
    private int quaverNum;
    private int demiquaverNum;

    public BarSize() {
    }

    public float getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(float barWidth) {
        this.barWidth = barWidth;
    }

    public int getMinimNum() {
        return minimNum;
    }

    public void setMinimNum(int minimNum) {
        this.minimNum = minimNum;
    }

    public int getCrotchetaNum() {
        return crotchetaNum;
    }

    public void setCrotchetaNum(int crotchetaNum) {
        this.crotchetaNum = crotchetaNum;
    }

    public int getQuaverNum() {
        return quaverNum;
    }

    public void setQuaverNum(int quaverNum) {
        this.quaverNum = quaverNum;
    }

    public int getDemiquaverNum() {
        return demiquaverNum;
    }

    public void setDemiquaverNum(int demiquaverNum) {
        this.demiquaverNum = demiquaverNum;
    }
}
