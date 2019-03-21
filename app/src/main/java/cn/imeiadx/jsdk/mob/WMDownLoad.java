package cn.imeiadx.jsdk.mob;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class WMDownLoad {
	private Activity act;
	private NotificationManager Notimanager;
	private static int icount = 0;
	private String filename;
	private static HashMap<String,String> DOWNMAP = new HashMap<String,String>();
	
	public WMDownLoad(Activity act) {
		this.act = act;
		Notimanager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void downLoad(String url, String strPos) {
		// WLog.d("%s" ,strUrl);
		String p = DOWNMAP.get(url);
		if (p != null)
		{
			//正在下载
			Toast.makeText(act, String.format("已在下载%s", url), Toast.LENGTH_SHORT).show();
			return;
		}
		else
		{
			DOWNMAP.put(url, url);
		}
		
		icount++;
		String apkName;
		int ipos1 = url.lastIndexOf("/");
		int ipos2 = url.lastIndexOf(".");
		if (-1 != ipos2) {
			apkName = url.substring(ipos1 + 1, ipos2);
			int len = apkName.length();
			if (len > 10) {
				apkName = apkName.substring(len - 11);
			}
		} else {
			apkName = url.substring(ipos1 + 1);
		}
		int iPos = url.lastIndexOf("/");
		String str = url.substring(iPos + 1);
		filename = strPos + "/" + str;
		
		
		Intent intent = new Intent();
		// intent.setAction(""+mNotice);
		PendingIntent pIntent = PendingIntent.getActivity(act, 0, intent, 0);
		
		NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(act);
		nbuilder.setSmallIcon(android.R.drawable.stat_sys_download)
        .setTicker("下载新应用").setContentInfo("")
        .setOngoing(true)
        .setContentTitle("准备下载").setContentText(url)
        .setNumber(icount);
        //.setDefaults(Notification.DEFAULT_ALL)
        //Notification.DEFAULT_ALL
		//setAutoCancel(true)
		PackageManager pm = act.getPackageManager();
		try
		{
			ApplicationInfo info = pm.getApplicationInfo(act.getPackageName(), 0);
			nbuilder.setLargeIcon(((BitmapDrawable)info.loadIcon(pm)).getBitmap());
		}
		catch (NameNotFoundException e)
		{
			WLog.d(e);
		} 
		
		Notification notif = nbuilder.build();

		notif.contentIntent = pIntent;

		Notimanager.notify(icount, notif);
		
		TDownLoad tdownload = new TDownLoad(url, filename, notif,nbuilder);
		Thread thread = new Thread(tdownload);
		thread.start();
	}

	public Handler handle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (101 == msg.arg1) {
				int len = msg.what;
				//Notification notification = (Notification) msg.obj;

				if (100 == len) {
					Toast.makeText(act, "下载完成, 准备安装", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setDataAndType(Uri.fromFile(new File(filename)),"application/vnd.android.package-archive");
					act.startActivity(intent);

				}
			} else if (102 == msg.arg1) {
			/*	Toast.makeText(act, "下载完成, 准备安装", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.fromFile(new File(filename)),"application/vnd.android.package-archive");
				act.startActivity(intent);*/

			}
		};
	};

	class TDownLoad implements Runnable {
		private int Curlen = 0;
		private String Url = null;
		private String filename = null;
		private Notification Notice = null;
		private Timer timer = new Timer();
		private NotificationCompat.Builder builder = null;

		TDownLoad(String strUrl, String filename, Notification Notice, NotificationCompat.Builder builder) {
			this.Url = strUrl;
			this.filename = filename;
			this.Notice = Notice;
			this.Curlen = 0;
			this.builder = builder;
		}

		@Override
		public void run() {
			URL fileURL = null;
			try {
				fileURL = new URL(Url);
			} catch (MalformedURLException e1) {
				WLog.d(e1);
			}
			try {
				HttpURLConnection conn = (HttpURLConnection) fileURL.openConnection();

				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				long length = (long) conn.getContentLength();
				//WLog.d("length:%d",length);

				if (length != -1) 
				{
					File file = new File(filename);
					if (file.exists() == false) {
						if (file.createNewFile() == false)
						{
							WLog.d("[%s]创建失败",filename);
						}
					}

					OutputStream os = new FileOutputStream(filename);
					//WLog.d("222");

					byte[] buffer = new byte[512];
					int readLen = 0;
					int destPos = 0;
					Curlen = 0;
					timer.schedule(new TimerTask()
					{
						@Override
						public void run() 
						{
							builder.setProgress(100, Curlen, false);
							if (100 == Curlen)
							{
								builder.setContentTitle("下载完成");
								builder.setOngoing(false);
								builder.setAutoCancel(true);
								DOWNMAP.remove(Url);
							}
							else
							{
								builder.setContentTitle("正在下载");
							}
							builder.setContentInfo(String.format("%d%%", Curlen));
							//老进度
							//Notice.contentView.setTextViewText(notifyid.GetProTextID(), Curlen + "%");
							//Notice.contentView.setProgressBar(notifyid.GetProcessID(), 100, Curlen, false);
							
							Notimanager.notify(Notice.number, builder.build());
							if (100 == Curlen) {
								timer.cancel();
								//Notimanager.cancel(Notice.number);
								Message msg = handle.obtainMessage();
								msg.arg1 = 102;
								//msg.obj = Notice.tickerText;
								handle.sendMessage(msg);
							}

						}
					}, 0, 1000);

					while ((readLen = is.read(buffer)) > 0) 
					{
						//WLog.d("%d",Curlen);
						os.write(buffer, 0, readLen);
						destPos += readLen;
						// Curlen = (int) ((float)(destPos*1.0/length) * 100);
						Curlen = (int) ((long) destPos * 100 / length);
						// if(0 != Curlen)
					}
					Curlen = 100;
					is.close();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}