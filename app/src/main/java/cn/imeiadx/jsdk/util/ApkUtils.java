package cn.imeiadx.jsdk.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.imeiadx.jsdk.entity.MyAppInfo;

public class ApkUtils {
    static String TAG="ApkTool";
    public static List mLocalInstallApps=null;
    public static List scanLocalInstallAppList(PackageManager packageManager) {
        List myAppInfos = new ArrayList();
        try {
            List packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = (PackageInfo) packageInfos.get(i);
//过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                MyAppInfo myAppInfo = new MyAppInfo();
                myAppInfo.setPackageName(packageInfo.packageName);
                myAppInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(myAppInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "===============获取应用包信息失败");
        }
        return myAppInfos;
    }

}
