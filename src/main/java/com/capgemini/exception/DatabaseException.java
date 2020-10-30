package com.capgemini.exception;

public class DatabaseException extends Exception {
	
	ExceptionType type;
	
	public enum ExceptionType{
		UNABLE_TO_CONNECT, UNABLE_TO_EXECUTE_QUERY;
	}
	
	public DatabaseException(String msg, ExceptionType type) {
		super(msg);
		this.type = type;
	}

}
