package com.vishipel.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKey.Factory;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.lang.JoseException;

import com.restfb.types.StoryAttachment.Media;
import com.sun.istack.NotNull;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.vishipel.dao.UsersDAO;
import com.vishipel.dao.Impl.UserDAOImpl;
import com.vishipel.model.SocialUser;
import com.vishipel.model.StatusMessage;
import com.vishipel.model.Users;
import com.vishipel.social.facebook.FacebookAuth;
import com.vishipel.social.google.GoogleAuth;
import com.vishipel.utils.Utils;

@Path("user")
public class UsersService {
	
	private enum LoginType {
		systems, facebook, google
	}
	
	static UsersDAO usersDAO = null;
	
	static Logger logger = Logger.getLogger(UsersService.class);	
	static JsonWebKey jwKey = null;
	
	static {
		logger.info("inside static initializer...");
		//Setting up Direct Symmetric Encryption and Decryption 
		usersDAO = new UserDAOImpl();
		jwKey = Utils.getJsonWebKeyInstance();
	}
	
	@POST
	@Path("/authenticate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticateCredentials(@HeaderParam("email") String email,
			@HeaderParam("password") String password, @HeaderParam("accessToken") String accessToken,
			@HeaderParam("type") String loginType)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		logger.info("Authenticating User Credentials...loginType : " + loginType);

		StatusMessage<Users> statusMessage = null;
		String jweSerialization = null;
		
		if(loginType == null){
			statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.PRECONDITION_FAILED.getStatusCode());
			statusMessage.setMessage("login type value is missing...");
			return Response.status(Status.PRECONDITION_FAILED.getStatusCode()).entity(statusMessage).build();
		}
		
		LoginType type = LoginType.valueOf(loginType);
		
		switch (type) {
		case systems:
			if(email == null){
				statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.PRECONDITION_FAILED.getStatusCode());
				statusMessage.setMessage("email value is missing...");
				return Response.status(Status.PRECONDITION_FAILED.getStatusCode()).entity(statusMessage).build();
			}
			
			Users user = usersDAO.validate(email, password);
			logger.info("user after validate : " + user);
			if(user == null){
				statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
				statusMessage.setMessage("Access dined for this functionality...");
				return Response.status(Status.FORBIDDEN.getStatusCode()).entity(statusMessage).build();
			}
			jweSerialization = getJWEToken(user);
			user.setPassword(null); //not return password and OTP
			user.setOTP(null);
			statusMessage = new StatusMessage<Users>();
			statusMessage.setStatus(Status.OK.getStatusCode());
			statusMessage.setMessage(jweSerialization);
			statusMessage.setData(user);
			logger.info("statusMessage : " + statusMessage);
			
			return Response.status(Status.OK.getStatusCode()).entity(statusMessage).build();
			
		case facebook:
			if(accessToken == null){
				statusMessage = new StatusMessage<Users>();
				statusMessage.setStatus(Status.PRECONDITION_FAILED.getStatusCode());
				statusMessage.setMessage("facebook access token value is missing...");
				return Response.status(Status.PRECONDITION_FAILED.getStatusCode()).entity(statusMessage).build();
			}
			
			
			FacebookAuth facebookAuth = new FacebookAuth();
			SocialUser fbUser = facebookAuth.verifySocialUser(accessToken);
			if(fbUser == null){
				statusMessage = new StatusMessage<Users>();
				statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
				statusMessage.setMessage("Fail while verify facebook user...");
				return Response.status(Status.FORBIDDEN.getStatusCode()).entity(statusMessage).build();
			}
			
			Users fb_user = usersDAO.validate(fbUser.getEmail(), null);
			if(fb_user == null){
				statusMessage = new StatusMessage<Users>();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage("User not found in database...");
				return Response.status(Status.NOT_FOUND.getStatusCode()).entity(statusMessage).build();
			}
			
			jweSerialization = getJWEToken(fb_user);
			fb_user.setPassword(null); //not return password and OTP
			fb_user.setOTP(null);
			statusMessage = new StatusMessage<Users>();
			statusMessage.setStatus(Status.OK.getStatusCode());
			statusMessage.setMessage(jweSerialization);
			statusMessage.setData(fb_user);
			logger.info("statusMessage : " + statusMessage);
			
			return Response.status(Status.OK.getStatusCode()).entity(statusMessage).build();
		case google:
			if(email == null){
				statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.PRECONDITION_FAILED.getStatusCode());
				statusMessage.setMessage("email value is missing...");
				return Response.status(Status.PRECONDITION_FAILED.getStatusCode()).entity(statusMessage).build();
			}
//			if(accessToken == null){
//				statusMessage = new StatusMessage<Users>();
//				statusMessage.setStatus(Status.PRECONDITION_FAILED.getStatusCode());
//				statusMessage.setMessage("google access token value is missing...");
//				return Response.status(Status.PRECONDITION_FAILED.getStatusCode()).entity(statusMessage).build();
//			}
			
