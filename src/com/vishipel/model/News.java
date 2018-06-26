package com.vishipel.model;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class News {

	private int newsId;
	private String title;
	private String link;
	private String type;
	private String updatePerson;
	private Date updateAt;
	
	@JsonProperty(value="ID")
	public int getNewsId() {
		return newsId;
	}
	
	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}
	
	@JsonProperty(value="TITLE")
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@JsonProperty(value="LINK")
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	@JsonProperty(value="LOAITIN")
	public String getType() {
		return type;
	}
	
	public void setType(String string) {
		this.type = string;
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
