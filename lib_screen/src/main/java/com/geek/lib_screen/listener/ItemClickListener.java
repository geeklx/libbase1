<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/listener/ItemClickListener.java
package com.yanbo.lib_screen.listener;
========
package com.geek.lib_screen.listener;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/listener/ItemClickListener.java

/**
 * Created by lzan13 on 2018/3/10.
 */
public abstract class ItemClickListener implements ICListener {

    @Override
    public abstract void onItemAction(int action, Object object);

    @Override
    public void onItemLongAction(int action, Object object) {
        
    }
}
