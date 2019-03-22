package cn.imeiadx.jsdk;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import cn.imeiadx.jsdk.mob.JyAd;
import cn.imeiadx.jsdk.mob.JyAdListener;
import cn.imeiadx.jsdk.mob.JyAdPopWindow;
import cn.imeiadx.jsdk.mob.JyAdView;
import cn.imeiadx.jsdk.mob.WLog;
import cn.imeiadx.jsdk.test.JyWebViewActivity;
import cn.imeiadx.jsdk.test.TestJs;
import cn.imeiadx.jsdk.test.TestWebViewActivity;

public class MainActivity extends AppCompatActivity {

    private JyAdPopWindow mPopupWindow = null;
    private Activity act = null;
    // 位置ID
    private String pid = "DAOOVC5SHVFTXACPPLL0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        // if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
        // PackageManager.PERMISSION_GRANTED) {
        // // requestPermissions(
        // // new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
        // // REQUEST_CODE_ASK_PERMISSON);
        // }
        //

        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_FINE_LOCATION",
                getPackageName()));

        WLog.d(getPackageName());
        if (permission) {
            WLog.d("有这个权限");
        } else {
            WLog.d("木有这个权限");
        }

        TestJs js = new TestJs();
        act = this;
        JyAdView adv = JyAd.initNormalAdView(this, "MEJDNjhDMDJCMzJEQUI1", 320,
                50, js);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        params.width = 320;
        params.height = 50;
        params.gravity = params.gravity = Gravity.BOTTOM;
        this.addContentView(adv, params);

        Button openbtn = (Button) findViewById(R.id.openbtn);
        openbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPopupWindow == null) {
                    // new ColorDrawable(0x7DC0C0C0) 半透明灰色
                    mPopupWindow = JyAd.initPopWindow(act, pid, 300, 250, null,
                            new ColorDrawable(0x7DC0C0C0));
                    JyAdListener l = new JyAdListener() {
                        @Override
                        public void onClosed() {
                            // 加入广告插屏关闭时响应
                            WLog.d("JyAdListener.onClosed");
                            mPopupWindow = null;
                        }

                    };

                    mPopupWindow.setListener(l);
                }
            }
        });

        Button closebtn = (Button) findViewById(R.id.closebtn);
        closebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent = new Intent(this, JyWebViewActivity.class);
        startActivity(intent);
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}