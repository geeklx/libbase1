package com.geek.libmlkitscanner.new60;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.geek.libmlkitscanner.R;

public class QRCodeAct1 extends QRCodeCameraScanActivity1 {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.qrcode_scan_activity;
    }

    @Override
    public void initUI() {
        super.initUI();
        ll_scan_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getPackageName() + ".hs.act.ScanHistoryActivity"));
            }
        });
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPickPhoto();
            }
        });
    }


}
