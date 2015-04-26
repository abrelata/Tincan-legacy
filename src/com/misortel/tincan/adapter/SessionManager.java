package com.misortel.tincan.adapter;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.sip.SipManager;

public class SessionManager{
	Context context;
	
	public SessionManager(Context context){
		this.context = context;
	}
    public boolean isOnline(){
    	ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (cn != null){
    		NetworkInfo[] info = cn.getAllNetworkInfo();
    		if (info != null)
    			for (int i = 0; i < info.length; i++)
    				if (info[i].getState() == NetworkInfo.State.CONNECTED){
    					return true;
					}
		}
    	return false;
	}
    public boolean isAPISupported(){
    	return SipManager.isApiSupported(context);
    }
    
    //ENCRYPTION
    public static byte[] encrypt(String key, String value) throws GeneralSecurityException {
    	byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
    	if (raw.length != 16) {
    		throw new IllegalArgumentException("Invalid key size.");
    	}
    	SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
    	return cipher.doFinal(value.getBytes(Charset.forName("US-ASCII")));
    }
    
    public static String decrypt(String key, byte[] encrypted) throws GeneralSecurityException {
    	byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
    	if (raw.length != 16) {
    		throw new IllegalArgumentException("Invalid key size.");
    	}
    	SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
    	
    	byte[] original = cipher.doFinal(encrypted);
    	return new String(original, Charset.forName("US-ASCII"));
	}
}