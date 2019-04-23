package com.st.orchestrator.exception;

public class NotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
		super(message);
	}
	
	public NotFoundException(String action, String tableName, int objId) {
		super(action + " failed. The " + tableName + " with id=" + objId + " not found");
	}
}