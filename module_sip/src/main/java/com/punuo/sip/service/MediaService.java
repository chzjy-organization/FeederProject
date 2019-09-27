package com.punuo.sip.service;

import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.H264Config;
import com.punuo.sip.model.MediaData;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.zoolu.sip.message.Message;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;

/**
 * Created by han.chen.
 * Date on 2019-08-21.
 **/
@Route(path = ServicePath.PATH_MEDIA)
public class MediaService extends NormalRequestService<MediaData> {

    @Override
    protected String getBody() {
        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();
        try {
            value.put("resolution", "Feed_Device");
            value.put("video", "H.264");
            value.put("audio", "G.711");
            value.put("kbps", 800);
            value.put("self", "192.168.1.129 UDP 5000");
            value.put("mode", "active");
            value.put("magic", "01234567890123456789012345678901");
            value.put("dev_type", H264Config.VIDEO_TYPE);
            body.put("media", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonToXml jsonToXml = new JsonToXml.Builder(body).build();
        return jsonToXml.toFormattedString();
    }

    @Override
    protected void onSuccess(Message msg, MediaData result) {
        Log.d("han.chen", "Media 收到视频请求");
        if (result != null) {
            onResponse(msg);
            H264Config.initMediaData(result);
            //TODO 开启接收视频
            ARouter.getInstance().build(VideoRouter.ROUTER_VIDEO_PLAY_ACTIVITY)
                    .navigation();
        }
    }

    @Override
    protected void onError(Exception e) {
        HandlerExceptionUtils.handleException(e);
    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {

    }
}
