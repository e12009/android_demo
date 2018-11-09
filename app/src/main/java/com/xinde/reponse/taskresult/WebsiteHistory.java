package com.xinde.reponse.taskresult;

import java.util.Date;
import java.util.List;

public class WebsiteHistory {
    private String month;
    private boolean isCompleted;
    private List<WebsiteDetail> details;

    public WebsiteHistory() {
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

    public List<WebsiteDetail> getDetails() {
        return details;
    }

    public void setDetails(List<WebsiteDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "WebsiteHistory{" +
                "month='" + month + '\'' +
                ", isCompleted=" + isCompleted +
                ", details=" + details +
                '}';
    }

    public class WebsiteDetail extends BasicType {
        private Date start;
        private String category;
        private String network;
        private String url;
        private String name;

        // parent field
        private String month;

        public WebsiteDetail() {
        }

        @Override
        public int itemType() {
            return BasicType.TYPE_WEB_DETAIL;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        @Override
        public String toString() {
            return "WebsiteDetail{" +
                    "start=" + start +
                    ", category='" + category + '\'' +
                    ", network='" + network + '\'' +
                    ", url='" + url + '\'' +
                    ", name='" + name + '\'' +
                    ", month='" + month + '\'' +
                    '}';
        }
    }
}
