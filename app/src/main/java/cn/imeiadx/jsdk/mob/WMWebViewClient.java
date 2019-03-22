package cn.imeiadx.jsdk.mob;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;

import cn.imeiadx.jsdk.util.Format;
import cn.imeiadx.jsdk.util.Tool;


public class WMWebViewClient extends WebViewClient {
	private Activity act;
	private final static String NET_UNKNOWN = "UNKNOWN";
	private final static String NET_WIFI = "WIFI";
	private final static String NET_2G = "2G";
	private final static String NET_3G = "3G";
	private final static String NET_4G = "4G";
	private static HashMap<String, Double> gps;

	// private int count = 0;

	public WMWebViewClient(Activity act) {
		this.act = act;
	}

	public boolean isTabletDevice() {
		TelephonyManager telephony = (TelephonyManager) act
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephony.getPhoneType();
		if (type == TelephonyManager.PHONE_TYPE_NONE) {
			return true;
		} else {
			return false;
		}
	}

	public String getNetWorkType() {
		String type = NET_UNKNOWN;
		ConnectivityManager connectMgr = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		int ntype = info.getType();
		if (ntype == ConnectivityManager.TYPE_WIFI) {
			type = NET_WIFI;
		} else if (ntype == ConnectivityManager.TYPE_MOBILE) {
			switch (info.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
				case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
				case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN:
					type = NET_2G;
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
				case TelephonyManager.NETWORK_TYPE_EHRPD:
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					type = NET_3G;
					break;
				case TelephonyManager.NETWORK_TYPE_LTE:
					type = NET_4G;
					break;
				default:
					type = NET_UNKNOWN;
			}

		}

		return type;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// WLog.d(url);
		// if(count > 0) return super.shouldOverrideUrlLoading(view, url);
		// else count++;
		// if (url.startsWith("newtab:"))
		if (url.indexOf("http://") == 0) {
			// String realUrl = url.substring(7, url.length());
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setData(Uri.parse(url));

			int idx = url.lastIndexOf(".") + 1;
			int len = url.length();

			String ext;
			if (idx + 1 >= len) {
				ext = url.substring(len - 3);
			} else {
				ext = url.substring(idx);
			}
			ext = ext.toLowerCase(Locale.getDefault());

			if (ext.equals("apk")) {
				// act.startActivity(it);
				// *
				String domain = getDomain(url);
				if (domain.indexOf("kejet.net") >= 0) {
					return super.shouldOverrideUrlLoading(view, url);
				} else {
					// String path = "/mnt/sdcard/download";
					// String path =
					// act.getExternalCacheDir().getAbsolutePath();
					// String path = act.getFilesDir().getPath();
					// String path = act.getCacheDir().getAbsolutePath();
					String path = Environment
							.getExternalStoragePublicDirectory(
									Environment.DIRECTORY_DOWNLOADS)
							.getAbsolutePath();
					// String path =
					// Environment.getDownloadCacheDirectory().getAbsolutePath();
					// Environment.getExternalStoragePublicDirectory(path).mkdir();
					// "/mnt/sdcard/download";
					WLog.d("下载apk文件[%s]TO[%s]", url, path);

					WMDownLoad wm = new WMDownLoad(act);
					wm.downLoad(url, path);
					// wm.downLoad("http://down.angeeks.com/c/d2/d10125/10125862.apk",
					// "/mnt/sdcard/download");
					// File f = downLoadFile(url,view.getContext());
					// WLog.d("安装apk文件[%s]",f.getAbsolutePath());
					// it.setDataAndType(Uri.fromFile(f),"application/vnd.android.package-archive");
					// it.setDataAndType(Uri.fromFile(new
					// File(url)),"application/vnd.android.package-archive");
					return super.shouldOverrideUrlLoading(view, url);
				}
				// */
			}
			/*
			 * else if (ext.equals("mp4")) { Intent i = new
			 * Intent(Intent.ACTION_VIEW); i.setDataAndType(Uri.parse(url),
			 * "video/*"); act.startActivity(i); //warning no error handling
			 * will cause force close if no media player on phone. return true;
			 * }
			 */
			else {
				act.startActivity(it);
			}

			// if (url.)
			// {
			// it.setDataAndType(Uri.fromFile(new File(url)),
			// "application/vnd.android.package-archive");
			// }

			// else act.startActivity(it);
			// startActivity(it);
			// main.startActivity(it);
		} else if (url.startsWith("jyad:")) {
			// closepop //adcallback
			String mname = url.substring(5);
			if ("closepop".equals(mname)) {
				view.loadUrl("javascript:window.jieyunmob.closepop();");
			} else if ("adcallback".equals(mname)) {
				view.loadUrl("javascript:window.jieyunmob.adcallback();");
			}
		} else {
			return super.shouldOverrideUrlLoading(view, url);
		}

		return true;
	}

