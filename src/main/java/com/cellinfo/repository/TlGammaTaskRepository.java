package com.cellinfo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTask;

public interface TlGammaTaskRepository extends PagingAndSortingRepository<TlGammaTask, String>{

	Iterable<TlGammaTask> findByTaskName(String taskName);
}
