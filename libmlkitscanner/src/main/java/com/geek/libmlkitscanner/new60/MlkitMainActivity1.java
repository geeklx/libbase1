package com.geek.libmlkitscanner.new60;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.libmlkitscanner.R;
import com.geek.libmlkitscanner.callback.act.MNScanCallback;

import java.util.ArrayList;

public class MlkitMainActivity1 extends AppCompatActivity {

    private TextView tv1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mlkit2);
        tv1 = findViewById(R.id.tv111);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanManager1.startScan(MlkitMainActivity1.this, new ScanCallback1() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (data == null) {
                            return;
                        }
                        handlerResult(resultCode, data);
                    }
                });
            }
        });
    }


    private void handlerResult(int resultCode, Intent data) {
        switch (resultCode) {
            case ScanManager1.RESULT_SUCCESS:
                ArrayList<String> results = data.getStringArrayListExtra(ScanManager1.INTENT_KEY_RESULT_SUCCESS);
                StringBuilder resultStr = new StringBuilder();
                String aaaa = "";
                for (int i = 0; i < results.size(); i++) {
                    resultStr.append("第" + (i + 1) + "条：");
                    resultStr.append(results.get(i));
                    resultStr.append("\n");
                    if (i==0){
                        aaaa = resultStr.toString();
                    }
                }
                Toast.makeText(MlkitMainActivity1.this, resultStr.toString(), Toast.LENGTH_LONG).show();
                tv1.setText(aaaa);
                break;
            case ScanManager1.RESULT_FAIL:
                String resultError = data.getStringExtra(ScanManager1.INTENT_KEY_RESULT_ERROR);
                Toast.makeText(MlkitMainActivity1.this, resultError, Toast.LENGTH_LONG).show();
                break;
            case ScanManager1.RESULT_CANCLE:
                Toast.makeText(MlkitMainActivity1.this, "取消扫码", Toast.LENGTH_LONG).show();
                break;
        }
    }

}
