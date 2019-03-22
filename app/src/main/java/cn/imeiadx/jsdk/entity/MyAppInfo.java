package cn.imeiadx.jsdk.entity;

import android.graphics.drawable.Drawable;

public class MyAppInfo {

    private Drawable image;
    private String packageName;

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    private String AppName;


}
