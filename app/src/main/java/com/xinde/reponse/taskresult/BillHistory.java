package com.xinde.reponse.taskresult;

import java.util.Date;
import java.util.List;

public class BillHistory {
    private String month;
    private Date startDate;
    private Date endDate;
    private int totalFee;
    private boolean isCompleted;
    private List<BillDetail> details;

    public BillHistory() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public List<BillDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BillDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "BillHistory{" +
                "month='" + month + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalFee=" + totalFee +
                ", isCompleted=" + isCompleted +
                ", details=" + details +
                '}';
    }

    public class BillDetail extends BasicType {
        private String billEntry;
        private int billEntryValue;

        // parent field
        private String month;

        public BillDetail() {
        }

        @Override
        public int itemType() {
            return BasicType.TYPE_BILL_DETAIL;
        }

        public String getBillEntry() {
            return billEntry;
        }

        public void setBillEntry(String billEntry) {
            this.billEntry = billEntry;
        }

        public int getBillEntryValue() {
            return billEntryValue;
        }

        public void setBillEntryValue(int billEntryValue) {
            this.billEntryValue = billEntryValue;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        @Override
        public String toString() {
            return "BillDetail{" +
                    "billEntry='" + billEntry + '\'' +
                    ", billEntryValue=" + billEntryValue +
                    ", month='" + month + '\'' +
                    '}';
        }
    }
}
