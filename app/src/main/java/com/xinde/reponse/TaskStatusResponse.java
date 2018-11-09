package com.xinde.reponse;

import java.util.Date;

public class TaskStatusResponse {
    private String tid = null;
    private String status = null;
    private String phase = null;
    private Date lastUpdateTime = null;
    private String need = null;
    private int failCode = 0;
    private String reason = null;

    //TODO:
    // private Object result = null;

    public TaskStatusResponse() {

    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public int getFailCode() {
        return failCode;
    }

    public void setFailCode(int failCode) {
        this.failCode = failCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isLogin() {
        return isProcessing() && "login".equalsIgnoreCase(phase);
    }

    public boolean isFetching() {
        return isProcessing() && "fetching".equalsIgnoreCase(phase);
    }

    public boolean isProcessing() {
        return "processing".equalsIgnoreCase(status);
    }

    public boolean isSuspended() {
        return "suspended".equalsIgnoreCase(status);
    }

    public boolean isFailed() {
        return "failed".equalsIgnoreCase(status);
    }

    public boolean isDone() {
        return "done".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "TaskStatusResponse{" +
                "tid='" + tid + '\'' +
                ", status='" + status + '\'' +
                ", phase='" + phase + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                ", need='" + need + '\'' +
                ", failCode=" + failCode +
                ", reason='" + reason + '\'' +
                '}';
    }
}
