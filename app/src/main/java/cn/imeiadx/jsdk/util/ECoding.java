/**
 * 
 */
package cn.imeiadx.jsdk.util;

import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.imeiadx.jsdk.mob.WLog;

/**
 * @author starneo@gmail.com Mar 18, 2019
 */
public class ECoding
{
	/**
	 * 
	 */
	private String cipherstr;

	private Cipher encipher;
	private Cipher decipher;
	private IvParameterSpec ivspec;
	private SecretKeySpec keyspec;

	public ECoding(String keygenerator, String key, String cipherstr,String iv)
	{
		this.cipherstr = cipherstr;

		keyspec = new SecretKeySpec(key.getBytes(), keygenerator);
		if (iv != null)
		{
			ivspec = new IvParameterSpec(iv.getBytes());
		}
	}

	public String encrypt(String content)
	{
		if (encipher == null)
		{
			try
			{
				encipher = Cipher.getInstance(cipherstr);
				if (ivspec == null)
				{
					encipher.init(Cipher.ENCRYPT_MODE, keyspec);// 初始化
				}
				else
				{
					encipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);// 初始化
				}
			}
			catch (NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidKeyException
					| InvalidAlgorithmParameterException e)
			{
				WLog.d(e);
			}
		}

		String enstr = null;
		try
		{
			byte[] byteContent = content.getBytes();

			if (cipherstr.indexOf("NoPadding") >= 0)
			{

				int blockSize = encipher.getBlockSize();
				int plaintextLength = byteContent.length;
				if (plaintextLength % blockSize != 0)
				{
					plaintextLength = plaintextLength
							+ (blockSize - (plaintextLength % blockSize));
				}
				byte[] plaintext = new byte[plaintextLength];
				System.arraycopy(byteContent, 0, plaintext, 0,
						byteContent.length);
				byteContent = plaintext;
			}

			byte[] result = encipher.doFinal(byteContent);
//			System.out.println(Format.byte2hex(result));
			enstr = Format.byte2hex(result);
//			enstr = Format.encodeBase64(result);
		}
		catch (IllegalBlockSizeException | BadPaddingException e)
		{
			WLog.d(e);
		}

		return enstr;
	}

	public String decrypt(String enstr)
	{
		if (decipher == null)
		{
			try
			{
				decipher = Cipher.getInstance(cipherstr);

				if (ivspec == null)
				{
					decipher.init(Cipher.DECRYPT_MODE, keyspec);// 初始化
				}
				else
				{
					decipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);// 初始化
				}
			}
			catch (InvalidKeyException | InvalidAlgorithmParameterException
					| NoSuchAlgorithmException | NoSuchPaddingException e)
			{
				WLog.d(e);
			}
		}

		String destr = null;
		try
		{
//			byte[] byteContent = Format.decodeBase64(enstr);
			byte[] byteContent = hex2byte(enstr);
			byte[] result = decipher.doFinal(byteContent);
			destr = new String(result);
		}
		catch (IllegalBlockSizeException | BadPaddingException e)
		{
			WLog.d(e);
		}

		return destr;
	}

	public static byte[] hex2byte(String s)
	{
		String s2;
		byte[] b = new byte[s.length() / 2];
		int i;
		for (i = 0; i < s.length() / 2; i++)
		{
			s2 = s.substring(i * 2, i * 2 + 2);
			b[i] = (byte) (Integer.parseInt(s2, 16) & 0xff);
		}
		return b;
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

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ECoding c = new ECoding("AES", "f2b6a2b84e5972db3b060355c81f4f3a","AES/CBC/NoPadding","f2b6a2b84e5972db");
		String e = c.encrypt("你好");

		System.out.println(e);
		System.out.println(c.decrypt(e));

		System.out.println(c.decrypt("e3a8d4118cdce82aef8c4cf9a9a4ddfc"));

		ECoding c2 = new ECoding("AES", "f2b6a2b84e5972db3b060355c81f4f3a","AES/CBC/NoPadding","f2b6a2b84e5972db");
		System.out.println(c2.decrypt("e3a8d4118cdce82aef8c4cf9a9a4ddfc"));

	}

}
