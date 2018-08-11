package com.vishipel.social.facebook;


import org.apache.log4j.Logger;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import com.vishipel.model.SocialUser;
import com.vishipel.social.SocialAuth;
import com.vishipel.utils.Constants;

public class FacebookAuth implements SocialAuth {
	
	static Logger logger = Logger.getLogger(FacebookAuth.class);

	@Override
	public SocialUser verifySocialUser(String thirdToken) {
		// TODO Auto-generated method stub
		SocialUser socialuser = new SocialUser();
		try {
//			AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(Constants.FACEBOOK_APP_ID, Constants.FACEBOOK_APP_SECRET);
			FacebookClient facebookClient = new DefaultFacebookClient(thirdToken);
			User fbUser = facebookClient.fetchObject("me", User.class,Parameter.with("fields", "id,email,name,first_name,last_name"));
			logger.info("fbUser "+ fbUser);
			socialuser.setEmail(fbUser.getEmail());
			socialuser.setFamilyName(fbUser.getLastName());
			socialuser.setGivenName(fbUser.getFirstName());
			socialuser.setPrictureUrl(fbUser.getLink());
			logger.info("fbClient : " + facebookClient + " fbUser : " + fbUser);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("exception : " + e);
			socialuser = null;
		}
		return socialuser;
	}

	/*@Override
	public String getAccessToken(String code) {
		// TODO Auto-generated method stub
		return null;
	}*/

}
