package com.vishipel.dao;

import javax.ws.rs.core.Response;

import com.vishipel.model.Users;

public interface UsersDAO {

	public Users validate(String email, String password);
	
	public Response createUser(Users users);
	
	public Response changePassword(Users user, String new_password);
	
	public boolean isExist(String email);
}
