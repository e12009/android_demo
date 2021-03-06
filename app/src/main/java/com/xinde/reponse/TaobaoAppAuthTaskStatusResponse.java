package com.xinde.reponse;

public class TaobaoAppAuthTaskStatusResponse<T> extends TaskStatusResponse<T> {
    private QR QR;

    public TaobaoAppAuthTaskStatusResponse.QR getQR() {
        return QR;
    }

    public void setQR(TaobaoAppAuthTaskStatusResponse.QR qr) {
        QR = qr;
    }

    @Override
    public String toString() {
        return "TaobaoAppAuthTaskStatusResponse{" +
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
