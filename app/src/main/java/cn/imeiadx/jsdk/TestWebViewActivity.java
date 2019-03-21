package cn.imeiadx.jsdk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.imeiadx.jsdk.mob.WMDownLoad;

/**
 * description :
 * author : LiuHuiJie
 * created on : 2019/3/21
 */
public class TestWebViewActivity extends AppCompatActivity {
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_webview);
        downTest();
    }

    private void runCommand() {
        try {
            Process process = Runtime.getRuntime().exec("pm list package -3");
            BufferedReader bis = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = bis.readLine()) != null) {
                System.out.println("MainActivity.runCommand, line=" + line);
            }
        } catch (IOException e) {
            System.out.println("MainActivity.runCommand,e=" + e);
        }
    }

    private void downTest() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else {
                goDown();
            }
        }
    }

    private void goDown() {
        String downUrl = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        WMDownLoad wm = new WMDownLoad(this);
        wm.downLoad(downUrl, path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            //judge
            goDown();
        }
    }
}
