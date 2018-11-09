package com.xinde.reponse.taskresult;

import java.util.Date;
import java.util.List;

public class CallHistory {
    private String month;
    private boolean isCompleted;
    private List<CallDetail> details;

    public CallHistory() {
        this.month = null;
        this.isCompleted = false;
        this.details = null;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public List<CallDetail> getDetails() {
        return details;
    }

    public void setDetails(List<CallDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "CallHistory{" +
                "month='" + month + '\'' +
                ", isCompleted=" + isCompleted +
                ", details=" + details +
                '}';
    }

    public class CallDetail extends BasicType {
        private String otherPhone;
        private int callType;
        private String callLocation;
        private String commType;
        private Date startTime;
        private int duration;
        private int fee;

        // parent fields
        private String month;

        public CallDetail() {
        }

        @Override
        public int itemType() {
            return BasicType.TYPE_CALL_DETAIL;
        }

        public String getOtherPhone() {
            return otherPhone;
        }

        public void setOtherPhone(String otherPhone) {
            this.otherPhone = otherPhone;
        }

        public int getCallType() {
            return callType;
        }

        public void setCallType(int callType) {
            this.callType = callType;
        }

        public String getCallLocation() {
            return callLocation;
        }

        public void setCallLocation(String callLocation) {
            this.callLocation = callLocation;
        }

        public String getCommType() {
            return commType;
        }

        public void setCommType(String commType) {
            this.commType = commType;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        @Override
        public String toString() {
            return "CallDetail{" +
                    "otherPhone='" + otherPhone + '\'' +
                    ", callType=" + callType +
                    ", callLocation='" + callLocation + '\'' +
                    ", commType='" + commType + '\'' +
                    ", startTime=" + startTime +
                    ", duration=" + duration +
                    ", fee=" + fee +
                    ", month='" + month + '\'' +
                    '}';
        }
    }
}
