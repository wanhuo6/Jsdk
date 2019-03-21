package cn.imeiadx.jsdk.util;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.imeiadx.jsdk.mob.WLog;


/**
 * <p>
 * <i>Copyright: 9esoft.com (c) 2005-2005<br>
 * Company: 九州易软科技发展有限公司</i>
 * </p>
 * 格式化处理
 * 
 * @version 1.0 (<i>2005-8-17 Neo</i>)
 */

public class Format
{
	private final static HashMap<String, String> cmap = new HashMap<String, String>();

	private final static String LOWSTRING = "abcdefghijklmnopqrstuvwxyz";
	private final static String NUMLOWSTRING = "abcdefghijklmnopqrstuvwxyz1234567890";
	private final static String ALLSTRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	
	private final static Pattern URLPAT = Pattern.compile("(http://|https://)[^\\s]*");



	/**
	 * 转换成script输出使用字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String toScriptString(String src)
	{
		String tmp = src;
		tmp = tmp.replaceAll("\r|\n", "");
		tmp = tmp.replaceAll("\"", "\\\\\"");
		tmp = tmp.replaceAll("</script>", "\"+\"<\"+\"/script>\"+\"");
		tmp = tmp.replaceAll("</SCRIPT>", "\"+\"<\"+\"/SCRIPT>\"+\"");

		return tmp;
	}


	/**
	 * 转换成HTML输出使用字符串。取出&,",',<,>。
	 * 
	 * @param src
	 * @return
	 */
	public static String toHTMLString(String src, boolean isnoquotes)
	{
		if (src == null)
		{
			return "";
		}
		String tmp = src;
		tmp = tmp.replaceAll("&", "&amp;");
		if (isnoquotes == true)
		{
			tmp = tmp.replaceAll("\"", "&quot;");
			tmp = tmp.replaceAll("'", "&#039;");
		}
		tmp = tmp.replaceAll("<", "&lt;");
		tmp = tmp.replaceAll(">", "&gt;");

		return tmp;
	}

	/**
	 * 转换成HTML输出使用字符串。取出&,",',<,>。
	 * 
	 * @param src
	 * @return
	 */
	public static String toHTMLString(String src)
	{
		return toHTMLString(src, false);
	}

	

	/**
	 * 返回list输出字符串，使用,分割
	 * 
	 * @param strs
	 * @return
	 */
	public static String toListString(String[] strs)
	{
		List<String> list = new ArrayList<String>();
		for (String t : strs)
		{
			list.add(t);
		}

		return toListString(list, ",");
	}

	/**
	 * 返回list输出字符串，使用,分割
	 * 
	 * @param list
	 * @return
	 */
	public static String toListString(List<String> list)
	{
		return toListString(list, ",");
	}

	/**
	 * 返回list输出字符串
	 * 
	 * @param list
	 *            对应list
	 * @param splitstr
	 *            分割字符
	 * @return
	 */
	public static String toListString(List<String> list, String splitstr)
	{
		StringBuffer buf = new StringBuffer();
		for (int i = 0, len = list.size(); i < len; i++)
		{
			buf.append(list.get(i).toString());
			buf.append(splitstr);
		}
		if (list.size() > 0)
		{
			buf.setLength(buf.length() - splitstr.length());
		}
		return buf.toString();
	}

