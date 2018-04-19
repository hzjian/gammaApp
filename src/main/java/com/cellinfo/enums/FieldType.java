package com.cellinfo.enums;

public enum FieldType {
	INTEGER("integer"),
	NUMBER("number"),
	STRING("string"),
	DATETIME("datetime");
	
	private final String type;
	
	FieldType(String type) {
		this.type = type; 
    }
	public String getType()
	{
		return this.type;
	}
}
