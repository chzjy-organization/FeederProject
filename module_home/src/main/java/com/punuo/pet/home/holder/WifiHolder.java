package com.punuo.pet.home.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.hacknife.wifimanager.IWifi;
import com.punuo.pet.home.R;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;

/**
 * Created by han.chen.
 * Date on 2020/8/21.
 **/
public class WifiHolder extends BaseViewHolder<IWifi> {
    private TextView wifiName;
    private TextView wifiDesc;
    public WifiHolder(@NonNull View itemView) {
        super(itemView);
        wifiName = itemView.findViewById(R.id.tv_name);
        wifiDesc = itemView.findViewById(R.id.tv_desc);
    }

    @Override
    protected void bindData(IWifi iWifi, int position) {
        wifiName.setText(iWifi.name());
        wifiDesc.setText(iWifi.description());
    }
}
