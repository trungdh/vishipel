package com.vishipel.service;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;

import com.vishipel.dao.NewsDAO;
import com.vishipel.dao.Impl.NewsDAOImpl;
import com.vishipel.model.StatusMessage;
import com.vishipel.utils.Utils;

@Path("news")
public class NewsService {
	
	private Logger logger = Logger.getLogger(NewsService.class);	
	private NewsDAO newsDao = null;

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllNews(@HeaderParam("access_token") String token)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info("Inside showAllNews method...");
		if (token == null) {
			StatusMessage statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode())
					.entity(statusMessage).build();
		}

		JsonWebKey jwKey = Utils.getJsonWebKeyInstance();
		
		logger.info("JWK (1) : " + jwKey.toJson());
		
		//validate Token's authenticity and check claims
		JwtConsumer jwtConsumer = Utils.getJwtConsumer();
		try {
			JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
			logger.info("JWT validation succeeded! " + jwtClaims);
		} catch (InvalidJwtException e) {
			// TODO: handle exception
			logger.error("JWT is Invalid: " + e);
			StatusMessage statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode()).entity(statusMessage).build();

		}
		
		newsDao = new NewsDAOImpl();
		Response res = newsDao.getAllNews();
		return res;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNewsByType(@HeaderParam("access_token") String token, @QueryParam("news_type") String type){
		
		logger.info("Inside getNewsByType method...news_type : " + type);
		if (token == null) {
			StatusMessage statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode())
					.entity(statusMessage).build();
		}
		
		JsonWebKey jwKey = Utils.getJsonWebKeyInstance();
		
		logger.info("JWK (1) : " + jwKey.toJson());
		
		//validate Token's authenticity and check claims
		JwtConsumer jwtConsumer = Utils.getJwtConsumer();
		try {
			JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
			logger.info("JWT validation succeeded! " + jwtClaims);
		} catch (InvalidJwtException e) {
			// TODO: handle exception
			logger.error("JWT is Invalid: " + e);
			StatusMessage statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode()).entity(statusMessage).build();

		}
		
		newsDao = new NewsDAOImpl();
		Response res = newsDao.getNewsByType(type);
		return res;
		
	}
}
