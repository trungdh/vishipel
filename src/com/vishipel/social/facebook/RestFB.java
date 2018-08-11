package com.vishipel.social.facebook;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vishipel.utils.Constants;

public class RestFB {

	public static String getToken(final String code) throws ClientProtocolException, IOException {
		String link = String.format(Constants.FACEBOOK_LINK_GET_TOKEN, Constants.FACEBOOK_APP_ID,Constants.FACEBOOK_APP_SECRET,Constants.FACEBOOK_REDIRECT_URL,code);
		String response = Request.Get(link).execute().returnContent().asString();
		
		JsonObject jObject = new Gson().fromJson(response, JsonObject.class);
		String accessToken = jObject.get("access_token").toString().replaceAll("\"", "");
		
		return accessToken;
	}
	
	
}
