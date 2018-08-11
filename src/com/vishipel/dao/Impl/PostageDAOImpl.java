package com.vishipel.dao.Impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.vishipel.dao.PostageDAO;
import com.vishipel.model.Postage;
import com.vishipel.model.StatusMessage;
import com.vishipel.utils.Database;

public class PostageDAOImpl implements PostageDAO {

	private DataSource datasource = Database.getDataSource();
	private Logger logger = Logger.getLogger(PostageDAOImpl.class);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private StatusMessage<?> statusMessage = null;
	
	private Statement stmt = null;

	@Override
	public Response getAllPostrage() {
		// TODO Auto-generated method stub
		List<Postage> allPostage = new ArrayList<Postage>();
		String sql = "select * from UM_TC_CUOCINM";
		try { 
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				Postage postage = new Postage();
				postage.setId(rs.getInt("ID"));
				postage.setCustomerCode(rs.getString("MAKH"));
				postage.setMonth(rs.getDate("THANG"));
				postage.setPostageType(rs.getString("LOAICUOC"));
				postage.setFiles(rs.getBlob("FILES"));
				postage.setPublishDay(rs.getDate("NGAYPH"));
				postage.setIsAuto(rs.getString("TUDONG"));
				postage.setUpdatePerson(rs.getString("NGUOICN"));
				postage.setUpdateAt(rs.getDate("NGAYCN"));

				allPostage.add(postage);
			}

			if (allPostage.isEmpty()) {
				logger.error("No Postage existed...");
				statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage("No Postage Existed...");
				return Response.status(404).entity(statusMessage).build();
			}

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
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
		return Response.status(200).entity(allPostage).build();
	}

	@Override
	public Response getPostage(int p_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getPostageByDate(Date p_date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createPostage(Postage p_postrage, InputStream is) {
		// TODO Auto-generated method stub
		String sql = "insert into UM_TC_CUOCINM (MAKH,THANG,LOAICUOC,FILES,NGAYPH,TUDONG,NGUOICN) values(?,?,?,?,?,?,?)";
		//Postage postageInserted = new Postage();
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			logger.info("conn : " + conn + " ps : " + ps + " postage : " + p_postrage);
			ps.setString(1, p_postrage.getCustomerCode());
			ps.setDate(2, p_postrage.getMonth());
			ps.setString(3, p_postrage.getPostageType());
			ps.setBlob(4, is);
			ps.setDate(5, p_postrage.getPublishDay());
			ps.setString(6, p_postrage.getIsAuto());
			ps.setString(7, p_postrage.getUpdatePerson());
			
			int rows = ps.executeUpdate();
			
			if(rows == 0){
				logger.error("Unable to create Postage...");
				statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage("Unable to create Postage...");
				return Response.status(404).entity(statusMessage).build();
			}
			
			/*stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from UM_TC_CUOCINM where ID = LAST_INSERT_ID()");
			if(rs.next()){
				postageInserted.setCustomerCode(rs.getString("MAKH"));
				postageInserted.setMonth(rs.getDate("THANG"));
				postageInserted.setPostageType(rs.getString("LOAICUOC"));
				postageInserted.setFiles(rs.getBlob("FILES"));
				postageInserted.setPublishDay(rs.getDate("NGAYPH"));
				postageInserted.setIsAuto(rs.getString("TUDONG"));
				postageInserted.setUpdatePerson(rs.getString("NGUOICN"));
				postageInserted.setUpdateAt(rs.getDate("NGAYCN"));
			}*/
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
		statusMessage = new StatusMessage();
		statusMessage.setStatus(Status.OK.getStatusCode());
		statusMessage.setMessage("Postage created...");
		return Response.status(200).entity(statusMessage).build();
	}

	@Override
	public Response updatePostage(Postage p_postrage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response deletePostage(int p_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getPostageByFilters(String customerCode, String type, String month, String year) {
		// TODO Auto-generated method stub	
		//List<Postage> postages = new ArrayList<Postage>();
		Blob blob = null;
		byte[] bytes = null;
		try {
			
			String sql = "select FILES from UM_TC_CUOCINM where MAKH = ? and LOAICUOC = ? and year(THANG) = ? and month(THANG) = ?";
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, customerCode);
			ps.setString(2, type);
			ps.setString(3, year);
			ps.setString(4, month);
			
			logger.info("sql : " + ps.toString());
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				blob = rs.getBlob("FILES");
				/*InputStream is = blob.getBinaryStream();
				FileOutputStream fos = new FileOutputStream("C:\\DownloadedFiles"+ "\\" + "test.pdf");
				int b = 0;
				while ((b = is.read()) != -1)
				{
				    fos.write(b); 
				}*/
				int blobLength = (int) blob.length();
				int pos = 1; // position is 1-based
				bytes = blob.getBytes(pos,(int)blob.length());

				/*Postage postage = new Postage();
				postage.setId(rs.getInt("ID"));
				postage.setCustomerCode(rs.getString("MAKH"));
				postage.setMonth(rs.getDate("THANG"));
				postage.setPostageType(rs.getString("LOAICUOC"));
				postage.setFiles(rs.getBlob("FILES"));
				postage.setPublishDay(rs.getDate("NGAYPH"));
				postage.setIsAuto(rs.getString("TUDONG"));
				postage.setUpdatePerson(rs.getString("NGUOICN"));
				postage.setUpdateAt(rs.getDate("NGAYCN"));
				
				postages.add(postage);*/
			} else{
				logger.info("query is empty");
			}
			
			/*if(postages.isEmpty()){
				logger.error("No Postage existed...");
				StatusMessage statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage("No Postage Existed...");
				return Response.status(404).entity(statusMessage).build();
			}*/
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
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
		
		return Response.ok().entity(bytes).type(MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "filename=postage.pdf").build();
	}

}
