package com.xinde.reponse.taskresult;

import java.util.Date;
import java.util.List;

public class NetFlowHistory {
    private String month;
    private int totalBytes;
    private int totalFee;
    private boolean isCompleted;
    private List<NetFlowDetail> details;

    public NetFlowHistory() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public List<NetFlowDetail> getDetails() {
        return details;
    }

    public void setDetails(List<NetFlowDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "NetFlowHistory{" +
                "month='" + month + '\'' +
                ", totalBytes=" + totalBytes +
                ", totalFee=" + totalFee +
                ", isCompleted=" + isCompleted +
                ", details=" + details +
                '}';
    }

    public class NetFlowDetail extends BasicType {
        private Date start;
        private String region;
        private int duration;
        private String network;
        private int totalBytes;
        private int fee;

        // parent field
        private String month;

        public NetFlowDetail() {
        }

        @Override
        public int itemType() {
            return BasicType.TYPE_NETFLOW_DETAIL;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public int getTotalBytes() {
            return totalBytes;
        }

        public void setTotalBytes(int totalBytes) {
            this.totalBytes = totalBytes;
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
            return "NetFlowDetail{" +
                    "start=" + start +
                    ", region='" + region + '\'' +
                    ", duration=" + duration +
                    ", network='" + network + '\'' +
                    ", totalBytes=" + totalBytes +
                    ", fee=" + fee +
                    ", month='" + month + '\'' +
                    '}';
        }
    }
}
