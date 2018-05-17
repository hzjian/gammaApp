package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernelFilter;

public interface TlGammaKernelFilterRepository extends PagingAndSortingRepository<TlGammaKernelFilter,String>{

	List<TlGammaKernelFilter> findByExtGuid(String extGuid);
	
	int deleteByExtGuid(String extGuid);

}
