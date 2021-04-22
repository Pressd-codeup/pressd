package com.codeup.pressd.models;

public class Filter {
	private String params;
	private String type_name;

	public String getParams() {
		return params;
	}
	public String getType_name(){
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
