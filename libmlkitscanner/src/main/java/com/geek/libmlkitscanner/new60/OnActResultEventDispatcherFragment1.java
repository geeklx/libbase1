package com.geek.libmlkitscanner.new60;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

public class OnActResultEventDispatcherFragment1 extends Fragment {
    public static final String TAG = "on_act_result_event_dispatcher";

    private SparseArray<ScanCallback1> mCallbacks = new SparseArray<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startForResult(Intent intent, ScanCallback1 callback) {
        mCallbacks.put(callback.hashCode(), callback);
        startActivityForResult(intent, callback.hashCode());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ScanCallback1 callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);

        if (callback != null) {
            callback.onActivityResult(resultCode, data);
        }
    }
}
