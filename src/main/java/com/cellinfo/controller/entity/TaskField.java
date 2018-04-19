package com.cellinfo.controller.entity;

import com.cellinfo.enums.FieldType;

public class TaskField {

	private String name;
	private String alias;
	private String type;
	private Integer length;
	private Integer precision = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	/**
	 * @return the precision
	 */
	public Integer getPrecision() {
		return precision;
	}

	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	@Override
	public String toString() {
		String tmpStr = "";
		if(this.getType().equalsIgnoreCase(FieldType.INTEGER.getType()))
			tmpStr = this.getName() + "  integer";
		if(this.getType().equalsIgnoreCase(FieldType.NUMBER.getType()))
			tmpStr = this.getName() + "  numeric (" + this.getLength()+"," +(this.precision<1?0:this.precision)+")";
		if(this.getType().equalsIgnoreCase(FieldType.DATETIME.getType()))
			tmpStr = this.getName() + "  timestamp";
		if(this.getType().equalsIgnoreCase(FieldType.STRING.getType()))
			tmpStr = String.format(this.getName() + "  character varying(%d)",this.length);
		return tmpStr;
	}

	//ALTER COLUMN test4 TYPE numeric (12, 5);
	public String getAlterString() {
		String tmpStr = "";
		if(this.getType().equalsIgnoreCase(FieldType.INTEGER.getType()))
			tmpStr = this.getName() + " type integer";
		if(this.getType().equalsIgnoreCase(FieldType.NUMBER.getType()))
			tmpStr = this.getName() + " type numeric (" + this.getLength()+"," +(this.precision<1?0:this.precision)+")";
		if(this.getType().equalsIgnoreCase(FieldType.DATETIME.getType()))
			tmpStr = this.getName() + " type timestamp";
		if(this.getType().equalsIgnoreCase(FieldType.STRING.getType()))
			tmpStr = String.format(this.getName() + " type character varying(%d)",this.length);
		return tmpStr;
	}
}
