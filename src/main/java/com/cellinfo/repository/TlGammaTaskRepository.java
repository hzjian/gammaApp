package com.cellinfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTask;

public interface TlGammaTaskRepository extends PagingAndSortingRepository<TlGammaTask, String>{

	Iterable<TlGammaTask> findByTaskName(String taskName);
	
	Page<TlGammaTask> findByGroupGuid(String groupGuid,Pageable pageable);
	
	Page<TlGammaTask> findByUserName(String groupGuid,Pageable pageable);
}
