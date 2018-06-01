package com.cellinfo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernelSubset;

public interface TlGammaKernelSubsetRepository extends PagingAndSortingRepository<TlGammaKernelSubset, Long>{

	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_kernel_subset (id,kernel_guid,ext_guid)(select nextval('seq_kernel_subset'),kernel_guid,?1 from Tl_Gamma_task_tmp a  where a.tmp_guid = ?2 and f_num = ?3)",nativeQuery = true) 
	public int createExtFilter(String extGuid,String tmpGuid,int fnum);


	@Transactional
	@Modifying
	@Query(value = "delete from tl_gamma_kernel_subset where ext_guid = ?1",nativeQuery = true) 
	public void removeExtGuid(String extGuid);
	
}
