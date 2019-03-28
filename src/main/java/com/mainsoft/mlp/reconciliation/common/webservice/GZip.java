package com.mainsoft.mlp.reconciliation.common.webservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩解压缩
 * 
 * @author czz
 * @version 2016-11-12
 */
public class GZip {
	public static void main(String[] args) {
		byte[] data = new String("a").getBytes();
		byte[] a = compress(data);
		byte[] b = uncompress(a);
		System.out.println(new String(b));
	}

	/**
	 * 对byte[]进行压缩
	 * 
	 * @param 要压缩的数据
	 * @return 压缩后的数据
	 */
	public static byte[] compress(byte[] data) {
		// System.out.println("before compress:" + data.length);

		GZIPOutputStream gzip = null;
		ByteArrayOutputStream baos = null;
		byte[] newData = null;

		try {
			baos = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(baos);

			gzip.write(data);
			gzip.finish();
			gzip.flush();

			newData = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				gzip.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// System.out.println("after compress:" + newData.length);
		return newData;
	}

	/**
	 * 对byte[]进行解压缩
	 * 
	 * @param 要压缩的数据
	 * @return 压缩后的数据
	 */
	public static byte[] uncompress(byte[] data) {
		byte[] newData = null;
		// System.out.println("before uncompress:" + data.length);
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		try {
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(data);
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			newData = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// System.out.println("after uncompress:" + newData.length);
		return newData;
	}
}