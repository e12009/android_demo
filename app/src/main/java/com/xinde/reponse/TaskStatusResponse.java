package com.xinde.reponse;

import java.util.Date;

public class TaskStatusResponse<T> {
    protected String tid = null;
    protected String status = null;
    protected String phase = null;
    protected Date lastUpdateTime = null;
    protected String need = null;
    protected int failCode = 0;
    protected String reason = null;

    protected T result = null;

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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
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
                ", result=" + result +
                '}';
    }
}
