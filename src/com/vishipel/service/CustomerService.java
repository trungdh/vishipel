package com.vishipel.service;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;

import com.vishipel.dao.CustomerDAO;
import com.vishipel.dao.Impl.CustomerDAOImpl;
import com.vishipel.model.StatusMessage;
import com.vishipel.utils.Utils;

@Path("customer")
public class CustomerService {

	private Logger logger = Logger.getLogger(CustomerService.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerInfo(@HeaderParam("access_token") String token,
			@QueryParam("customer_code") String customerCode) {

		logger.info("inside getCunstomerInfo...");

		if (token == null) {
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage
					.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode())
					.type(MediaType.APPLICATION_JSON).entity(statusMessage)
					.build();
		}

		JsonWebKey jwKey = Utils.getJsonWebKeyInstance();
		logger.info("JWK : " + jwKey.toJson());

		// validate Token's authenticity and check claims
		JwtConsumer jwtConsumer = Utils.getJwtConsumer();
		try {
			JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
			logger.info("JWT validation succeeded " + jwtClaims);
		} catch (InvalidJwtException e) {
			// TODO: handle exception
			logger.error("JWT is Invalid : " + e);
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage
					.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode())
					.type(MediaType.APPLICATION_JSON).entity(statusMessage)
					.build();
		}

		CustomerDAO customerDAO = new CustomerDAOImpl();
		Response res = customerDAO.getCustomer(customerCode);
		return res;
	}

}