	public static String getContent(String str, String start, String end)
	{
		try
		{
			int si = str.indexOf(start);
			int ssi = si + start.length();
			int ei = str.indexOf(end, ssi);
			return str.substring(ssi, ei);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static String replaceContent(String str, String start, String end,
                                        String newstring)
	{
		try
		{
			int si = str.indexOf(start);
			int ssi = si + start.length();
			int ei = str.indexOf(end, ssi);

			return String.format("%s%s%s", str.substring(0, ssi), newstring,
							str.substring(ei, str.length()));
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * 汉字转拼音
	 * 
	 * @param str
	 * @return
	 */
	public static String getPinyin(String str)
	{
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length(); i++)
		{
			String k = str.substring(i, i + 1);
			String t = cmap.get(k);
			if (t == null)
			{
				buf.append(k);
			}
			else
			{
				buf.append(t);
			}
		}

		return buf.toString();
	}

	/**
	 * 取得拼音首字母
	 * 
	 * @param str
	 * @return
	 */
	public static String getFirstPinyin(String str)
	{
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length(); i++)
		{
			String k = str.substring(i, i + 1);
			String t = cmap.get(k);
			if (t == null)
			{
				buf.append(k);
			}
			else
			{
				buf.append(t.substring(0, 1));
			}
		}

		return buf.toString();
	}

	/**
	 * 取得对象所有变量打印
	 * 
	 * @param o
	 * @return
	 */
	public static String toNormalString(Object o)
	{
		StringBuffer buf = new StringBuffer();
		Field[] fields = o.getClass().getDeclaredFields();

		for (Field f : fields)
		{
			boolean accessFlag = f.isAccessible();
			f.setAccessible(true);
			try
			{
				buf.append(String.format("%s:%s\r\n", f.getName(), f.get(o)));
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			f.setAccessible(accessFlag);
		}

		return buf.toString();
	}

	/**
	 * 字符串相似值 Levenshtein Distance
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int ld(String str1, String str2)
	{
		int d[][]; // 矩阵
		int n = str1.length();
		int m = str2.length();
		int i; // 遍历str1的
		int j; // 遍历str2的
		char ch1; // str1的
		char ch2; // str2的
		int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (n == 0)
		{
			return m;
		}
		if (m == 0)
		{
			return n;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++)
		{ // 初始化第一列
			d[i][0] = i;
		}
		for (j = 0; j <= m; j++)
		{ // 初始化第一行
			d[0][j] = j;
		}
		for (i = 1; i <= n; i++)
		{ // 遍历str1
			ch1 = str1.charAt(i - 1);
			// 去匹配str2
			for (j = 1; j <= m; j++)
			{
				ch2 = str2.charAt(j - 1);
				if (ch1 == ch2)
				{
					temp = 0;
				}
				else
				{
					temp = 1;
				}
				// 左边+1,上边+1, 左上角+temp取最小
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
								+ temp);
			}
		}
		return d[n][m];
	}

	/**
	 * 返回字符串相似百分比
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static double sim(String str1, String str2)
	{
		int ld = ld(str1, str2);
		return 1 - (double) ld / Math.max(str1.length(), str2.length());
	}

	private static int min(int one, int two, int three)
	{
		int min = one;
		if (two < min)
		{
			min = two;
		}
		if (three < min)
		{
			min = three;
		}
		return min;
	}

	/**
	 * IP转长整
	 * 
	 * @param ip
	 * @return
	 */
	public static long getIpNum(String ip)
	{
		long ipnum = 0;

		String[] t = ip.split("\\.");

		if (t.length == 4)
		{
			for (int i = 0; i < 4; i++)
			{
				try
				{
					long l = Long.parseLong(t[i]);
					ipnum += l << (8 * (3 - i));
				}
				catch (Exception e)
				{
					return -1;
				}

			}
		}
		else
		{
			return -1;
		}

		return ipnum;
	}

	/**
	 * 返回随机字符串 只有小写字母与数字
	 * @param num
	 * @return
	 */
	public static String getRandStringNum(int num)
	{
		StringBuffer buf = new StringBuffer();
		int len = NUMLOWSTRING.length();
		// LOWSTRING
		for (int i = 0; i < num; i++)
		{
			int pos = (int) (Math.random() * len);
			buf.append(NUMLOWSTRING.substring(pos, pos + 1));
		}
		return buf.toString();
	}
	
	/**
	 * 返回随机字符串 只有小写字母
	 * 
	 * @param num
	 *            生成字母数量
	 * @return
	 */
	public static String getRandString(int num)
	{
		StringBuffer buf = new StringBuffer();
		int len = LOWSTRING.length();
		// LOWSTRING
		for (int i = 0; i < num; i++)
		{
			int pos = (int) (Math.random() * len);
			buf.append(LOWSTRING.substring(pos, pos + 1));
		}
		return buf.toString();
	}
	
	/**
	 * 返回随机字符串 大小写与数字
	 * 
	 * @param num
	 *            生成字母数量
	 * @return
	 */
	public static String getRandAllString(int num)
	{
		StringBuffer buf = new StringBuffer();
		int len = ALLSTRING.length();
		// LOWSTRING
		for (int i = 0; i < num; i++)
		{
			int pos = (int) (Math.random() * len);
			buf.append(ALLSTRING.substring(pos, pos + 1));
		}
		return buf.toString();
	}
	
	public static String Sha256(String str)
	{
		return MessageDigest("sha-256",str);
	}
	
	public static String Sha1(String str)
	{
		return MessageDigest("sha-1",str);
	}
	
	public static String Md2(String str)
	{
		return MessageDigest("md2",str);
	}
	
	public static String Md5(String str)
	{
		return MessageDigest("md5",str);
	}
	
	public static Long getNumber(String str)
	{
		String number = "0123456789";
		StringBuffer buf = new StringBuffer("0");
		
		for (char t : str.toCharArray())
		{
			for (int i=0,len=number.length(); i<len; i++)
			{
				char nt = number.charAt(i);
				if (nt == t)
				{
					buf.append(t);
					break;
				}
			}
		}
		
		return Long.parseLong(buf.toString());
	}
	
	
	public static String MessageDigest(String m, String str)
	{
		String mstr = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance(m);
			mstr = Format.byte2hex(md.digest(str.getBytes()));
		}
		catch (NoSuchAlgorithmException e)
		{
			WLog.d(e.toString());
		}
		return mstr;
	}

	public static String byte2hex(byte[] b) // 二行制转字符串
	{
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++)
		{
			String hex = Integer.toHexString(0xff & b[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
	

	public static List<String> getUrls(final String str)
	{
		List<String> list = new LinkedList<String>();
		Matcher matcher = URLPAT.matcher(str);
		while (matcher.find())
		{
			list.add(matcher.group());
		}
		return list;
	}
	
	public static String getMapString(HashMap<String,String> map)
	{
		StringBuffer buf = new StringBuffer();
		
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String,String> entry =  iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			
			buf.append(String.format("[%s]:[%s]\n", key,val));
		}
		
		return buf.toString();
	}
}