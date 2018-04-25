package com.cellinfo.security;

public class UserInfo {
	
	private String userName="";
	private String groupGuid="";
	
	public UserInfo(String name,String groupid)
	{
		this.userName = name;
		this.groupGuid =groupid;
		
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the groupGuid
	 */
	public String getGroupGuid() {
		return groupGuid;
	}

	/**
	 * @param groupGuid the groupGuid to set
	 */
	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}

	
}
