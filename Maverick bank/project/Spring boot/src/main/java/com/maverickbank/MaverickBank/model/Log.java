package com.maverickbank.MaverickBank.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.maverickbank.MaverickBank.enums.LogType;
import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="log")
public class Log {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(nullable=false)
	private User user;
	@Enumerated(EnumType.STRING)
	@Column(name="log_type",nullable=false)
	private LogType logType;
	@Column(name="log_date",nullable=false)
	private LocalDate logDate;
	@Column(name="log_time",nullable=false)
	private LocalTime logTime;
	
	
	
	public Log() {}



	public Log(int id, User user, LogType logType, LocalDate logDate, LocalTime logTime) throws InvalidInputException {
		this.setId(id);
		this.setUser(user);
		this.setLogType(logType);
		this.setLogDate(logDate);
		this.setLogTime(logTime);
	}


// ------------------------------------------ Getters & Setters --------------------------------------------------
	public int getId() {return id;}
	public User getUser() {return user;}
	public LogType getLogType() {return logType;}
	public LocalDate getLogDate() {return logDate;}
	public LocalTime getLogTime() {return logTime;}



	public void setId(int id) throws InvalidInputException{
		if( id<=0) {
			throw new InvalidInputException("Log ID is Invalid. Please enter appropriate Log ID...!!!");
		}
		this.id = id;
	}



	public void setUser(User user) throws InvalidInputException{
		if (user==null) {
			throw new InvalidInputException("Provided user object is null. Please provide appropriate user object...!!!");
		}
		this.user = user;
	}



	public void setLogType(LogType logType) throws InvalidInputException{
		if (logType==null) {
			throw new InvalidInputException("Provided log type is null. Please provide appropriate log type...!!!");
		}
		this.logType = logType;
	}



	public void setLogDate(LocalDate logDate) throws InvalidInputException{
		if (logDate==null) {
			throw new InvalidInputException("Provided log date is null. Please provide appropriate log type...!!!");
		}
		this.logDate = logDate;
	}



	public void setLogTime(LocalTime logTime) throws InvalidInputException{
		if (logTime==null) {
			throw new InvalidInputException("Provided log time is null. Please provide appropriate log time...!!!");
		}
		this.logTime = logTime;
	}
	
	


}
