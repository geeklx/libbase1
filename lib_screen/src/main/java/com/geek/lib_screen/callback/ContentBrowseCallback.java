<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/callback/ContentBrowseCallback.java
package com.yanbo.lib_screen.callback;
========
package com.geek.lib_screen.callback;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/callback/ContentBrowseCallback.java

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.SortCriterion;

/**
 * Created by lzan13 on 2018/3/18.
 */
public abstract class ContentBrowseCallback extends Browse {

    public ContentBrowseCallback(Service service, String containerId) {
        super(service, containerId, BrowseFlag.DIRECT_CHILDREN, "*", 0, null,
                new SortCriterion(true, "dc:title"));
    }

    @Override
    public void updateStatus(Status status) {

    }
}
