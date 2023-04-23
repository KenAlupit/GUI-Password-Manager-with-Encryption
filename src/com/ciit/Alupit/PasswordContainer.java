package com.ciit.Alupit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PasswordContainer implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String username;
	private byte[] password;
	
	private SecretKey myDesKey;
	private transient Cipher desCipher;
	
	
	PasswordContainer(){
		this.name = null;
		this.username = null;
		this.myDesKey = null;
		this.password = new byte[0];
		try {
			this.desCipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public byte[] getPassword() {
		try {
		 	if (myDesKey == null) {
	            KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
	            myDesKey = keygenerator.generateKey();
		 	}
		 	Cipher cipher = getCipher();
		 	cipher.init(Cipher.DECRYPT_MODE, myDesKey);
		 	byte[] decryptedData  = desCipher.doFinal(this.password);
		 	
		 	return decryptedData;
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		return this.password;
	}
	public void setPassword(byte[] password) {
		 try {
			 	if (myDesKey == null) {
		            KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
		            myDesKey = keygenerator.generateKey();
			 	}
			 	Cipher cipher = getCipher();
			 	cipher.init(Cipher.ENCRYPT_MODE, myDesKey);
	            this.password = desCipher.doFinal(password);
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	        }
	}
	 private void writeObject(ObjectOutputStream out) throws IOException {
	        out.defaultWriteObject();
	        if (myDesKey != null) {
	            out.writeObject(myDesKey.getEncoded());
	        }
	    }

	    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
	        in.defaultReadObject();
	        byte[] encodedKey = (byte[]) in.readObject();
	        if (encodedKey != null) {
	            myDesKey = new SecretKeySpec(encodedKey, "AES");
	        }
	        try {
	            desCipher = Cipher.getInstance("AES");
	        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
	            e.printStackTrace();
	        }
	    }

	    private Cipher getCipher() {
	        if (desCipher == null) {
	            try {
	                desCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
	                e.printStackTrace();
	            }
	        }
	        return desCipher;
	    }
}
