package com.vishipel.social.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.apache.log4j.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.vishipel.model.SocialUser;
import com.vishipel.social.SocialAuth;


public class GoogleAuth implements SocialAuth {

	static Logger logger = Logger.getLogger(GoogleAuth.class);
	@Override
	public SocialUser verifySocialUser(String thirdToken) {
		// TODO Auto-generated method stub
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
				new NetHttpTransport(), new JacksonFactory()).setAudience(
				Collections.singletonList("526538537920-84fbijad5npn0um5ff3ecqipc2jer06m.apps.googleusercontent.com")).build();
		SocialUser socialuser = new SocialUser();
		try {
			GoogleIdToken idToken = verifier.verify(thirdToken);
			logger.info("idToken : " + idToken);
			if(idToken != null){
				Payload payload = idToken.getPayload();
				
				socialuser.setUserId(payload.getSubject());
				socialuser.setEmail(payload.getEmail());
				socialuser.setName(payload.get("name").toString());
				socialuser.setPrictureUrl(payload.get("picture").toString());
				socialuser.setLocate(payload.get("locale").toString());
				socialuser.setFamilyName(payload.get("family_name").toString());
				socialuser.setGivenName(payload.get("given_name").toString());
				logger.info("user email  : " + socialuser.getEmail());
			} else{
				logger.info("Invalid ID token.");
				socialuser = null;
			}
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socialuser;
	}
	/*@Override
	public String getAccessToken(String code) {
		// TODO Auto-generated method stub
		return null;
	}*/

}
