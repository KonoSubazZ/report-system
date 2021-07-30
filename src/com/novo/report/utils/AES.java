package com.novo.report.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.Base64Utils;

import com.mysql.jdbc.Blob;

public class AES {
	
	private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES";//默认的加密算法
    
	/*AES加密
	 *@param password 密文
	 *@param strKey 秘钥
	 *@return 加密好的内容
	 * */
	public static String aes_encrypt(String password, String strKey) {
	    try {
	        byte[] encodeFormat=Arrays.copyOf(strKey.getBytes("ASCII"), 16);
	        SecretKeySpec key = new SecretKeySpec(encodeFormat, "AES");
	        // Cipher对象实际完成加密操作
	        Cipher cipher = Cipher.getInstance("AES");
	        // 加密内容进行编码
	        byte[] byteContent = password.getBytes("UTF-8");
	        // 用密匙初始化Cipher对象
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        // 正式执行加密操作
	        byte[] result = cipher.doFinal(byteContent);
	        return new String(Hex.encodeHex(result));
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (NoSuchPaddingException e) {
	        e.printStackTrace();
	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (IllegalBlockSizeException e) {
	        e.printStackTrace();
	    } catch (BadPaddingException e) {
	        e.printStackTrace();
	    } return null;
	}
	
	/*AES解密
	 *@param password 密文
	 *@param strKey 秘钥
	 *@return 解密好的内容
	 * */
	public static String aes_decrypt(byte[] password, String strKey) {
	    try {
	    	//密文使用Hex解码
	        //byte[]content = Hex.decodeHex(password.toCharArray());
	    	byte[]content = password;
	        //秘钥 Hex解码为什么秘钥要进行解码，因为秘钥是某个秘钥明文进行了Hex编码后的值，所以在使用的时候要进行解码
	        byte[] encodeFormat = Arrays.copyOf(strKey.getBytes("ASCII"), 16);
	        SecretKeySpec key = new SecretKeySpec(encodeFormat, "AES");
	        // Cipher对象实际完成加密操作
	        Cipher cipher = Cipher.getInstance("AES");
	        // 用密匙初始化Cipher对象
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        // 正式执行解密操作
	        byte[] result = cipher.doFinal(content);
	        return new String(result, "UTF-8");

	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (NoSuchPaddingException e) {
	        e.printStackTrace();
	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (IllegalBlockSizeException e) {
	        e.printStackTrace();
	    } catch (BadPaddingException e) {
	        e.printStackTrace();
	    }  return null;
	}
	
	public static List<Map> getDecList(List<Map> list){
		List<Map> newList = new ArrayList<Map>();
		for (Map map : list) {
			HashMap newMap = new HashMap();
			for(Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();iterator.hasNext();) {
				Entry<String, Object> next = iterator.next();
				String key = next.getKey();
				if(key.toString().indexOf("enc_") != -1) {
					byte[] value = (byte[]) next.getValue();
					String val = value == null? "": aes_decrypt(value,"novogene@2019/cn");
					newMap.put(key.toString().substring(4), val);
				}else {
					newMap.put(key, next.getValue());
				}
			}
			newList.add(newMap);
		}
		return newList;
	}
}
