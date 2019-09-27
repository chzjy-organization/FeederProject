package com.punuo.sip.request;

import android.util.Log;

import com.punuo.sip.SipConfig;
import com.punuo.sip.message.SipMessageFactory;
import com.punuo.sip.service.SipServiceManager;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.message.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by han.chen.
 * Date on 2019-08-12.
 **/
public class BaseSipRequest {
    private SipRequestType mSipRequestType;
    private Message mMessage;
    private int mCode;
    private String mReason;
    private boolean hasResponse = true; //sip请求是否有response
    private String mTargetResponse = "";
    private Timer mTimer = new Timer();

    public BaseSipRequest() {
    }

    public void setSipRequestType(SipRequestType sipRequestType) {
        mSipRequestType = sipRequestType;
    }

    public void setHasResponse(boolean hasResponse) {
        this.hasResponse = hasResponse;
    }

    public boolean hasResponse() {
        return hasResponse;
    }

    public void setTargetResponse(String targetResponse) {
        mTargetResponse = targetResponse;
    }

    public String getTargetResponse() {
        return mTargetResponse;
    }

    public void startCounting() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("Sip", mTargetResponse + "请求超时");
                SipServiceManager.getInstance().handleTimeOut(mTargetResponse, BaseSipRequest.this);
            }
        }, 3 * 1000);
    }

    public void response() {
        try {
            mTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setResponse(Message message, int code, String reason) {
        mMessage = message;
        mCode = code;
        mReason = reason;
    }

    /**
     * 获取远程目标地址
     *
     * @return NameAddress
     */
    public NameAddress getDestNameAddress() {
        return SipConfig.getServerAddress();
    }

    /**
     * 获取本地地址
     *
     * @return NameAddress
     */
    public NameAddress getLocalNameAddress() {
        return SipConfig.getUserNormalAddress();
    }

    /**
     * 获取消息体
     *
     * @return String
     */
    public String getBody() {
        return null;
    }

    public Message build() {
        switch (mSipRequestType) {
            case Register:
                return SipMessageFactory.createRegisterRequest(getDestNameAddress(), getLocalNameAddress(), getBody());
            case Subscribe:
                return SipMessageFactory.createSubscribeRequest(getDestNameAddress(), getLocalNameAddress(), getBody());
            case Notify:
                return SipMessageFactory.createNotifyRequest(getDestNameAddress(), getLocalNameAddress(), getBody());
            case Invite:
                return SipMessageFactory.createInviteRequest(getDestNameAddress(), getLocalNameAddress(), getBody());
            case Options:
                return SipMessageFactory.createOptionsRequest(getDestNameAddress(), getLocalNameAddress(), getBody());
            case Bye:
                return SipMessageFactory.createByeRequest(getDestNameAddress(), getLocalNameAddress());
            case Response:
                return SipMessageFactory.createResponse(mMessage, mCode, mReason, getBody());
            default:
                return null;
        }
    }
}
