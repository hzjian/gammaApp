package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernelGeoFilter;

public interface TlGammaKernelGeoFilterRepository extends PagingAndSortingRepository<TlGammaKernelGeoFilter,String>{

	@Query("select a from TlGammaKernelGeoFilter a  where a.extGuid= ?1") 
	public List<TlGammaKernelGeoFilter> findByExtGuid(String extGuid);

	public void deleteByExtGuid(String extGuid);

}
