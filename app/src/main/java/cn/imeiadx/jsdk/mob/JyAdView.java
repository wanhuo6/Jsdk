package cn.imeiadx.jsdk.mob;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

public class JyAdView extends WebView
{
	private String placeid;
	private int width;
	private int height;
	private int adtype;
	
	public JyAdView(Context context, Activity act, String placeid, int adtype, int width, int height)
	{
		this(context,act,placeid,adtype,width,height,null);
	}

	public JyAdView(Context context, Activity act, String placeid, int adtype, int width, int height, JyJS jse)
	{
		super(context);
		// 广告位ID
		this.placeid = placeid;
		this.adtype = adtype;
		
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT);
		
		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		// 取得宽高
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);

		params.width = width;
		params.height = height;

		this.width = width;
		this.height = height;

		/*
		// 设置浏览器
		webview.getSettings()
				.setUserAgentString(
						"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.41 Safari/537.36");
		*/
		// 不读取缓存 所有内容均从网络下载
		WebSettings settings = getSettings();
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// 允许JS
		settings.setJavaScriptEnabled(true);
		//settings.setMediaPlaybackRequiresUserGesture(false);
		//settings.setMediaPlaybackRequiresUserGesture(false);
		//1：1显示
        //settings.setUseWideViewPort(true); 
        //settings.setLoadWithOverviewMode(true); 
        //settings.setSupportZoom(false);
		setInitialScale(100);
        
		//设置按1:1尺寸显示 有问题不能用
		//settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 

		settings.setUserAgentString(String.format("%s jyad_android", settings.getUserAgentString()));

        /*
        int screenDensity = getResources().getDisplayMetrics().densityDpi ;   
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM ;   
        switch (screenDensity){   
        case DisplayMetrics.DENSITY_LOW :  
            zoomDensity = WebSettings.ZoomDensity.CLOSE;  
            break;  
        case DisplayMetrics.DENSITY_MEDIUM:  
            zoomDensity = WebSettings.ZoomDensity.MEDIUM;  
            break;  
        case DisplayMetrics.DENSITY_HIGH:  
            zoomDensity = WebSettings.ZoomDensity.FAR;  
            break ;  
        }  
        settings.setDefaultZoom(zoomDensity); 
        settings.setDisplayZoomControls(true);
        //*/
        

		// 隐身滚动条
		setVerticalScrollBarEnabled(false);
		// 竖滚动屏蔽
		setVerticalScrollBarEnabled(false);
		// 横滚动屏蔽
		setHorizontalScrollBarEnabled(false);

		//WLog.d("JyAdView:"+this);
		// 点击下载APK
		setWebViewClient(new WMWebViewClient(act));
		
		setWebChromeClient(new WebChromeClient());
		// 去掉空白
		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		


		// webview.setMinimumHeight(100);
		// webview.setMinimumWidth(100);

		addJavascriptInterface(jse, "jieyunmob");

		// 显示广告
		/*
		webview.loadUrl("http://woso100.com/exchangemob.html?pid=" + placeid
				+ "&sizeid=" + sizeid + "&w=" + width + "&h=" + height);
		*/
	}
	
	public void loadAd()
	{
		loadUrl(getAdurl());
	}
	
	@Override
	public boolean onTouchEvent(android.view.MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			return true; // 在这里根据你的需要捕获
		}
		else 
		{
			return super.onTouchEvent(event);
		}
	}
	

	private String getAdurl()
	{
		// 使用这个地址测试
		String url = String.format("http://cmarket.kejet.net/exchangemob.html?pid=%s&w=%d&h=%d&adtype=%s",placeid, width,height,adtype);
		//String url = String.format("http://woso100.com/exchangemob.html?pid=%s&w=%d&h=%d&adtype=%s",placeid, width,height,adtype);

		//WLog.d(url);
		return url;
	}
}
