package cn.imeiadx.jsdk;

import android.webkit.JavascriptInterface;

import cn.imeiadx.jsdk.mob.JyJS;
import cn.imeiadx.jsdk.mob.WLog;


public class TestJs extends JyJS
{
	@JavascriptInterface  
	public void adcallback()
	{
		WLog.d("没有广告进行回调。");
	}

}
