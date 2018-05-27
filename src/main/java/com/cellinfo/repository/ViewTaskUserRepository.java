package com.cellinfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskUser;
import com.cellinfo.entity.ViewTaskUserPK;

public interface ViewTaskUserRepository  extends PagingAndSortingRepository<ViewTaskUser, ViewTaskUserPK> {

	@Query("select a from ViewTaskUser a  where a.id.taskGuid = ?1") 
	public Page<ViewTaskUser> getByTaskGuid(String taskGuid,Pageable pageable);
	
	@Query("select a from ViewTaskUser a  where a.id.userName = ?1") 
	public Page<ViewTaskUser> getByUserName(String userName,Pageable pageable);

	@Query("select a from ViewTaskUser a  where a.id.userName = ?1 and a.taskName like %?2%") 
	public Page<ViewTaskUser> getUserTasks( String userName, String filterStr, Pageable pageInfo);

	@Query("select a from ViewTaskUser a  where a.id.userName = ?1 and a.taskName like %?2% and a.businessPassword is not null") 
	public Page<ViewTaskUser> getPasswordTasks(String userName, String filterStr, Pageable pageInfo);
	
}