// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package mobi.dzs.util;

import java.io.PrintStream;

public class CHexConver
{
	private static final char mChars[] = "0123456789ABCDEF".toCharArray();
	private static final String mHexStr = "0123456789ABCDEF";

	public static String byte2HexStr(byte abyte0[], int i)
	{
		StringBuilder stringbuilder = new StringBuilder("");
		int j = 0;
		do
		{
			if (j >= i)
				return stringbuilder.toString().toUpperCase().trim();
			String s = Integer.toHexString(0xff & abyte0[j]);
			String s1;
			if (s.length() == 1)
				s1 = (new StringBuilder("0")).append(s).toString();
			else
				s1 = s;
			stringbuilder.append(s1);
			stringbuilder.append(" ");
			j++;
		} while (true);
	}

	public static boolean checkHexStr(String s)
	{		
		String s1 = s.toString().trim().replace(" ", "").toUpperCase();
		int i = s1.length();
		if (i <= 1 || i % 2 != 0)
			return false;
		
		for(int j=0; j<i; j++)
		{			
			if ("0123456789ABCDEF".contains(s1.substring(j, j + 1)))
				return false;
		}
		return true;
	}

	public static byte[] hexStr2Bytes(String s)
	{
		int i = s.length() / 2;
		System.out.println(i);
		byte abyte0[] = new byte[i];
		int j = 0;
		do
		{
			if (j >= i)
				return abyte0;
			int k = 1 + j * 2;
			int l = k + 1;
			abyte0[j] = (byte)(0xff & Integer.decode((new StringBuilder("0x")).append(s.substring(j * 2, k)).append(s.substring(k, l)).toString()).intValue());
			j++;
		} while (true);
	}

	public static String hexStr2Str(String s)
	{
		char ac[] = s.toCharArray();
		byte abyte0[] = new byte[s.length() / 2];
		int i = 0;
		do
		{
			if (i >= abyte0.length)
				return new String(abyte0);
			abyte0[i] = (byte)(0xff & 16 * "0123456789ABCDEF".indexOf(ac[i * 2]) + "0123456789ABCDEF".indexOf(ac[1 + i * 2]));
			i++;
		} while (true);
	}

	public static String str2HexStr(String s)
	{
		StringBuilder stringbuilder = new StringBuilder("");
		byte abyte0[] = s.getBytes();
		int i = 0;
		do
		{
			if (i >= abyte0.length)
				return stringbuilder.toString().trim();
			int j = (0xf0 & abyte0[i]) >> 4;
			stringbuilder.append(mChars[j]);
			int k = 0xf & abyte0[i];
			stringbuilder.append(mChars[k]);
			stringbuilder.append(' ');
			i++;
		} while (true);
	}

	public static String strToUnicode(String s)
		throws Exception
	{
		StringBuilder stringbuilder = new StringBuilder();
		int i = 0;
		do
		{
			if (i >= s.length())
				return stringbuilder.toString();
			char c = s.charAt(i);
			String s1 = Integer.toHexString(c);
			if (c > '\200')
				stringbuilder.append((new StringBuilder("\\u")).append(s1).toString());
			else
				stringbuilder.append((new StringBuilder("\\u00")).append(s1).toString());
			i++;
		} while (true);
	}

	public static String unicodeToString(String s)
	{
		int i = s.length() / 6;
		StringBuilder stringbuilder = new StringBuilder();
		int j = 0;
		do
		{
			if (j >= i)
				return stringbuilder.toString();
			String s1 = s.substring(j * 6, 6 * (j + 1));
			String s2 = (new StringBuilder(String.valueOf(s1.substring(2, 4)))).append("00").toString();
			String s3 = s1.substring(4);
			stringbuilder.append(new String(Character.toChars(Integer.valueOf(s2, 16).intValue() + Integer.valueOf(s3, 16).intValue())));
			j++;
		} while (true);
	}

}
