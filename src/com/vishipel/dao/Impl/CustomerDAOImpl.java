package com.vishipel.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.vishipel.dao.CustomerDAO;
import com.vishipel.model.Customer;
import com.vishipel.model.StatusMessage;
import com.vishipel.utils.Database;
import com.vishipel.utils.Utils;

public class CustomerDAOImpl implements CustomerDAO {

	private DataSource datasource = Database.getDataSource();
	private Logger logger = Logger.getLogger(CustomerDAOImpl.class);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private Statement stmt = null;

	@Override
	public Response getCustomer(String p_MaKH) {
		// TODO Auto-generated method stub
		logger.info("inside get Customer...");
		String sql = "select * from UM_CUSTOMERS where MAKH = ?";
		Customer customer = null;
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, p_MaKH);
			rs = ps.executeQuery();
			customer = new Customer();
			while(rs.next()){
				customer.setCustomerId(rs.getInt("ID"));
				customer.setCustomerCode(rs.getString("MAKH"));
				customer.setCustomerName(rs.getString("TENKH"));
				customer.setGlobal(rs.getString("QUOCTE"));
				customer.setMST(rs.getString("MST"));
				customer.setPhoneNumber(rs.getString("DIENTHOAI"));
				customer.setAddress(rs.getString("DIACHI"));
				customer.setFax(rs.getString("FAX"));
				customer.setEmail(rs.getString("EMAIL"));
				customer.setCreateAt(rs.getDate("NGAYTAO"));
				customer.setCreatePerson(rs.getString("NGUOITAO"));
				customer.setUpdateAt(rs.getDate("NGAYCN"));
				customer.setUpdatePerson(rs.getString("NGUOICN"));
			}
			
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		StatusMessage<Customer> statusMessage = new StatusMessage<Customer>();
		statusMessage.setStatus(Status.OK.getStatusCode());
		statusMessage.setMessage("Customer info...");
		statusMessage.setData(customer);
		return Response.status(Status.OK.getStatusCode()).entity(statusMessage).build();
	}

	@Override
	public Customer createCustomer(Customer customer) {
		logger.info("inside create Customer...");

		String sql = "insert into UM_CUSTOMERS (MAKH,TENKH,QUOCTE,MST,DIENTHOAI,DIACHI,FAX,EMAIL,NGUOITAO,NGUOICN) values (?,?,?,?,?,?,?,?,?,?)";

		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, Utils.genUniqueUUID(12));
			ps.setString(2, customer.getCustomerName());
			ps.setString(3, customer.getGlobal());
			ps.setString(4, customer.getMST());
			ps.setString(5, customer.getPhoneNumber());
			ps.setString(6, customer.getAddress());
			ps.setString(7, customer.getFax());
			ps.setString(8, customer.getEmail());
			ps.setString(9, customer.getCreatePerson());
			ps.setString(10, customer.getUpdatePerson());

			int rows = ps.executeUpdate();

			if (rows == 0) {
				logger.error("Unable to create customer...");
				return null;
			}

			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from UM_CUSTOMERS where ID = LAST_INSERT_ID()");
			if (rs.next()) {
				customer.setCustomerCode(rs.getString("MAKH"));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("Error closing resultset: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("Error closing PreparedStatement: "
							+ e.getMessage());
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error closing connection: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		// TODO Auto-generated method stub
		return customer;
	}

	@Override
	public Response updateCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response deleteCustomer(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

}
