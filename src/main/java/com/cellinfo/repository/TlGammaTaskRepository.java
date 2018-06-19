package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTask;

public interface TlGammaTaskRepository extends PagingAndSortingRepository<TlGammaTask, String>{

	List<TlGammaTask> findByTaskName(String taskName);
	
	Page<TlGammaTask> findByGroupGuid(String groupGuid,Pageable pageable);
	
	Page<TlGammaTask> findByUserName(String userName,Pageable pageable);

	@Query("select u from TlGammaTask u where u.taskGuid <> ?1 and u.groupGuid= ?2 and u.taskName = ?3")
	List<TlGammaTask> getByTaskNameExclude(String taskId,String groupId, String taskName);
}