	private void getGps() {
		if (gps == null) {
			double lat = 0.0;
			double lng = 0.0;

			// if (checkSelfPermission)

			gps = new HashMap<String, Double>();
			if (Tool.checkPermission("android.permission.ACCESS_FINE_LOCATION", act)) {
				LocationManager locationManager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Location location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					LocationListener locationListener = new JyLocationListener();

					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
					location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

					//WLog.d("GPS_PROVIDER location2 %s", location);

					if (location != null) {
						lat = location.getLatitude(); // 经度
						lng = location.getLongitude(); // 纬度
					}
				} else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
					Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					LocationListener locationListener = new JyLocationListener();

					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
					location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						lat = location.getLatitude(); // 经度
						lng = location.getLongitude(); // 纬度
					}
				}
			}

			gps.put("lat", lat);
			gps.put("lng", lng);
		}
	}

	// /*
	private String getDomain(final String url) {
		return Format.getContent(url, "//", "/");
	}

	// */

	@SuppressWarnings("deprecation")
	private String getJyMob() {
		StringBuffer param = new StringBuffer();
		try {
			String platform = "ANDROID";
			String brand = android.os.Build.BRAND;
			brand = URLEncoder.encode(brand, "utf-8");

			String model = android.os.Build.MODEL;
			model = URLEncoder.encode(model, "utf-8");

			String ver = android.os.Build.VERSION.RELEASE;
			ver = URLEncoder.encode(ver, "utf-8");

			TelephonyManager TelephonyMgr = (TelephonyManager) act
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return null;
			}
			String imei = TelephonyMgr.getDeviceId();
			imei = URLEncoder.encode(imei, "utf-8");

			WifiManager wm = (WifiManager) act.getApplicationContext()
					.getSystemService(Context.WIFI_SERVICE);
		//	String mac = wm.getConnectionInfo().getMacAddress();
		//	mac = URLEncoder.encode(mac, "utf-8");

			Display display = act.getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight();

			String isapp = "Y";
			String appid = act.getPackageName();
			// appid = URLEncoder.encode(app_id, "utf-8");
			String idfa = "";
			String networktype = getNetWorkType();
			String istabletdevice = isTabletDevice() == true ? "Y" : "N";

			String appname = "";
			String appver = "";
			String androidid = Secure.getString(act.getContentResolver(),
					Secure.ANDROID_ID);
			getGps();

			PackageInfo info = null;
			try
			{
				info = act.getPackageManager().getPackageInfo(appid, 0);
				appver = info.versionName;
				appname = act.getResources().getString(
						act.getApplicationInfo().labelRes);
			}
			catch (NameNotFoundException e)
			{
			}

			/*
			 * param = String.format("%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/%s",
			 * platform, brand, model, ver, is_app, app_id, width, height, mac,
			 * imei, idfa, networktype,isTabletDevice);
			 */
			param.append("var jy_adparam = [];");
			param.append(String.format("jy_adparam[\"appid\"]=\"%s\";",
					appid.replace("\"", "\\\"")));
			param.append(String.format("jy_adparam[\"platform\"]=\"%s\";",
					platform.replace("\"", "\\\"")));
			param.append(String.format("jy_adparam[\"brand\"]=\"%s\";",
					brand.replace("\"", "\\\"")));
			param.append(String.format("jy_adparam[\"model\"]=\"%s\";",
					model.replace("\"", "\\\"")));
			param.append(String.format("jy_adparam[\"ver\"]=\"%s\";",
					ver.replace("\"", "\\\"")));
			param.append(String.format("jy_adparam[\"isapp\"]=\"%s\";",
					isapp.replace("\"", "\\\"")));
			param.append(String.format("jy_adparam[\"width\"]=\"%d\";", width));
			param.append(String
					.format("jy_adparam[\"height\"]=\"%d\";", height));
			param.append(String.format("jy_adparam[\"idfa\"]=\"%s\";",
					idfa.replace("\"", "\\\"")));
			param.append(String.format("jy_adparam[\"networktype\"]=\"%s\";",
					networktype.replace("\"", "\\\"")));
			param.append(String.format(
					"jy_adparam[\"istabletdevice\"]=\"%s\";",
					istabletdevice.replace("\"", "\\\"")));
			param.append(String
					.format("jy_adparam[\"appver\"]=\"%s\";", appver));
			param.append(String.format("jy_adparam[\"appname\"]=\"%s\";",
					appname.replace("\"", "\\\"")));
			param.append(String.format("jy_adparam[\"androidid\"]=\"%s\";",
					androidid.replace("\"", "\\\"")));

			param.append(String.format("jy_adparam[\"lat\"]=\"%f\";",
					gps.get("lat")));
			param.append(String.format("jy_adparam[\"lng\"]=\"%f\";",
					gps.get("lng")));

			/*
			 * [param appendString:@"var jy_adparam = [];"]; [param
			 * appendFormat:@"jy_adparam[\"appid\"]=\"%@\";",[appid
			 * stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""]];
			 * [param appendFormat:@"jy_adparam[\"platform\"]=\"%@\";",[platform
			 * stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""]];
			 * [param appendFormat:@"jy_adparam[\"brand\"]=\"%@\";",[brand
			 * stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""]];
			 * [param appendFormat:@"jy_adparam[\"model\"]=\"%@\";",[model
			 * stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""]];
			 * [param appendFormat:@"jy_adparam[\"ver\"]=\"%@\";",[ver
			 * stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""]];
			 * [param appendFormat:@"jy_adparam[\"isapp\"]=\"%@\";",[isapp
			 * stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""]];
			 * [param appendFormat:@"jy_adparam[\"width\"]=\"%d\";",pwidth];
			 * [param appendFormat:@"jy_adparam[\"height\"]=\"%d\";",pheight];
			 * [param appendFormat:@"jy_adparam[\"idfa\"]=\"%@\";",idfa]; [param
			 * appendFormat:@"jy_adparam[\"networktype\"]=\"%@\";",[networktype
			 * stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""]];
			 * [param appendFormat:@"jy_adparam[\"istabletdevice\"]=\"%@\";",[
			 * istabletdevice stringByReplacingOccurrencesOfString:@"\""
			 * withString:@"\\\""]]; [param
			 * appendFormat:@"jy_adparam[\"appver\"]=\"%@\";",[appver
			 * stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""]];
			 * [param appendFormat:@"jy_adparam[\"appname\"]=\"%@\";",[appname
			 * stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""]];
			 * [param appendFormat:@"jy_adparam[\"lat\"]=\"%f\";",lat]; [param
			 * appendFormat:@"jy_adparam[\"lng\"]=\"%f\";",lng];
			 */

		}
		catch (UnsupportedEncodingException e)
		{
			WLog.d(e);
		}
		return param.toString();
	}

	/**
	 * 传入手机特定参数
	 * 
	 * @param url
	 * @return
	 */
	/*
	 * private String getMobUrl(final String url) { String moburl = null; String
	 * param = getJyMob();
	 * 
	 * String t[] = url.split("/"); int len = t.length; if (len <=9) { moburl =
	 * String.format("%s/%s", url, param); } else { StringBuffer buf = new
	 * StringBuffer(); for (int i=0; i<len ; i++) { buf.append(t[i]);
	 * buf.append("/"); if (i == 8) { buf.append(param); buf.append("/"); } }
	 * buf.setLength(buf.length()-1); moburl = buf.toString(); }
	 * 
	 * return moburl; }
	 * 
	 * private boolean isAdUrl(final String url) { boolean isad = false; String
	 * domain = getDomain(url);
	 * 
	 * if (domain.equals("cmarket.kejet.net")) { // 第一段20 2 3 4都是数字 String t[] =
	 * url.split("/"); // int i=0; // // for (String s : t) // { //
	 * WLog.d(i+" "+s); // i++; // }
	 * 
	 * //WLog.d("%d %s",t.length,url); if ((t.length == 9 || t.length == 12) &&
	 * t[3].length() == 41) { String pid[] = t[3].split("\\?");
	 * 
	 * if (pid.length == 2 && pid[0].length() == 20 && pid[0].equals(pid[1])) {
	 * isad = true; } } }
	 * 
	 * return isad; }
	 */

	@SuppressLint("NewApi")
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url)
	{
		// WLog.d(isAdUrl(url)+"#"+ url);
		//WLog.d(url);
		// Log.d("WMMOB", ""+isTabletDevice(view));
		// http://cmarket.kejet.net:8081/MTFFN0M4N0NFQzI3QUY2?MTFFN0M4N0NFQzI3QUY2/9/5/1/480/0.48989776814352404/platform/brand/model/ver/is_app/app_id/screen_width/screen_height/mac/imei/idfa/openudid
		// /platform/brand/model/ver/is_app/app_id/screen_width/screen_height/mac/imei/idfa/openudid
		// String domain = Format.getContent(url, "//", "/");
		// WLog.d(domain);

		/*
		 * if (isAdUrl(url)) { url = getMobUrl(url); //WLog.d(url);
		 * //WLog.d("shouldInterceptRequest:" + url);
		 * //view.loadUrl(String.format(
		 * "javascript:var js = document.createElement('script');js.type = 'text/javascript';js.src = '%s';document.getElementsByTagName('head')[0].appendChild(js);"
		 * , url)); //return null; //return new
		 * WebResourceResponse("text/javascript", "utf-8", new
		 * ByteArrayInputStream("".getBytes())); //return new
		 * WebResourceResponse("text/javascript", "utf-8", new
		 * ByteArrayInputStream(String.format(
		 * "var js = document.createElement('script');js.type = 'text/javascript';js.src = '%s';document.getElementsByTagName('head')[0].appendChild(js);"
		 * , url).getBytes()));
		 * //document.write("<script type='text/javascript' src='http://"
		 * +_ehost+"/"+pid+"?"+parm+"'></script>"); return new
		 * WebResourceResponse("text/javascript", "utf-8", new
		 * ByteArrayInputStream(String.format(
		 * "document.write(\"<script type='text/javascript' src='%s'></script>\");"
		 * , url).getBytes()));
		 * 
		 * //return new WebResourceResponse("text/javascript", "utf-8", new
		 * ByteArrayInputStream((
		 * "var js = document.createElement('script');js.type = 'text/javascript';js.src = '"
		 * +url+"';document.getElementsByTagName('head')[0].appendChild(js);").
		 * getBytes())); }
		 */
		return super.shouldInterceptRequest(view, url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView,
	 * java.lang.String, android.graphics.Bitmap)
	 */
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon)
	{
		// WLog.d(String.format("javascript:%s", getJyMob()));
		view.loadUrl(String.format("javascript:%s", getJyMob()));
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url)
	{
		// WLog.d("onPageFinished"+url);
		// view.loadUrl("javascript:alert(123);mplay();");
		// WLog.d("onPageFinished"+view);
		view.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('video'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
		super.onPageFinished(view, url);
	}

	class JyLocationListener implements LocationListener
	{
		// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}

		// Provider被enable时触发此函数，比如GPS被打开
		@Override
		public void onProviderEnabled(String provider)
		{
		}

		// Provider被disable时触发此函数，比如GPS被关闭
		@Override
		public void onProviderDisabled(String provider)
		{

		}

		// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
		@Override
		public void onLocationChanged(Location location)
		{
			if (location != null)
			{
				// WLog.d("%f %f",location.getLatitude() ,
				// location.getLongitude());
				gps.put("lat", location.getLatitude());
				gps.put("lng", location.getLongitude());
			}
		}
	}
}
