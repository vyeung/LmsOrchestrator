package com.st.il.orchestratorapp.exception;

public class AlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public AlreadyExistsException(String message) {
		super(message);
	}	
	
	public AlreadyExistsException(String tableName, String objName) {
		super(tableName + " " + objName + " already exists in the database");
	}
}