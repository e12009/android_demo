package com.xinde.reponse.taskresult;

import java.util.Date;
import java.util.List;

public class SmsHistory {
    private String month;
    private boolean isCompleted;
    private List<SmsDetail> details;

    public SmsHistory() {
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

    public List<SmsDetail> getDetails() {
        return details;
    }

    public void setDetails(List<SmsDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "SmsHistory{" +
                "month='" + month + '\'' +
                ", isCompleted=" + isCompleted +
                ", details=" + details +
                '}';
    }

    public class SmsDetail extends BasicType {
        private String otherPhone;
        private int smsType;
        private Date date;
        private int fee;

        // parent field
        private String month;

        public SmsDetail() {
        }

        @Override
        public int itemType() {
            return BasicType.TYPE_SMS_DETAIL;
        }

        public String getOtherPhone() {
            return otherPhone;
        }

        public void setOtherPhone(String otherPhone) {
            this.otherPhone = otherPhone;
        }

        public int getSmsType() {
            return smsType;
        }

        public void setSmsType(int smsType) {
            this.smsType = smsType;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
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
            return "SmsDetail{" +
                    "otherPhone='" + otherPhone + '\'' +
                    ", smsType=" + smsType +
                    ", date=" + date +
                    ", fee=" + fee +
                    ", month='" + month + '\'' +
                    '}';
        }
    }
}
