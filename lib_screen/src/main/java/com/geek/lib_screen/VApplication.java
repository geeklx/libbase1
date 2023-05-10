<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/VApplication.java
package com.yanbo.lib_screen;
========
package com.geek.lib_screen;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/VApplication.java

import android.content.Context;


/**
 * Created by lzan13 on 2018/3/15.
 */
public class VApplication {
    protected static Context mcontext;
    public static Context getContext() {
        return mcontext;
    }
    public static void init(Context context) {
        mcontext=context;
//        ClingManager.getInstance().startClingService();
    }
}
