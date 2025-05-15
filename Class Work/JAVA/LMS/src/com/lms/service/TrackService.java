package com.lms.service;
import com.lms.dao.*;
import com.lms.dao.impl.TrackDaoImpl;
import com.lms.model.Track;
public class TrackService {
	TrackDao tDao = new TrackDaoImpl();
	
	public void addTrack(String name) {
		Track t = new Track();
		t.setTrackName(name);
		tDao.addTrack(t);
	}


}
