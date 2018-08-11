package com.vishipel.model;

import java.sql.Blob;
import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Postage {
	
	private int Id;
	private String customerCode;
	private Date month;
	private String postageType;
	private Blob files;
	private Date publishDay;
	private String isAuto;
	private String updatePerson;
	private Date updateAt;
	
	
	@JsonProperty(value="ID")
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}

	@JsonProperty(value="MAKH")
	public String getCustomerCode() {
		return customerCode;
	}
	
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	
	@JsonProperty(value="THANG")
	public Date getMonth() {
		return month;
	}
	
	public void setMonth(Date month) {
		this.month = month;
	}
	
	@JsonProperty(value="LOAICUOC")
	public String getPostageType() {
		return postageType;
	}
	
	public void setPostageType(String postageType) {
		this.postageType = postageType;
	}
	
	@JsonProperty(value="FILES")
	public Blob getFiles() {
		return files;
	}
	
	public void setFiles(Blob files) {
		this.files = files;
	}
	
	@JsonProperty(value="NGAYPH")
	public Date getPublishDay() {
		return publishDay;
	}
	
	public void setPublishDay(Date publishDay) {
		this.publishDay = publishDay;
	}
	
	@JsonProperty(value="TUDONG")
	public String getIsAuto() {
		return isAuto;
	}
	
	public void setIsAuto(String isAuto) {
		this.isAuto = isAuto;
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
