package com.vishipel.service;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.vishipel.dao.NewsDAO;
import com.vishipel.dao.Impl.NewsDAOImpl;

@Path("news")
public class NewsService {
	
	private Logger logger = Logger.getLogger(NewsService.class);	

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllNews() throws JsonGenerationException,JsonMappingException,IOException{
		NewsDAO newsDao = new NewsDAOImpl();
		logger.info("Inside showAllNews method...");
		Response res = newsDao.getAllNews();
		return res;
	}
}
