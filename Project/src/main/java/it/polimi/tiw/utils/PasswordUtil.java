package it.polimi.tiw.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * This class provides a function to quickly apply an Hashing algoritm to a String,
 * used to encrypt some text like Passwords.
 */
public class PasswordUtil {
	
	/*
	 * Returns the hashed String corresponding to the password passed as parameter.
	 * It uses the SHA-256 algorithm.
	 */
    public static String hashPassword(String password) {
    	
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            byte[] hash = md.digest(password.getBytes());
            
            // Convert byte array to hex string
            StringBuilder hex = new StringBuilder();
            
            for (byte b : hash) {
                String hexString = Integer.toHexString(0xff & b);
                if (hexString.length() == 1) hex.append('0');
                hex.append(hexString);
            }
            
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }
}
