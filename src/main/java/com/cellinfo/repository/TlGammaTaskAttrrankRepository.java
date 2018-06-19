package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTaskAttrrank;

public interface TlGammaTaskAttrrankRepository extends PagingAndSortingRepository<TlGammaTaskAttrrank, String>{

	List<TlGammaTaskAttrrank> findByTaskGuid(String taskId);

	void deleteByTaskGuid(String taskGuid);

}
