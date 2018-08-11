package com.vishipel.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.vishipel.dao.NewsDAO;
import com.vishipel.model.News;
import com.vishipel.model.StatusMessage;
import com.vishipel.utils.Database;

public class NewsDAOImpl implements NewsDAO {

	private DataSource datasource = Database.getDataSource();
	private Logger logger = Logger.getLogger(NewsDAOImpl.class);
	
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	@Override
	public Response getAllNews() {
		// TODO Auto-generated method stub
		

		List<News> allNews = new ArrayList<News>();

		String sql = "select * from UM_DM_TINTUC";
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				News news = new News();
				news.setNewsId(rs.getInt("ID"));
				news.setTitle(rs.getString("TITLE"));
				news.setLink(rs.getString("LINK"));
				news.setType(rs.getString("LOAITIN"));
				news.setUpdatePerson(rs.getString("NGUOICN"));
				news.setUpdateAt(rs.getDate("NGAYCN"));

				allNews.add(news);
			}

			if (allNews.isEmpty()) {
				logger.error("No News existed...");
				StatusMessage<?> statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage("No News Existed...");
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
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		return Response.status(200).entity(allNews).build();
	}

	@Override
	public Response getNews(int p_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createNews(News p_news) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateNews(News p_news) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response deleteNews(int p_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getNewsByType(String type) {
		// TODO Auto-generated method stub
		List<News> listNews = new ArrayList<News>();
		String sql = "select * from UM_DM_TINTUC where LOAITIN = ?";
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, type);
			rs = ps.executeQuery();
			
			while(rs.next()){
				News news = new News();
				news.setNewsId(rs.getInt("ID"));
				news.setTitle(rs.getString("TITLE"));
				news.setLink(rs.getString("LINK"));
				news.setType(rs.getString("LOAITIN"));
				news.setUpdatePerson(rs.getString("NGUOICN"));
				news.setUpdateAt(rs.getDate("NGAYCN"));
				
				listNews.add(news);
			}
			if (listNews.isEmpty()) {
				logger.error("No News existed...");
				StatusMessage statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage("No News Existed...");
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
		return Response.status(200).entity(listNews).build();
	}

}
