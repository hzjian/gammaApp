package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernelGeoFilter;

public interface TlGammaKernelGeoFilterRepository extends PagingAndSortingRepository<TlGammaKernelGeoFilter,String>{

	List<TlGammaKernelGeoFilter> findByExtGuid(String extGuid);

}
