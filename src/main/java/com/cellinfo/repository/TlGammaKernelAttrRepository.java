package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernelAttr;

public interface TlGammaKernelAttrRepository extends PagingAndSortingRepository<TlGammaKernelAttr,String>{

	List<TlGammaKernelAttr> findByKernelClassid(String kernelClassid);

	@Query("select a from TlGammaKernelAttr a  where a.kernelClassid = ?1 and a.attrName like %?2%") 
	Page<TlGammaKernelAttr> getKernelAttrList(String classId, String filterStr, Pageable pageInfo);

}
