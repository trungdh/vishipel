package com.vishipel.social;

import com.vishipel.model.SocialUser;

public interface SocialAuth {
	
	/*public String getAccessToken(String code);*/
	
	public SocialUser verifySocialUser(String thirdToken);

}
