package com.cellinfo.controller.entity;

public class RequestParameter implements GammaParameter{

	private Integer page;
	private Integer pageSize;
	//ASC, DESC
	private String sortDirection;
	private String sortField;
	
	private String skey;
	
	private Integer ikey;
	
	private String groupGuid;
	
	/**
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(Integer page) {
		this.page = page;
	}
	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the sortDirection
	 */
	public String getSortDirection() {
		return sortDirection;
	}
	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	/**
	 * @return the sortField
	 */
	public String getSortField() {
		return sortField;
	}
	/**
	 * @param sortField the sortField to set
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	/**
	 * @return the skey
	 */
	public String getSkey() {
		return skey;
	}
	/**
	 * @param skey the skey to set
	 */
	public void setSkey(String skey) {
		this.skey = skey;
	}
	/**
	 * @return the ikey
	 */
	public Integer getIkey() {
		return ikey;
	}
	/**
	 * @param ikey the ikey to set
	 */
	public void setIkey(Integer ikey) {
		this.ikey = ikey;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "page:"+this.page+"skey "+this.skey;
	}
	
	
	
}
