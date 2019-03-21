/**
 * 
 */
package cn.imeiadx.jsdk.util;

import android.app.Activity;
import android.content.pm.PackageManager;

/**
 * @author starneo@gmail.com 2016年5月13日
 */
public class Tool
{
	public static boolean checkPermission(String permissionname, Activity act)
	{		
		PackageManager pm = act.getPackageManager();
		boolean isok = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permissionname,act.getPackageName()));
		
		return isok;
	}
}
