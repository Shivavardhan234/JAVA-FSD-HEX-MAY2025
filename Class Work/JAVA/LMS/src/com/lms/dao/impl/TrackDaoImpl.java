package com.lms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.lms.dao.TrackDao;
import com.lms.model.Track;
import com.lms.utility.DBUtility;

public class TrackDaoImpl implements TrackDao{
	DBUtility db = new DBUtility();

	@Override
	public List<Track> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	@Override
	public void addTrack(Track t) {
		Connection conn= db.connect();
		int id=(int)(Math.random()* 10000000);
		String q ="INSERT INTO track(id, name) VALUES(?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(q);
			ps.setInt(1, id);
			ps.setString(2,t.getTrackname());
			ps.executeUpdate();
			
		} catch (SQLException e) {
		System.out.println(e.getMessage());
		}
		
		System.out.println("Track successfully inserted...");
		db.close();
		
		
	}

}
