package com.geek.libgoodview.blastgoodview.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午4:03
 * Application工具类
 * 功能：
 * 1.获取Application的Context
 * 2.获取App的Activity栈
 * 3.获取App最顶层Activity
 */
public class ApplicationUtils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private static final ActivityLifecycleImpl ACTIVITY_LIFECYCLE = new ActivityLifecycleImpl();

    private ApplicationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param context context
     */
    public static void init(final Context context) {
        if (context == null) {
            init(getApplicationByReflect());
            return;
        }
        init((Application) context.getApplicationContext());

    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    public static void init(final Application app) {
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }
            sApplication.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
        }
    }

    /**
     * Return the context of Application object.
     * 返回 Application 对象的上下文，此处为整个app的全局context
     *
     * @return the context of Application object
     */
    public static Application getApp() {
        if (sApplication != null) {
            return sApplication;
        }
        Application app = getApplicationByReflect();
        init(app);
        return app;
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    /**
     * 获取整个app主进程内的堆栈管理
     *
     * @return
     */
    public static ActivityLifecycleImpl getActivityLifecycle() {
        return ACTIVITY_LIFECYCLE;
    }

    public static LinkedList<Activity> getActivityList() {
        return ACTIVITY_LIFECYCLE.mActivityList;
    }

    /**
     * 用于判断某个页面是否在最上层
     *
     * @param activityClass 某个页面的Class
     * @return
     */
    public static boolean isTopActivity(Class activityClass) {
        LinkedList<Activity> activities = ACTIVITY_LIFECYCLE.mActivityList;
        if (activities.size() > 0) {
            if (activities.get(activities.size() - 1).getClass().getName().equals(activityClass.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取Activity的数量
     *
     * @return
     */
    public static int getActivitySize() {
        return ACTIVITY_LIFECYCLE.mActivityList.size();
    }

    /**
     * 获取除了某个Activity以外的Activity的数量
     *
     * @param activityClass 需要排除的Activity的Class
     * @return
     */
    public static int getActivitySizeExcept(Class activityClass) {
        LinkedList<Activity> activities = ACTIVITY_LIFECYCLE.mActivityList;
        boolean hasActivity = false;
        for (Activity activity : activities) {
            if (activity.getClass().getName().equals(activityClass.getName())) {
                hasActivity = true;
            }
        }
        if (hasActivity) {
            return activities.size() - 1;
        } else {
            return activities.size();
        }
    }

    /**
     * 是否栈里最多只有一个activity
     *
     * @return
     */
    public static boolean isActivityAlone() {
        return ACTIVITY_LIFECYCLE.mActivityList.size() <= 1;
    }

    /**
     * 检查是否存在对应的Activity
     *
     * @param activityClass 需要判断的Activity的Class
     * @return
     */
    public static boolean isActivityExist(Class activityClass) {
        LinkedList<Activity> activities = ACTIVITY_LIFECYCLE.mActivityList;
        if (activities.size() > 0) {
            for (Activity activity : activities) {
                if (activity.getClass().getName().equals(activityClass.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取栈底的Activity名称
     *
     * @return
     */
    public static String getLastActivityName() {
        return ACTIVITY_LIFECYCLE.lastActivityName;
    }

    static Context getTopActivityOrApp() {
        if (isAppForeground()) {
            Activity topActivity = ACTIVITY_LIFECYCLE.getTopActivity();
            return topActivity == null ? ApplicationUtils.getApp() : topActivity;
        } else {
            return ApplicationUtils.getApp();
        }
    }

    public static boolean isAppForeground() {
        ActivityManager am =
                (ActivityManager) ApplicationUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName.equals(ApplicationUtils.getApp().getPackageName());
            }
        }
        return false;
    }

    public static void startOtherApp(Context context, String packageName) {
        try {
            Intent intent;
            PackageManager packageManager = context.getPackageManager();
            intent = packageManager.getLaunchIntentForPackage(packageName);
            if (intent == null) {
                Toast.makeText(context, "App Not Found!", Toast.LENGTH_SHORT).show();
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

        final LinkedList<Activity> mActivityList = new LinkedList<>();
        private String lastActivityName = "";
        final HashMap<Object, OnAppStatusChangedListener> mStatusListenerMap = new HashMap<>();

        private int mForegroundCount = 0;
        private int mConfigCount = 0;

        public void addListener(final Object object, final OnAppStatusChangedListener listener) {
            mStatusListenerMap.put(object, listener);
        }

        public void removeListener(final Object object) {
            mStatusListenerMap.remove(object);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            lastActivityName = activity.getClass().getName();
            setTopActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            setTopActivity(activity);
            if (mForegroundCount <= 0) {
                postStatus(true);
            }
            if (mConfigCount < 0) {
                ++mConfigCount;
            } else {
                ++mForegroundCount;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setTopActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {/**/}

        @Override
        public void onActivityStopped(Activity activity) {
            if (activity.isChangingConfigurations()) {
                --mConfigCount;
            } else {
                --mForegroundCount;
                if (mForegroundCount <= 0) {
                    postStatus(false);
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {/**/}

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivityList.remove(activity);
        }

        private void postStatus(final boolean isForeground) {
            if (mStatusListenerMap.isEmpty()) {
                return;
            }
            for (OnAppStatusChangedListener onAppStatusChangedListener : mStatusListenerMap.values()) {
                if (onAppStatusChangedListener == null) {
                    return;
                }
                if (isForeground) {
                    onAppStatusChangedListener.onForeground();
                } else {
                    onAppStatusChangedListener.onBackground();
                }
            }
        }

        private void setTopActivity(final Activity activity) {
//            if (activity.getClass() == PermissionUtils.PermissionActivity.class) return;
            if (mActivityList.contains(activity)) {
                if (!mActivityList.getLast().equals(activity)) {
                    mActivityList.remove(activity);
                    mActivityList.addLast(activity);
                }
            } else {
                mActivityList.addLast(activity);
            }
        }

        Activity getTopActivity() {
            if (!mActivityList.isEmpty()) {
                final Activity topActivity = mActivityList.getLast();
                if (topActivity != null) {
                    return topActivity;
                }
            }
            Activity topActivityByReflect = getTopActivityByReflect();
            if (topActivityByReflect != null) {
                setTopActivity(topActivityByReflect);
            }
            return topActivityByReflect;
        }

        private Activity getTopActivityByReflect() {
            try {
                @SuppressLint("PrivateApi")
                Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
                Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
                Field activitiesField = activityThreadClass.getDeclaredField("mActivityList");
                activitiesField.setAccessible(true);
                Map activities = (Map) activitiesField.get(activityThread);
                if (activities == null) {
                    return null;
                }
                for (Object activityRecord : activities.values()) {
                    Class activityRecordClass = activityRecord.getClass();
                    Field pausedField = activityRecordClass.getDeclaredField("paused");
                    pausedField.setAccessible(true);
                    if (!pausedField.getBoolean(activityRecord)) {
                        Field activityField = activityRecordClass.getDeclaredField("activity");
                        activityField.setAccessible(true);
                        return (Activity) activityField.get(activityRecord);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    /**
     * Return the top activity in activity's stack.
     * 获取当前栈顶的Activity
     *
     * @return the top activity in activity's stack
     */
    public static Activity getTopActivity() {
        return ApplicationUtils.getActivityLifecycle().getTopActivity();
    }

    /**
     * 结束指定的Activity
     * @param activity
     */
    public static void killActivity(Activity activity) {
        if (activity != null && !getActivityList().isEmpty()) {
            if (getActivityList().contains(activity)) {
                getActivityList().remove(activity);
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * 结束指定类名的Activity
     * @param cls activity对应class
     */
    public static void killActivity(Class<?> cls) {
        if (cls != null && !getActivityList().isEmpty()) {
            for (int i = getActivityList().size() - 1; i >= 0; i--) {
                if (getActivityList().get(i).getClass().equals(cls)) {
                    killActivity(getActivityList().get(i));
                }
            }
        }
    }


    /**
     * 获取某个activity
     * @param cls activity对应class
     * @return 相应activity
     */
    public static Activity getActivity(Class<?> cls) {
        if (cls != null && !getActivityList().isEmpty()) {
            for (int i = getActivityList().size() - 1; i >= 0; i--) {
                if (getActivityList().get(i).getClass().equals(cls)) {
                    return getActivityList().get(i);
                }
            }
        }
        return null;
    }


    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    public interface OnAppStatusChangedListener {
        void onForeground();

        void onBackground();
    }

}

