package com.vishipel.dao;

import javax.ws.rs.core.Response;
import com.vishipel.model.Customer;

public interface CustomerDAO {
	
	public Response getCustomer(String p_MaKH);
	
	public Response createCustomer(Customer customer);
	
	public Response updateCustomer(Customer customer);
	
	public Response deleteCustomer(int id);
	
	public Response getAllCustomers();

}
