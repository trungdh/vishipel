package com.vishipel.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Random;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import org.apache.log4j.Logger;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKey.Factory;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import com.google.gson.stream.JsonReader;

public class Utils {
	
	static final Logger logger = Logger.getLogger(Utils.class);

	static final private String DIGITAL = "0123456789";
	final private static Random ran = new SecureRandom();
	private static JsonWebKey jwKey = null;
	
	public static final String SITE_VERIFY_URL = //
            "https://www.google.com/recaptcha/api/siteverify";
	
	//Setting up Direct Symmetric Encryption and Decryption 
	final static String jwkJson = "{\"kty\":\"oct\",\"k\":\"9d6722d6-b45c-4dcb-bd73-2e057c44eb93-928390\"}";
	
	
	public static JsonWebKey initializationJsonWebKey(){
		try {
			new JsonWebKey.Factory();
			jwKey = Factory.newJwk(jwkJson);
		} catch (JoseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jwKey;
	}
	
	public static JsonWebKey getJsonWebKeyInstance(){
		if(jwKey != null){
			return jwKey;
		}else{
			return initializationJsonWebKey();
		}
	}
	
	public static JwtConsumer getJwtConsumer(){
		JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime()
				.setAllowedClockSkewInSeconds(30)
				.setRequireSubject()
				.setExpectedIssuer("com.vishipel")
				.setDecryptionKey(getJsonWebKeyInstance().getKey())
				.setVerificationKey(getJsonWebKeyInstance().getKey()).build();
		return jwtConsumer;
	}
	
	public static String ComputeHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.reset();
		md.update(password.getBytes("UTF-8"));
		return toHexString(md.digest());
		
	}
	
	public static String genUniqueUUID(int length){
		
		StringBuilder sb = new StringBuilder();
		while(length > 0){
			length--;
			sb.append(randomChar());
		}
		logger.info("genUniqueUUID : " + sb.toString());
		return sb.toString();
		
//		UUID uuid =  UUID.randomUUID();
//		long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
//		return Long.toString(l, Character.MAX_RADIX);
		//return UUID.fromString(email).toString();
	}
	
	public static String getUniqueOTP(){
		return UUID.randomUUID().toString();
	}
	
	public static Date getExpirationOTPTime(){
		Date date = new java.util.Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 15);
		Date expirationDate  = cal.getTime();
		return expirationDate;
	}
	
	public static boolean verifyCaptcha(String gRecaptcharResponse){
		if (gRecaptcharResponse == null || gRecaptcharResponse.length() == 0) {
            return false;
        }
		
		try {
			URL verifyUrl = new URL(SITE_VERIFY_URL);
			//open connection
			HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();
			//add header
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
         // Data will be sent to the server.
            String postParams = "secret=" + Constants.GOOGLE_RECAPTCHA_SECRET_KEY_API //
                    + "&response=" + gRecaptcharResponse;
            //send request
            conn.setDoOutput(true);
            
            //get the output stream of connection
            //wrire data in this stream which means to send data to server
            OutputStream os = conn.getOutputStream();
            os.write(postParams.getBytes());
            
            os.flush();
            os.close();
            
            //response code from google server
            int responseCode = conn.getResponseCode();
            logger.info("google captcha response code : " + responseCode);
            
            //get the input stream of connection to read data send from the server
            InputStream is = conn.getInputStream();
            
            
			return false;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	private static char randomChar(){
		return DIGITAL.charAt(ran.nextInt(DIGITAL.length()));
	}
	
	private static String toHexString(byte[] data){
		Formatter formatter = new Formatter();
		for(byte b : data){
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
