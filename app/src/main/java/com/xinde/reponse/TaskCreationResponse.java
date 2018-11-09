package com.xinde.reponse;

import java.util.Date;

public class TaskCreationResponse {
    private String tid = null;
    private String status = null;
    private String phase = null;
    private String type = null;
    private Date lastUpdateTime = null;

    public TaskCreationResponse() {

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "TaskCreationResponse{" +
                "tid='" + tid + '\'' +
                ", status='" + status + '\'' +
                ", phase='" + phase + '\'' +
                ", type='" + type + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
