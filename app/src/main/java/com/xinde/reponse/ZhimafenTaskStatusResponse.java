package com.xinde.reponse;

/**
 * POJO object definition of response data regarding zhimafen task status
 *
 * check out xinde data API doc for details
 */
public class ZhimafenTaskStatusResponse<T> extends TaskStatusResponse<T> {
    private QR QR;


    public ZhimafenTaskStatusResponse.QR getQR() {
        return QR;
    }

    public void setQR(ZhimafenTaskStatusResponse.QR QR) {
        this.QR = QR;
    }

    @Override
    public String toString() {
        return "ZhimafenTaskStatusResponse{" +
                "QR=" + QR +
                ", tid='" + tid + '\'' +
                ", status='" + status + '\'' +
                ", phase='" + phase + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                ", need='" + need + '\'' +
                ", failCode=" + failCode +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }

    public class QR {
        private String url;

        public QR() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "QR{" +
                    "url='" + url + '\'' +
                    '}';
        }
    }


}
