package com.example.demo01.entity.msgModel;
//状态报告通知实体类
public class NotifyResponseModel {
    //发送失败还是成功
    private String code="error";
    //发送回执消息的发送方地址（原消息的目的方地址
    private String address;
    //源消息ID
    private String messageId;
    //消息发送状态
    private String deliveryStatus;
    private String description;

    private String text;
    //对应源消息resourceURL属性 rel。
    private String rel;
    //对应源消息resourceURL 属性 href。
    private String href;

    public NotifyResponseModel() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "NotifyResponseModel{" +
                "code=" + code +
                ", address='" + address + '\'' +
                ", messageId='" + messageId + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", rel='" + rel + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
