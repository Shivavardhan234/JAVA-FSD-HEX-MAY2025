package com.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms.dao.impl.UserDaoImpl;
import com.lms.exception.InvalidIdException;
import com.lms.exception.InvalidInputException;
import com.lms.model.Learner;
import com.lms.utility.*;

public class LearnerDaoImpl implements LearnerDao{
	private LearnerUtility lu=new LearnerUtility();
	private DBUtility db=DBUtility.getInstance();
	private UserDao ud= new UserDaoImpl();
	@Override
	public void addLearner(Learner l) throws InvalidInputException {
		Connection conn = db.connect();
		
		int id=(int)(Math.random()* 10000000);
		String q="INSERT INTO LEARNER (id,name,email,user_id) VALUES (?,?,?,?) ";
		try {
			PreparedStatement ps = conn.prepareStatement(q);
			
			ps.setInt(1, id);
			ps.setString(2, l.getName());
			ps.setString(3, l.getEmail());
			int userId=ud.addUser(l.getUser());
			System.out.println("User id : "+ userId );
			ps.setInt(4, userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		db.close();
		
	}

	@Override
	public List<Learner> getAllLearners() {
		Connection conn= db.connect();
		
		String q= "SELECT * FROM learner";
		ArrayList<Learner> l = new ArrayList<>();
		
		try {
			PreparedStatement ps =conn.prepareStatement(q);
			
			ResultSet rs= ps.executeQuery(q);
			while (rs.next()==true) {
				Learner lr= new Learner(rs.getInt("id"),rs.getString("name"),rs.getString("email"));
				l.add(lr);		
				}//while
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}//catch
		db.close();
		return l;
	}

	@Override
	public Learner getById(int id) throws InvalidIdException {
		
		Connection conn = db.connect();
		Learner lnr = new Learner();
		String q= "SELECT * FROM learner WHERE id= ? ";
		try {
			PreparedStatement ps = conn.prepareStatement(q);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
			lnr.setId(rs.getInt("id"));	
			lnr.setName(rs.getString("name"));
			lnr.setEmail(rs.getString("email"));
			}
			else {
				throw new InvalidIdException("Invalid Id...");
			}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		db.close();
		return lnr;
	}

	@Override
	public Learner updateLearner(int id, Learner l) throws InvalidInputException, InvalidIdException {
		this.deleteLearner(id);
		List<Learner> lis= getAllLearners();
		List<Learner> list= new ArrayList<>(lis);
		list.add(l);
		LearnerUtility.setList(list);
		return null;
	}

	@Override
	public void deleteLearner(int id) throws InvalidIdException {
		List<Learner> list = lu.getSampleData();
		List<Learner> lis =list.stream().filter(l->l.getId() !=id).toList();
		
		if(lis.size()==list.size())
			throw new InvalidIdException("No records found with given id...");
		LearnerUtility.setList(lis);
		
	}
	
	//---------------------------------just ala testing----------------------------------------------------------------
//	public static void main(String[] args) throws InvalidIdException{
//		LearnerDaoImpl dao = new LearnerDaoImpl();
//		
//		dao.getAllLearners().stream().forEach(l-> System.out.println(l));
//		
//		try {
//		System.out.println("enter id");
//		int id=7;
//		System.out.println(dao.getById(id));
//			
//		}
//		catch(InvalidIdException e) {
//			System.err.println(e.getMessage());
//		}
//		
//	}

	
}
