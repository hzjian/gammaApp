package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernelAttr;

public interface TlGammaKernelAttrRepository extends PagingAndSortingRepository<TlGammaKernelAttr,String>{

	List<TlGammaKernelAttr> findByKernelClassid(String kernelClassid);
	
	

}
