package com.xinde.reponse.taskresult;

import java.util.Date;
import java.util.List;

public class TaobaoDetail {
    private UserInfo userinfo;
    private List<PurchaseHistory> taobaoHistory;

    public TaobaoDetail() {
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public List<PurchaseHistory> getTaobaoHistory() {
        return taobaoHistory;
    }

    public void setTaobaoHistory(List<PurchaseHistory> taobaoHistory) {
        this.taobaoHistory = taobaoHistory;
    }


    @Override
    public String toString() {
        return "TaobaoDetail{" +
                "userinfo=" + userinfo +
                ", taobaoHistory=" + taobaoHistory +
                '}';
    }

    public class UserInfo {
        private String username;
        private String userId;
        private String accountName;
        private String avatar;
        private String nickname;
        private String realname;
        private Date birthday;
        private int taoqizhi;
        private String residence;
        private String hometown;
        private int sellerAdmin;
        private boolean huaBeiReqSuccess;
        private boolean huaBeiSuccess;
        private String huaBeiCreditAmount;
        private String huaBeiTotalCreditAmount;
        private boolean jiebeiReqSuccess;
        private boolean jiebeiSuccess;
        private String jiebeiCreditAmount;
        private String jiebeiTotalCreditAmount;
        private int gender;
        private List<DeliveryAddress> deliveryAddresses;
        private AccountBindingInfo accountBindingInfo;


        public UserInfo() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public int getTaoqizhi() {
            return taoqizhi;
        }

        public void setTaoqizhi(int taoqizhi) {
            this.taoqizhi = taoqizhi;
        }

        public String getResidence() {
            return residence;
        }

        public void setResidence(String residence) {
            this.residence = residence;
        }

        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }

        public int getSellerAdmin() {
            return sellerAdmin;
        }

        public void setSellerAdmin(int sellerAdmin) {
            this.sellerAdmin = sellerAdmin;
        }

        public boolean isHuaBeiReqSuccess() {
            return huaBeiReqSuccess;
        }

        public void setHuaBeiReqSuccess(boolean huaBeiReqSuccess) {
            this.huaBeiReqSuccess = huaBeiReqSuccess;
        }

        public boolean isHuaBeiSuccess() {
            return huaBeiSuccess;
        }

        public void setHuaBeiSuccess(boolean huaBeiSuccess) {
            this.huaBeiSuccess = huaBeiSuccess;
        }

        public String getHuaBeiCreditAmount() {
            return huaBeiCreditAmount;
        }

        public void setHuaBeiCreditAmount(String huaBeiCreditAmount) {
            this.huaBeiCreditAmount = huaBeiCreditAmount;
        }

        public String getHuaBeiTotalCreditAmount() {
            return huaBeiTotalCreditAmount;
        }

        public void setHuaBeiTotalCreditAmount(String huaBeiTotalCreditAmount) {
            this.huaBeiTotalCreditAmount = huaBeiTotalCreditAmount;
        }

        public boolean isJiebeiReqSuccess() {
            return jiebeiReqSuccess;
        }

        public void setJiebeiReqSuccess(boolean jiebeiReqSuccess) {
            this.jiebeiReqSuccess = jiebeiReqSuccess;
        }

        public boolean isJiebeiSuccess() {
            return jiebeiSuccess;
        }

        public void setJiebeiSuccess(boolean jiebeiSuccess) {
            this.jiebeiSuccess = jiebeiSuccess;
        }

        public String getJiebeiCreditAmount() {
            return jiebeiCreditAmount;
        }

        public void setJiebeiCreditAmount(String jiebeiCreditAmount) {
            this.jiebeiCreditAmount = jiebeiCreditAmount;
        }

        public String getJiebeiTotalCreditAmount() {
            return jiebeiTotalCreditAmount;
        }

        public void setJiebeiTotalCreditAmount(String jiebeiTotalCreditAmount) {
            this.jiebeiTotalCreditAmount = jiebeiTotalCreditAmount;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public List<DeliveryAddress> getDeliveryAddresses() {
            return deliveryAddresses;
        }

        public void setDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
            this.deliveryAddresses = deliveryAddresses;
        }

        public AccountBindingInfo getAccountBindingInfo() {
            return accountBindingInfo;
        }

        public void setAccountBindingInfo(AccountBindingInfo accountBindingInfo) {
            this.accountBindingInfo = accountBindingInfo;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "username='" + username + '\'' +
                    ", userId='" + userId + '\'' +
                    ", accountName='" + accountName + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", realname='" + realname + '\'' +
                    ", birthday=" + birthday +
                    ", taoqizhi=" + taoqizhi +
                    ", residence='" + residence + '\'' +
                    ", hometown='" + hometown + '\'' +
                    ", sellerAdmin=" + sellerAdmin +
                    ", huaBeiReqSuccess=" + huaBeiReqSuccess +
                    ", huaBeiSuccess=" + huaBeiSuccess +
                    ", huaBeiCreditAmount='" + huaBeiCreditAmount + '\'' +
                    ", huaBeiTotalCreditAmount='" + huaBeiTotalCreditAmount + '\'' +
                    ", jiebeiReqSuccess=" + jiebeiReqSuccess +
                    ", jiebeiSuccess=" + jiebeiSuccess +
                    ", jiebeiCreditAmount='" + jiebeiCreditAmount + '\'' +
                    ", jiebeiTotalCreditAmount='" + jiebeiTotalCreditAmount + '\'' +
                    ", gender=" + gender +
                    ", deliveryAddresses=" + deliveryAddresses +
                    ", accountBindingInfo=" + accountBindingInfo +
                    '}';
        }

        public class DeliveryAddress {
            private String nickname;
            private String region;
            private String address;
            private String postcode;
            private String mobile;
            private String telephone;
            private boolean isDefault;

            public DeliveryAddress() {
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPostcode() {
                return postcode;
            }

            public void setPostcode(String postcode) {
                this.postcode = postcode;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public boolean isDefault() {
                return isDefault;
            }

            public void setDefault(boolean aDefault) {
                isDefault = aDefault;
            }

            @Override
            public String toString() {
                return "DeliveryAddress{" +
                        "nickname='" + nickname + '\'' +
                        ", region='" + region + '\'' +
                        ", address='" + address + '\'' +
                        ", postcode='" + postcode + '\'' +
                        ", mobile='" + mobile + '\'' +
                        ", telephone='" + telephone + '\'' +
                        ", isDefault=" + isDefault +
                        '}';
            }
        }

        public class  AccountBindingInfo {
            private String email;
            private String phone;
            private String accountType;
            private String authentication;


            public AccountBindingInfo() {
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAccountType() {
                return accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public String getAuthentication() {
                return authentication;
            }

            public void setAuthentication(String authentication) {
                this.authentication = authentication;
            }

            @Override
            public String toString() {
                return "AccountBindingInfo{" +
                        "email='" + email + '\'' +
                        ", phone='" + phone + '\'' +
                        ", accountType='" + accountType + '\'' +
                        ", authentication='" + authentication + '\'' +
                        '}';
            }
        }
    }

    public class PurchaseHistory {
        private String month;
        private List<Merchant> data;

        public PurchaseHistory() {
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public List<Merchant> getData() {
            return data;
        }

        public void setData(List<Merchant> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "PurchaseHistory{" +
                    "month='" + month + '\'' +
                    ", data=" + data +
                    '}';
        }

        public class Merchant {
            private String ID;
            private String type;
            private String status;
            private String merchantName;
            private String merchantUrl;
            private float totalCost;
            private String currency;
            private boolean orderIsDirty;
            private OrderDetailInfo orderDetail;
            private boolean logisticIsDirty;
            private LogisticDetailInfo logisticDetail;
            private List<Order> orders;

            public Merchant() {
            }

            public String getID() {
                return ID;
            }

            public void setID(String ID) {
                this.ID = ID;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getMerchantName() {
                return merchantName;
            }

            public void setMerchantName(String merchantName) {
                this.merchantName = merchantName;
            }

            public String getMerchantUrl() {
                return merchantUrl;
            }

            public void setMerchantUrl(String merchantUrl) {
                this.merchantUrl = merchantUrl;
            }

            public float getTotalCost() {
                return totalCost;
            }

            public void setTotalCost(float totalCost) {
                this.totalCost = totalCost;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public boolean isOrderIsDirty() {
                return orderIsDirty;
            }

            public void setOrderIsDirty(boolean orderIsDirty) {
                this.orderIsDirty = orderIsDirty;
            }

            public OrderDetailInfo getOrderDetail() {
                return orderDetail;
            }

            public void setOrderDetail(OrderDetailInfo orderDetail) {
                this.orderDetail = orderDetail;
            }

            public boolean isLogisticIsDirty() {
                return logisticIsDirty;
            }

            public void setLogisticIsDirty(boolean logisticIsDirty) {
                this.logisticIsDirty = logisticIsDirty;
            }

            public LogisticDetailInfo getLogisticDetail() {
                return logisticDetail;
            }

            public void setLogisticDetail(LogisticDetailInfo logisticDetail) {
                this.logisticDetail = logisticDetail;
            }

            public List<Order> getOrders() {
                return orders;
            }

            public void setOrders(List<Order> orders) {
                this.orders = orders;
            }

            @Override
            public String toString() {
                return "Merchant{" +
                        "ID='" + ID + '\'' +
                        ", type='" + type + '\'' +
                        ", status='" + status + '\'' +
                        ", merchantName='" + merchantName + '\'' +
                        ", merchantUrl='" + merchantUrl + '\'' +
                        ", totalCost=" + totalCost +
                        ", currency='" + currency + '\'' +
                        ", orderIsDirty=" + orderIsDirty +
                        ", orderDetail=" + orderDetail +
                        ", logisticIsDirty=" + logisticIsDirty +
                        ", logisticDetail=" + logisticDetail +
                        ", orders=" + orders +
                        '}';
            }

            public class OrderDetailInfo {
                private Logistic logistic;
                private Seller seller;
                private Buyer buyer;
                private Transaction transaction;

                public OrderDetailInfo() {
                }

                public Logistic getLogistic() {
                    return logistic;
                }

                public void setLogistic(Logistic logistic) {
                    this.logistic = logistic;
                }

                public Seller getSeller() {
                    return seller;
                }

                public void setSeller(Seller seller) {
                    this.seller = seller;
                }

                public Buyer getBuyer() {
                    return buyer;
                }

                public void setBuyer(Buyer buyer) {
                    this.buyer = buyer;
                }

                public Transaction getTransaction() {
                    return transaction;
                }

                public void setTransaction(Transaction transaction) {
                    this.transaction = transaction;
                }

                @Override
                public String toString() {
                    return "OrderDetailInfo{" +
                            "logistic=" + logistic +
                            ", seller=" + seller +
                            ", buyer=" + buyer +
                            ", transaction=" + transaction +
                            '}';
                }
            }

            public class LogisticDetailInfo {
                private Logistic logistic;
                private Seller seller;
                private Buyer buyer;

                public LogisticDetailInfo() {
                }

                public Logistic getLogistic() {
                    return logistic;
                }

                public void setLogistic(Logistic logistic) {
                    this.logistic = logistic;
                }

                public Seller getSeller() {
                    return seller;
                }

                public void setSeller(Seller seller) {
                    this.seller = seller;
                }

                public Buyer getBuyer() {
                    return buyer;
                }

                public void setBuyer(Buyer buyer) {
                    this.buyer = buyer;
                }

                @Override
                public String toString() {
                    return "LogisticDetailInfo{" +
                            "logistic=" + logistic +
                            ", seller=" + seller +
                            ", buyer=" + buyer +
                            '}';
                }
            }

            public class Order {
                private String ID;
                private String name;
                private int count;
                private String currency;
                private String url;
                private String imageUrl;
                private String snapshot;
                private List<SKU> sku;
                private Fee fee;

                public Order() {
                }

                public String getID() {
                    return ID;
                }

                public void setID(String ID) {
                    this.ID = ID;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public String getCurrency() {
                    return currency;
                }

                public void setCurrency(String currency) {
                    this.currency = currency;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }

                public String getSnapshot() {
                    return snapshot;
                }

                public void setSnapshot(String snapshot) {
                    this.snapshot = snapshot;
                }

                public List<SKU> getSku() {
                    return sku;
                }

                public void setSku(List<SKU> sku) {
                    this.sku = sku;
                }

                public Fee getFee() {
                    return fee;
                }

                public void setFee(Fee fee) {
                    this.fee = fee;
                }

                @Override
                public String toString() {
                    return "Order{" +
                            "ID='" + ID + '\'' +
                            ", name='" + name + '\'' +
                            ", count=" + count +
                            ", currency='" + currency + '\'' +
                            ", url='" + url + '\'' +
                            ", imageUrl='" + imageUrl + '\'' +
                            ", snapshot='" + snapshot + '\'' +
                            ", sku=" + sku +
                            ", fee=" + fee +
                            '}';
                }
            }
        }

        public class Logistic {
            private String shipingOrderNo;
            private String logisticCompanyName;
            private String callCenter;

            public Logistic() {
            }

            public String getShipingOrderNo() {
                return shipingOrderNo;
            }

            public void setShipingOrderNo(String shipingOrderNo) {
                this.shipingOrderNo = shipingOrderNo;
            }

            public String getLogisticCompanyName() {
                return logisticCompanyName;
            }

            public void setLogisticCompanyName(String logisticCompanyName) {
                this.logisticCompanyName = logisticCompanyName;
            }

            public String getCallCenter() {
                return callCenter;
            }

            public void setCallCenter(String callCenter) {
                this.callCenter = callCenter;
            }

            @Override
            public String toString() {
                return "Logistic{" +
                        "shipingOrderNo='" + shipingOrderNo + '\'' +
                        ", logisticCompanyName='" + logisticCompanyName + '\'' +
                        ", callCenter='" + callCenter + '\'' +
                        '}';
            }
        }

        public class Seller {
            private String nickname;
            private String address;
            private String postcode;
            private String storeName;
            private String telephone;

            public Seller() {
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPostcode() {
                return postcode;
            }

            public void setPostcode(String postcode) {
                this.postcode = postcode;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            @Override
            public String toString() {
                return "Seller{" +
                        "nickname='" + nickname + '\'' +
                        ", address='" + address + '\'' +
                        ", postcode='" + postcode + '\'' +
                        ", storeName='" + storeName + '\'' +
                        ", telephone='" + telephone + '\'' +
                        '}';
            }
        }

        public class Buyer {
            private String address;
            private String postcode;
            private String nickname;
            private String telephone;

            public Buyer() {
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPostcode() {
                return postcode;
            }

            public void setPostcode(String postcode) {
                this.postcode = postcode;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            @Override
            public String toString() {
                return "Buyer{" +
                        "address='" + address + '\'' +
                        ", postcode='" + postcode + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", telephone='" + telephone + '\'' +
                        '}';
            }
        }

        public class Transaction {
            private String alipayId;
            private Date createTime;
            private Date payTime;
            private Date deliveryTime;
            private Date confirmTime;
            private Date dealTime;

            public Transaction() {
            }

            public String getAlipayId() {
                return alipayId;
            }

            public void setAlipayId(String alipayId) {
                this.alipayId = alipayId;
            }

            public Date getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Date createTime) {
                this.createTime = createTime;
            }

            public Date getPayTime() {
                return payTime;
            }

            public void setPayTime(Date payTime) {
                this.payTime = payTime;
            }

            public Date getDeliveryTime() {
                return deliveryTime;
            }

            public void setDeliveryTime(Date deliveryTime) {
                this.deliveryTime = deliveryTime;
            }

            public Date getConfirmTime() {
                return confirmTime;
            }

            public void setConfirmTime(Date confirmTime) {
                this.confirmTime = confirmTime;
            }

            public Date getDealTime() {
                return dealTime;
            }

            public void setDealTime(Date dealTime) {
                this.dealTime = dealTime;
            }

            @Override
            public String toString() {
                return "Transaction{" +
                        "alipayId='" + alipayId + '\'' +
                        ", createTime=" + createTime +
                        ", payTime=" + payTime +
                        ", deliveryTime=" + deliveryTime +
                        ", confirmTime=" + confirmTime +
                        ", dealTime=" + dealTime +
                        '}';
            }
        }

        public class Fee {
            private float original;
            private float realTotal;

            public Fee() {
            }

            public float getOriginal() {
                return original;
            }

            public void setOriginal(float original) {
                this.original = original;
            }

            public float getRealTotal() {
                return realTotal;
            }

            public void setRealTotal(float realTotal) {
                this.realTotal = realTotal;
            }

            @Override
            public String toString() {
                return "Fee{" +
                        "original=" + original +
                        ", realTotal=" + realTotal +
                        '}';
            }
        }

        public class SKU {
            private String name;
            private String value;

            public SKU() {
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "SKU{" +
                        "name='" + name + '\'' +
                        ", value='" + value + '\'' +
                        '}';
            }
        }


    }
}
