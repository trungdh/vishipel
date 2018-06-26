package com.vishipel.dao;

import java.sql.Date;

import javax.ws.rs.core.Response;

import com.vishipel.model.Postage;

public interface PostageDAO {

	public Response getAllPostrage();
	
	public Response getPostage(int p_id);
	
	public Response getPostageByDate( Date p_date);
	
	public Response createPostage(Postage p_postrage);

	public Response updatePostage(Postage p_postrage);
	
	public Response deletePostage (int p_id);
}