//			GoogleAuth googleAuth = new GoogleAuth();
//			SocialUser ggUser = googleAuth.verifySocialUser(accessToken);
//			if(ggUser == null){
//				statusMessage = new StatusMessage<Users>();
//				statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
//				statusMessage.setMessage("Fail while verify Goolge user...");
//				return Response.status(Status.FORBIDDEN.getStatusCode()).entity(statusMessage).build();
//			}
			
			Users gg_User = usersDAO.validate(email, null);
			if(gg_User == null){
				statusMessage = new StatusMessage<Users>();
				statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
				statusMessage.setMessage("Access dined for this functionality...");
				return Response.status(Status.FORBIDDEN.getStatusCode()).entity(statusMessage).build();
			}
			
			jweSerialization = getJWEToken(gg_User);
			gg_User.setPassword(null); //not return password and OTP
			gg_User.setOTP(null);
			statusMessage = new StatusMessage<Users>();
			statusMessage.setStatus(Status.OK.getStatusCode());
			statusMessage.setMessage(jweSerialization);
			statusMessage.setData(gg_User);
			logger.info("statusMessage : " + statusMessage);
			
			return Response.status(Status.OK.getStatusCode()).entity(statusMessage).build();

		default:
			statusMessage = new StatusMessage<Users>();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage.setMessage("Wrong login type...");
			return Response.status(Status.FORBIDDEN.getStatusCode()).entity(statusMessage).build();
		}
	}
	
	private String getJWEToken(Users users){
		
		String jweSerialization = null;
		
		JwtClaims claims = new JwtClaims();
		claims.setIssuer("com.vishipel");
		claims.setExpirationTimeMinutesInTheFuture(10);
		claims.setGeneratedJwtId();
		claims.setIssuedAtToNow();
		claims.setNotBeforeMinutesInThePast(2);
		claims.setSubject(users.getEmail());
		
		JsonWebSignature jws = new JsonWebSignature();
		logger.info("Claims : "  + claims.toJson());
		
		//The payload of the JWS is json content of the JWT Claims
		jws.setPayload(claims.toJson());
		jws.setKeyIdHeaderValue(jwKey.getKeyId());
		jws.setKey(jwKey.getKey());
		
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
		
		String jwt = null;
		try {
			jwt = jws.getCompactSerialization();
		} catch (JoseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(jwKey.getKey());
		jwe.setKeyIdHeaderValue(jwKey.getKeyId());
		jwe.setContentTypeHeaderValue("JWT");
		jwe.setPayload(jwt);
		
		try {
			jweSerialization = jwe.getCompactSerialization();
		} catch (JoseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return jweSerialization;
		
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(Users users){
		UsersDAO usersDAO = new UserDAOImpl();
		logger.info("inside register User...");
		Response res = usersDAO.createUser(users);
		return res;
		
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@HeaderParam("access_token") String token, @HeaderParam("email") String email){
		
		if(token == null){
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode())
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
			return Response.status(Status.FORBIDDEN.getStatusCode()).entity(statusMessage).build();
		}
		
		if(usersDAO.isExist(email)){
			logger.error("logout successfully..");
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.OK.getStatusCode());
			statusMessage.setMessage("Logout successfully...");
			return Response.status(Status.OK.getStatusCode()).entity(statusMessage).build();
		} else {
			logger.error("Logout failed, not existed email..." + email);
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
			statusMessage.setMessage("Logout failed, not existed email..." + email);
			return Response.status(Status.NOT_FOUND.getStatusCode()).entity(statusMessage).build();
		}
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changePassword(@HeaderParam("access_token") String token,
			@HeaderParam("email") String email,
			@HeaderParam("old_pass") String oldPass,
			@HeaderParam("new_pass") String newPass) {
		if(token == null){
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.FORBIDDEN.getStatusCode());
			statusMessage.setMessage("Access Denied for this functionality !!!");
			return Response.status(Status.FORBIDDEN.getStatusCode()).type(MediaType.APPLICATION_JSON)
					.entity(statusMessage).build();
		}
		
		if(email == null){
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.BAD_REQUEST.getStatusCode());
			statusMessage.setMessage("email is required...");
			return Response.status(Status.BAD_REQUEST.getStatusCode()).type(MediaType.APPLICATION_JSON)
					.entity(statusMessage).build();
		}
		
		if(oldPass == null){
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.BAD_REQUEST.getStatusCode());
			statusMessage.setMessage("old password is required...");
			return Response.status(Status.BAD_REQUEST.getStatusCode()).type(MediaType.APPLICATION_JSON)
					.entity(statusMessage).build();
		}
		
		if(newPass == null){
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.BAD_REQUEST.getStatusCode());
			statusMessage.setMessage("new password is required...");
			return Response.status(Status.BAD_REQUEST.getStatusCode()).type(MediaType.APPLICATION_JSON)
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
		
		Users users = usersDAO.validate(email, oldPass);
		if(users == null){
			StatusMessage<?> statusMessage = new StatusMessage();
			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
			statusMessage.setMessage("user is not existed, check the email or password...");
			return Response.status(Status.NOT_FOUND.getStatusCode()).type(MediaType.APPLICATION_JSON)
					.entity(statusMessage).build();
		}
		
		Response res = usersDAO.changePassword(users, newPass);
		
		return res;
	}
	
	/*@GET
	@Path("/google/callback")
	@Produces(MediaType.APPLICATION_JSON)
	public Response handleGoogleCallback(@NotNull @QueryParam("code") String code) throws InterruptedException, ExecutionException, IOException {
		
		return null;
	}
	
	@GET
	@Path("facebook/callback")
	@Produces(MediaType.APPLICATION_JSON)
	public Response handleFacebookCallback(@NotNull @QueryParam("code") String code) throws InterruptedException, ExecutionException, IOException {
		
		return null;
	}*/
}
