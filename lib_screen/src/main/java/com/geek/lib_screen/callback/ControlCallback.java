<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/callback/ControlCallback.java
package com.yanbo.lib_screen.callback;
========
package com.geek.lib_screen.callback;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/callback/ControlCallback.java

/**
 * Created by lzan13 on 2018/3/10.
 * 投屏控制回调
 */
public interface ControlCallback {
    void onSuccess();

    void onError(int code, String msg);
}
