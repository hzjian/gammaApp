package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernelExt;

public interface TlGammaKernelExtRepository extends PagingAndSortingRepository<TlGammaKernelExt,String>{

	Page<TlGammaKernelExt> findByKernelClassidAndUserName(String kernelClassid, String userName,Pageable pageable);
	
	@Query("select u from TlGammaKernelExt u where u.extName like %?1% and u.userName = ?2")
	Page<TlGammaKernelExt> filterByExtName(String filterStr, String userName,Pageable pageable);
	

	@Query("select u from TlGammaKernelExt u where u.kernelClassid = ?1 and (u.userName = ?2 or u.extGrade = 'GROUP')")
	List<TlGammaKernelExt> findByKernelClassidAndUserName(String kernelClassid, String userName);

	@Query("select u from TlGammaKernelExt u where u.extName = ?1 and u.userName = ?2")
	List<TlGammaKernelExt> getKernelExtByName(String extName, String userName);

	Page<TlGammaKernelExt> findByUserName(String userName, Pageable pageable);

}
