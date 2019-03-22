package cn.imeiadx.jsdk.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.imeiadx.jsdk.R;
import cn.imeiadx.jsdk.entity.MyAppInfo;
import cn.imeiadx.jsdk.util.ApkUtils;

public class JyWebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jy_webview);

        initView();

    }

    private void initView() {
        mWebView = findViewById(R.id.webView);
        mWebView.setInitialScale(100);
        // 去掉空白
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        mWebSetting = mWebView.getSettings();
        mWebSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 允许JS
        mWebSetting.setJavaScriptEnabled(true);
        //settings.setMediaPlaybackRequiresUserGesture(false);
        //settings.setMediaPlaybackRequiresUserGesture(false);
        //1：1显示
        //settings.setUseWideViewPort(true);
        //settings.setLoadWithOverviewMode(true);
        //settings.setSupportZoom(false);

        mWebView.loadUrl("https://www.baidu.com");
        mWebView.addJavascriptInterface(new JsObject(), "android");
        // getJson();
        //  List<MyAppInfo> myAppInfoList = ApkUtils.scanLocalInstallAppList(getPackageManager());
        //  System.out.println("app_message" + myAppInfoList.toString());
        //getNearUseApp();
        // showRecentApps();
        //getNear();
        // getNearUseApp();
        // getNbNearUseApp();
       // getNear();
        getAppStatus();

    }

    private void initData() {


    }

    private class JsObject {
        @JavascriptInterface
        public void nativeEZPlay(String cameraInfoJson) {

        }

    }

    //5.0之前
    private void getNearUseApp() {
        PackageManager pm = this.getPackageManager();
        ActivityManager mActivityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RecentTaskInfo> appList4 = mActivityManager
                .getRecentTasks(10, ActivityManager.RECENT_WITH_EXCLUDED);//参数，前一个是你要取的最大数，后一个是状态
        for (ActivityManager.RecentTaskInfo running : appList4) {
            Intent intent = running.baseIntent;
            ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (resolveInfo != null) {
                System.out.println(resolveInfo.activityInfo.packageName + "-hhhhhh");//获取应用包名
                System.out.println(resolveInfo.loadLabel(pm).toString() + "-nhhhhh");//获取应用名
// System.out.println(resolveInfo.loadIcon(pm) "n");//获取应用头标

            }
        }
    }

    //5.0之后
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void getNbNearUseApp() {
        PackageManager pm = this.getPackageManager();
        ActivityManager mActivityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        // List<ActivityManager.RecentTaskInfo> appList4 = mActivityManager
        //      .getRecentTasks(10, ActivityManager.RECENT_WITH_EXCLUDED);//参数，前一个是你要取的最大数，后一个是状态
        List<ActivityManager.AppTask> tasks = mActivityManager.getAppTasks();
        for (ActivityManager.AppTask task : tasks) {
            ActivityManager.RecentTaskInfo taskInfo = task.getTaskInfo();
            Intent intent = taskInfo.baseIntent;
            ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (resolveInfo != null) {
                System.out.println(resolveInfo.activityInfo.packageName + "-hhhhhh");//获取应用包名
                System.out.println(resolveInfo.loadLabel(pm).toString() + "-nhhhhh");//获取应用名
// System.out.println(resolveInfo.loadIcon(pm) "n");//获取应用头标

            }
        }
    }

    private void showRecentApps() {
        Class serviceManagerClass;
        try {
            serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClass.getMethod("getService",
                    String.class);
            IBinder retbinder = null;
            try {
                retbinder = (IBinder) getService.invoke(
                        serviceManagerClass, "statusbar");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            Class statusBarClass = null;
            try {
                statusBarClass = Class.forName(retbinder
                        .getInterfaceDescriptor());
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Object statusBarObject = statusBarClass.getClasses()[0].getMethod(
                    "asInterface", IBinder.class).invoke(null,
                    new Object[]{retbinder});
            Method clearAll = statusBarClass.getMethod("toggleRecentApps");
            clearAll.setAccessible(true);
            clearAll.invoke(statusBarObject);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void getJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("package_list", getPackageList());   // 获得JSONObject的String
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("json---" + jsonObject.toString());
       /* List<String> list=new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        JSONArray jsonArray = new JSONArray();

        int count = list.size();
        for(int i = 0; i < count; i++)
        {
            jsonArray.put(list.get(i));
        }
        String array = jsonArray.toString(); // 将JSONArray转换得到String

        List<String> list2=new ArrayList<>();
        list2.add("0");
        list2.add("1");
        list2.add("2");
        JSONArray jsonArray2 = new JSONArray();

        int count2 = list2.size();
        for(int i = 0; i < count2; i++)
        {
            jsonArray2.put(list2.get(i));
        }
        String array2 = jsonArray2.toString(); // 将JSONArray转换得到String

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("list" , array);   // 获得JSONObject的String
            jsonObject.put("list2",array2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }

    private JSONArray getPackageList() {
        JSONArray array = new JSONArray();
        try {
            Process process = Runtime.getRuntime().exec("pm list package -3");
            BufferedReader bis = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bis.readLine()) != null) {
                System.out.println("MainActivity.runCommand, line=" + line);
                array.put(line);
            }
        } catch (IOException e) {
            System.out.println("MainActivity.runCommand,e=" + e);
        }
        return array;
    }


    private void getNear() {
        /**
         * 核心方法，加载最近启动的应用程序 注意：这里我们取出的最近任务为 MAX_RECENT_TASKS +
         * 1个，因为有可能最近任务中包好Launcher2。 这样可以保证我们展示出来的 最近任务 为 MAX_RECENT_TASKS 个
         * 通过以下步骤，可以获得近期任务列表，并将其存放在了appInfos这个list中，接下来就是展示这个list的工作了。
         */
        int appNumber = 10;
        List<HashMap<String, Object>> appInfos = new ArrayList<>();
        int MAX_RECENT_TASKS = appNumber; // allow for some discards
        int repeatCount = appNumber;// 保证上面两个值相等,设定存放的程序个数

        /* 每次加载必须清空list中的内容 */
        appInfos.removeAll(appInfos);

        // 得到包管理器和activity管理器
        final Context context = this.getApplication();
        final PackageManager pm = context.getPackageManager();
        final ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        // 从ActivityManager中取出用户最近launch过的 MAX_RECENT_TASKS + 1 个，以从早到晚的时间排序，
        // 注意这个 0x0002,它的值在launcher中是用ActivityManager.RECENT_IGNORE_UNAVAILABLE
        // 但是这是一个隐藏域，因此我把它的值直接拷贝到这里
        final List<ActivityManager.RecentTaskInfo> recentTasks = am
                .getRecentTasks(MAX_RECENT_TASKS + 1, 0x0002);

        // 这个activity的信息是我们的launcher
        ActivityInfo homeInfo = new Intent(Intent.ACTION_MAIN).addCategory(
                Intent.CATEGORY_HOME).resolveActivityInfo(pm, 0);
        int numTasks = recentTasks.size();
        for (int i = 0; i < numTasks && (i < MAX_RECENT_TASKS); i++) {
            HashMap<String, Object> singleAppInfo = new HashMap<String, Object>();// 当个启动过的应用程序的信息
            final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

            Intent intent = new Intent(info.baseIntent);
            if (info.origActivity != null) {
                intent.setComponent(info.origActivity);
            }
            /**
             * 如果找到是launcher，直接continue，后面的appInfos.add操作就不会发生了
             */
            if (homeInfo != null) {
                if (homeInfo.packageName.equals(intent.getComponent()
                        .getPackageName())
                        && homeInfo.name.equals(intent.getComponent()
                        .getClassName())) {
                    MAX_RECENT_TASKS = MAX_RECENT_TASKS + 1;
                    continue;
                }
            }
            // 设置intent的启动方式为 创建新task()【并不一定会创建】
            intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            // 获取指定应用程序activity的信息(按我的理解是：某一个应用程序的最后一个在前台出现过的activity。)
            final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (resolveInfo != null) {
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                final String title = activityInfo.loadLabel(pm).toString();
                Drawable icon = activityInfo.loadIcon(pm);

                if (title != null && title.length() > 0 && icon != null) {
                    singleAppInfo.put("title", title);
                    singleAppInfo.put("icon", icon);
                    singleAppInfo.put("tag", intent.getAction());
                    singleAppInfo.put("packageName", activityInfo.packageName);
                    appInfos.add(singleAppInfo);
                }
            }
        }
        MAX_RECENT_TASKS = repeatCount;
        System.out.println(appInfos.get(0).toString());
        System.out.println(appInfos.toString());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getAppStatus() {
        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.HOUR_OF_DAY, -1);
        Calendar endCal = Calendar.getInstance();
        UsageStatsManager manager = (UsageStatsManager) getApplicationContext().getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());

        StringBuilder sb = new StringBuilder();
        for (UsageStats us : stats) {
            try {
                PackageManager pm = getApplicationContext().getPackageManager();
                ApplicationInfo applicationInfo = pm.getApplicationInfo(us.getPackageName(), PackageManager.GET_META_DATA);
                if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0) {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String t = format.format(new Date(us.getLastTimeUsed()));
                    sb.append(pm.getApplicationLabel(applicationInfo) + "\t" + t + "\t" + (us.getTotalTimeInForeground()) + "\n");
                    System.out.println(pm.getApplicationLabel(applicationInfo)+"======"+us.getTotalTimeInForeground());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("====="+sb+"======");
    }

    private boolean isNoOption() {
        PackageManager packageManager = getApplicationContext()
                .getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}
