package com.vishipel.dao;

import java.io.InputStream;
import java.sql.Date;

import javax.ws.rs.core.Response;

import com.vishipel.model.Postage;

public interface PostageDAO {

	public Response getAllPostrage();
	
	public Response getPostage(int p_id);
	
	public Response getPostageByDate( Date p_date);
	
	public Response getPostageByFilters(String customerCode, String type, String month, String year);
	
	public Response createPostage(Postage p_postrage, InputStream is);

	public Response updatePostage(Postage p_postrage);
	
	public Response deletePostage (int p_id);
}
