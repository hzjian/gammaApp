package com.cellinfo.controller.entity;

public class FieldParameter {

	
	private String key;
	private String fieldname;
	/**
	 * STRING
	 * INTEGER
	 * NUMBER
	 * DATETIME
	 */
	private String fieldtype;
	private String isedit;
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the fieldname
	 */
	public String getFieldname() {
		return fieldname;
	}
	/**
	 * @param fieldname the fieldname to set
	 */
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	/**
	 * @return the fieldtype
	 */
	public String getFieldtype() {
		return fieldtype;
	}
	/**
	 * @param fieldtype the fieldtype to set
	 */
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
	/**
	 * @return the isedit
	 */
	public String getIsedit() {
		return isedit;
	}
	/**
	 * @param isedit the isedit to set
	 */
	public void setIsedit(String isedit) {
		this.isedit = isedit;
	}
}
