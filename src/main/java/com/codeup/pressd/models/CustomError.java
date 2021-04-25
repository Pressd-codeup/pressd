package com.codeup.pressd.models;

public class CustomError {
	private String ErrorCode;
	private String ErrorMessage;
	private String ErrorExeption;

	public String getErrorCode() {
		return ErrorCode;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public String getErrorExeption() {
		return ErrorExeption;
	}

	public void setErrorCode(String ErrorCode) {
		this.ErrorCode = ErrorCode;
	}

	public void setErrorMessage(String ErrorMessage) {
		this.ErrorMessage = ErrorMessage;
	}

	public void setErrorExeption(String ErrorExeption) {
		this.ErrorExeption = ErrorExeption;
	}

}
