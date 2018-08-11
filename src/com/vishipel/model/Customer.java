package com.vishipel.model;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;



public class Customer {

	private int customerId;
	private String customerCode;
	private String customerName;
	private String global;
	private String MST;
	private String phoneNumber;
	private String address;
	private String fax;
	private String email;
	private Date createAt;
	private String createPerson;
	private Date updateAt;
	private String updatePerson;
	
	@JsonProperty(value="ID")
	public int getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@JsonProperty(value="MAKH")
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	@JsonProperty(value="TENKH")
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	@JsonProperty(value="QUOCTE")
	public String getGlobal() {
		return global;
	}
	
	public void setGlobal(String global) {
		this.global = global;
	}
	
	@JsonProperty(value="MST")
	public String getMST() {
		return MST;
	}
	
	public void setMST(String mST) {
		MST = mST;
	}
	
	@JsonProperty(value="DIENTHOAI")
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@JsonProperty(value="DIACHI")
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@JsonProperty(value="FAX")
	public String getFax() {
		return fax;
	}
	
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@JsonProperty(value="EMAIL")
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
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
	
	@JsonProperty(value="NGAYCN")
	public Date getUpdateAt() {
		return updateAt;
	}
	
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	
	@JsonProperty(value="NGUOICN")
	public String getUpdatePerson() {
		return updatePerson;
	}
	
	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}
	
	
}
