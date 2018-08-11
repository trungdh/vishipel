package com.vishipel.model;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Users {

	private int Id;
	private String email;
	private String password;
	private String isActive;
	private String OTP;
	private Date OTPExpiredTime;
	private String customerId;
	private Date createAt;
	private String createPerson;
	private String updatePerson;
	private Date updateAt;
	
	@JsonProperty(value="ID")
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	@JsonProperty(value="EMAIL")
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@JsonProperty(value="PWD")
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonProperty(value="HOATDONG")
	public String getIsActive() {
		return isActive;
	}
	
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	@JsonProperty(value="OTP")
	public String getOTP() {
		return OTP;
	}
	
	public void setOTP(String oTP) {
		OTP = oTP;
	}
	
	@JsonProperty(value="OTPTM")
	public Date getOTPExpiredTime() {
		return OTPExpiredTime;
	}
	
	public void setOTPExpiredTime(Date oTPExpiredTime) {
		OTPExpiredTime = oTPExpiredTime;
	}
	
	@JsonProperty(value="MAKH")
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@JsonProperty(value="NGAYTAO")
	public Date getCreateAt() {
		return createAt;
	}
	
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	@JsonProperty(value="NGUOITAO")
	public String getCreatePerson() {
		return createPerson;
	}
	
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	
	@JsonProperty(value="NGUOICN")
	public String getUpdatePerson() {
		return updatePerson;
	}
	
	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}
	
	@JsonProperty(value="NGAYCN")
	public Date getUpdateAt() {
		return updateAt;
	}
	
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	
	
}
