package com.xinde.reponse.taskresult;

import java.util.Date;

public class ZhimafenDetail {
    private int zhimafen;
    private Date lastUpdateTime;


    public ZhimafenDetail() {
    }

    public int getZhimafen() {
        return zhimafen;
    }

    public void setZhimafen(int zhimafen) {
        this.zhimafen = zhimafen;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "ZhimafenDetail{" +
                "zhimafen=" + zhimafen +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
