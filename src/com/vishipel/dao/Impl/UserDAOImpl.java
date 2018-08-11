package com.vishipel.dao.Impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.vishipel.dao.CustomerDAO;
import com.vishipel.dao.UsersDAO;
import com.vishipel.model.Customer;
import com.vishipel.model.StatusMessage;
import com.vishipel.model.Users;
import com.vishipel.utils.Database;
import com.vishipel.utils.Utils;

public class UserDAOImpl implements UsersDAO {

	private DataSource datasource = Database.getDataSource();
	private Logger logger = Logger.getLogger(UserDAOImpl.class);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private Statement stmt = null;

	private StatusMessage<?> statusMessage = null;

	@Override
	public Users validate(String email, String password) {
		// TODO Auto-generated method stub
		logger.info("inside validate user...");
		Users users = null;

		String sql = String.format(
				"select * from UM_USERS where email = '%s' ", email);
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				users = new Users();
				users.setEmail(rs.getString("EMAIL"));
				users.setPassword(rs.getString("PWD"));
				users.setIsActive(rs.getString("HOATDONG"));
				users.setOTP(rs.getString("OTP"));
				users.setOTPExpiredTime(rs.getDate("OTPTM"));
				users.setCustomerId(rs.getString("MAKH"));
				users.setCreateAt(rs.getDate("NGAYTAO"));
				users.setCreatePerson(rs.getString("NGUOITAO"));
				users.setUpdatePerson(rs.getString("NGUOICN"));
				users.setUpdateAt(rs.getDate("NGAYCN"));
			}
			logger.info("user queried..." + users);
			if (users != null) {
				if (password != null) {
					try {
						String hashPass = Utils.ComputeHash(password)
								.toUpperCase();
						if (hashPass.equals(users.getPassword())) {
							return users;
						}
					} catch (NoSuchAlgorithmException
							| UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						users = null;
					}
				}
				return users;

			}

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			users = null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		return users;

		// return users;
	}

	@Override
	public Response createUser(Users users) {
		// TODO Auto-generated method stub
		// create customer to get customerId
		CustomerDAO customerDAO = new CustomerDAOImpl();
		Users userInserted = new Users();

		Customer customer = new Customer();
		customer.setEmail(users.getEmail());
		customer.setCustomerName("Nguyen Van A");
		customer.setGlobal("1");
		customer.setMST("8574837273GD");
		customer.setPhoneNumber("0986742911");
		customer.setAddress("144 - Mai Dich Street - Mai Dich Ward - Cau Giay District - Ha Noi");
		customer.setFax("88873");
		customer.setCreatePerson("Nguyen Van B");
		customer.setUpdatePerson("Nguyen Van B");

		Customer customer_created = customerDAO.createCustomer(customer);
		logger.info("customer created : " + customer_created.getCustomerId());

		String sql = "insert into UM_USERS (EMAIL,PWD,HOATDONG,OTP,OTPTM,MAKH,NGUOITAO,NGUOICN) values (?,?,?,?,?,?,?,?)";

		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, users.getEmail());
			ps.setString(2, Utils.ComputeHash(users.getPassword()).toUpperCase());
			ps.setString(3, users.getIsActive());
			ps.setString(4, Utils.getUniqueOTP());
			ps.setDate(5, new Date(Utils.getExpirationOTPTime().getTime()));
			ps.setString(6, customer_created.getCustomerCode());
			ps.setString(7, "Nguyen Van B");
			ps.setString(8, "Nguyen Van B");

			int rows = ps.executeUpdate();

			if (rows == 0) {
				logger.error("Unable to create User...");
				statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage("Unable to create user...");
				return Response.status(404).entity(statusMessage).build();

			}
			
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from UM_USERS where ID = LAST_INSERT_ID()");
			if(rs.next()){
				userInserted.setEmail(rs.getString("EMAIL"));
				userInserted.setIsActive(rs.getString("HOATDONG"));
				userInserted.setCustomerId(rs.getString("MAKH"));
				userInserted.setCreatePerson(rs.getString("NGUOITAO"));
				userInserted.setCreateAt(rs.getDate("NGAYTAO"));
				userInserted.setUpdatePerson(rs.getString("NGUOICN"));
				userInserted.setUpdateAt(rs.getDate("NGAYCN"));
				userInserted.setId(rs.getInt("ID"));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
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

		return Response.status(200).type(MediaType.APPLICATION_JSON).entity(userInserted).build();
	}
	
	@Override
	public Response changePassword(Users user, String new_password) {
		// TODO Auto-generated method stub
		String sql = "update UM_USERS set PWD = ? where EMAIL = ?";
		
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, Utils.ComputeHash(new_password).toUpperCase());
			ps.setString(2, user.getEmail());
			
			int rows = ps.executeUpdate();

			if (rows == 0) {
				logger.error("Unable to update user password...");
				statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_MODIFIED.getStatusCode());
				statusMessage.setMessage("Unable to create user...");
				return Response.status(Status.NOT_MODIFIED.getStatusCode()).entity(statusMessage).build();
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		statusMessage = new StatusMessage();
		statusMessage.setStatus(Status.OK.getStatusCode());
		statusMessage.setMessage("Updated password successfully...");
		return Response.status(Status.OK.getStatusCode()).entity(statusMessage).build();
	}
	
	@Override
	public boolean isExist(String email) {
		// TODO Auto-generated method stub
		String sql = "select * from UM_USERS where email = ?";
		
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

}
