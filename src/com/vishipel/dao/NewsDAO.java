package com.vishipel.dao;

import javax.ws.rs.core.Response;

import com.vishipel.model.News;

public interface NewsDAO {
	
	public Response getAllNews();
	
	public Response getNews(int p_id);
	
	public Response getNewsByType(String type);
	
	public Response createNews(News p_news);
	
	public Response updateNews(News p_news);
	
	public Response deleteNews(int p_id);

}
