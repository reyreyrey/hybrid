package com.ivi.hybrid.core.push.model;


/**
 * author: Rea.X
 * date: 2017/3/10.
 */

public class PushMessage{

    //消息id
    private String messageId;
    //消息内容
    private byte[] messageBytes;
    //消息顺序
    private int messageOrder;
    //是否推送过了
    private int isShowed;
    //是否结束
    private int finish;
    //是不是只有一条信息
    private int isOnlgMessage;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }


    public byte[] getMessageBytes() {
        return messageBytes;
    }

    public void setMessageBytes(byte[] messageBytes) {
        this.messageBytes = messageBytes;
    }

    public int getMessageOrder() {
        return messageOrder;
    }

    public void setMessageOrder(int messageOrder) {
        this.messageOrder = messageOrder;
    }

    public int getIsShowed() {
        return isShowed;
    }

    public void setIsShowed(int isShowed) {
        this.isShowed = isShowed;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getIsOnlgMessage() {
        return isOnlgMessage;
    }

    public void setIsOnlgMessage(int isOnlgMessage) {
        this.isOnlgMessage = isOnlgMessage;
    }
}
