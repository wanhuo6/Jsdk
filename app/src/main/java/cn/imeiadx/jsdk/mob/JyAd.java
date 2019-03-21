package cn.imeiadx.jsdk.mob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class JyAd 
{	
	/**
	 * 普通广告
	 */
	public static int NORMAL_AD = 6;
	/**
	 * 插屏广告
	 */
	public static int POP_AD = 7;
	
	private String placeid;
	private int width;
	private int height;

	public JyAd(Activity act, String placeid, int location,
                int width, int height)
	{

		this(act, placeid, location, width, height, null);
	}

	/**
	 * 根据传入参数设置本控件尺寸与webview尺寸
	 * @param act
	 * @param placeid
	 * @param location
	 * @param width
	 * @param height
	 */
	public JyAd(Activity act, String placeid, int location,
                int width, int height, JyJS jse) {

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);

		// 设置广告出现的位置(悬浮于底部)

		/*
		// 0 = 顶部
		if (location == 0)
			params.gravity = Gravity.TOP;

		// 1 = 底部
		else
			params.gravity = Gravity.BOTTOM;
		*/
		params.gravity = location;
		params.width = width;
		params.height = height;

		this.width = width;
		this.height = height;
		
		// 广告位ID
		this.placeid = placeid;

		JyAdView webview = new JyAdView(act, act,  this.placeid,NORMAL_AD, this.width,  this.height,  jse);
		webview.loadAd();
		//webview.loadUrl(getAdurl());

		// 装在到屏幕里
		act.addContentView(webview, params);
	}
	
	private static JyAdView initView(Activity act, String placeid, int adstyle, int width, int height, JyJS jse)
	{
		JyAdView webview = new JyAdView(act, act,  placeid,NORMAL_AD, width,  height,  jse);
		
		return webview;
	}
	
	/**
	 * 实例化一个一般广告
	 * @param act
	 * @param placeid
	 * @param width
	 * @param height
	 * @return
	 */
	public static JyAdView initNormalAdView(Activity act, String placeid, int width, int height)
	{
		return initNormalAdView(act,placeid,width,height,null);		
	}
	
	/**
	 * 实例化一个一般形式广告
	 * @param act
	 * @param placeid
	 * @param width
	 * @param height
	 * @return
	 */
	public static JyAdView initNormalAdView(Activity act, String placeid, int width, int height, JyJS jse)
	{
		JyAdView view = initView(act,placeid,NORMAL_AD,width,height,jse);
		view.loadAd();

		return view;
	}
	
	public static JyAdPopWindow initPopWindow(Activity act, String placeid, int width, int height, JyJS jse)
	{
        JyAdPopWindow mPopupWindow = new JyAdPopWindow(act,placeid,width,height,jse,null); 

		return mPopupWindow;
	}
	
	public static JyAdPopWindow initPopWindow(Activity act, String placeid, int width, int height, JyJS jse, Drawable background)
	{
        JyAdPopWindow mPopupWindow = new JyAdPopWindow(act,placeid,width,height,jse,background); 

		return mPopupWindow;
	}
}
