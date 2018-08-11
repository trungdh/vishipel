package com.vishipel.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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

import com.sun.jersey.multipart.FormDataParam;
import com.vishipel.dao.PostageDAO;
import com.vishipel.dao.Impl.PostageDAOImpl;
import com.vishipel.model.Postage;
import com.vishipel.model.StatusMessage;
import com.vishipel.utils.Utils;

@Path("postage")
public class PostageService {

	private Logger logger = Logger.getLogger(PostageService.class);

	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
	public Response getPostageDetail(@HeaderParam("access_token") String token, @QueryParam("customer") String customerId,
			@QueryParam("type") String type, @QueryParam("month") String month,
			@QueryParam("year") String year) throws JsonGenerationException,
			JsonMappingException, IOException {
		PostageDAO postageDAO = new PostageDAOImpl();
		logger.info("inside getPostageDetails method...");
		
		if(token == null){
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode()).type(MediaType.APPLICATION_JSON)
					.entity(statusMessage).build();
		}
		
		JsonWebKey jwKey = Utils.getJsonWebKeyInstance();
		logger.info("JWK : " + jwKey.toJson());
		
		//validate Token's authenticity and check claims
		JwtConsumer jwtConsumer = Utils.getJwtConsumer();
		try {
			JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
			logger.info("JWT validation succeeded " + jwtClaims);
		} catch (InvalidJwtException e) {
			// TODO: handle exception
			logger.error("JWT is Invalid : " + e);
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode()).type(MediaType.APPLICATION_JSON).entity(statusMessage).build();
		}
		
		Response res = postageDAO.getPostageByFilters(customerId,type,month,year);
		return res;
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadPostage(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("customer_code") String customerCode,
			@FormDataParam("month") String month,
			@FormDataParam("postage_type") String type,
			@FormDataParam("publish_date") String publishDate,
			@FormDataParam("is_auto") String isAuto,
			@FormDataParam("update_person") String updatePerson) throws IOException {

		logger.info("customer_code:  " + customerCode + " month : " + month + " postage_type : " + type + " publish_date : " + publishDate + " is_auto : " + isAuto + " update_person : " + updatePerson);
		Response res = null;
		Blob blob = null;
		PostageDAO postageDAO = new PostageDAOImpl();
		Postage postage = null;
		/*byte[] content = new byte[1024];
		IOUtils.readFully(uploadedInputStream, content);*/
		try {
			SimpleDateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			java.util.Date d = (java.util.Date) f.parse(month);
			//blob = new SerialBlob(content);
			postage = new Postage();
			postage.setCustomerCode(customerCode);
			postage.setMonth(new java.sql.Date(d.getTime()));
			postage.setPostageType(type);
			//postage.setFiles(blob);
			postage.setPublishDay(new java.sql.Date(d.getTime()));
			postage.setIsAuto(isAuto);
			postage.setUpdatePerson(updatePerson);
			logger.info("postage input : " + postage);
			res = postageDAO.createPostage(postage,uploadedInputStream);
		/*} catch (SerialException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();*/
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return res;

	}

}
