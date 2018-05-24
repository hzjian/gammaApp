package com.cellinfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTask;

public interface TlGammaTaskRepository extends PagingAndSortingRepository<TlGammaTask, String>{

	Iterable<TlGammaTask> findByTaskName(String taskName);
	
	Page<TlGammaTask> findByGroupGuid(String groupGuid,Pageable pageable);
	
	Page<TlGammaTask> findByUserName(String userName,Pageable pageable);
	
	@Query("select u from TlGammaTask u where u.groupGuid = ?1 and u.userName = ?2 and u.taskName like %?3%")
	Page<TlGammaTask> getUserCreateTasks(String groupGuid,String userName,String strFilter,Pageable pageable);
}
