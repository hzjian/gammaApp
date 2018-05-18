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
	
}