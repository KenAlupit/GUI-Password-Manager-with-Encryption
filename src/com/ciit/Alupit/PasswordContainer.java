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

//An object class that contains the name, username, and password
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
	
	//Returns the name variable
	public String getName() {
		return name;
	}
	
	//Sets the name variable
	public void setName(String name) {
		this.name = name;
	}
	
	//Returns the username variable
	public String getUsername() {
		return username;
	}
	
	//Sets the username variable
	public void setUsername(String username) {
		this.username = username;
	}
	
	//Returns the unencrypted byte array value of the password
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
	
	//Encrypts the password and sets it
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
	
	//Saves the secret key for serialization
	private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (myDesKey != null) {
            out.writeObject(myDesKey.getEncoded());
        }
    }
	
	//Retrieves the secret key for deserialization
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
    
    //Returns the cipher
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
