package com.geek.libmlkitscanner.new60;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;

public class ActResultRequest1 {
    private OnActResultEventDispatcherFragment1 fragment;

    public ActResultRequest1(Activity activity) {
        fragment = getEventDispatchFragment(activity);
    }

    private OnActResultEventDispatcherFragment1 getEventDispatchFragment(Activity activity) {
        final FragmentManager fragmentManager = activity.getFragmentManager();

        OnActResultEventDispatcherFragment1 fragment = findEventDispatchFragment(fragmentManager);
        if (fragment == null) {
            fragment = new OnActResultEventDispatcherFragment1();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, OnActResultEventDispatcherFragment1.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private OnActResultEventDispatcherFragment1 findEventDispatchFragment(FragmentManager manager) {
        return (OnActResultEventDispatcherFragment1) manager.findFragmentByTag(OnActResultEventDispatcherFragment1.TAG);
    }

    public void startForResult(Intent intent, ScanCallback1 callback) {
        fragment.startForResult(intent, callback);
    }

}
