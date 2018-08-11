package com.vishipel.model;

public class SocialUser {

	private String userId;
	private String email;
	private String name;
	private String prictureUrl;
	private String locate;
	private String familyName;
	private String givenName;

	public SocialUser() {

	}

	public SocialUser(String userId, String email, String name,
			String prictureUrl, String locate, String familyName,
			String givenName) {
		super();
		this.userId = userId;
		this.email = email;
		this.name = name;
		this.prictureUrl = prictureUrl;
		this.locate = locate;
		this.familyName = familyName;
		this.givenName = givenName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrictureUrl() {
		return prictureUrl;
	}

	public void setPrictureUrl(String prictureUrl) {
		this.prictureUrl = prictureUrl;
	}

	public String getLocate() {
		return locate;
	}

	public void setLocate(String locate) {
		this.locate = locate;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

}
