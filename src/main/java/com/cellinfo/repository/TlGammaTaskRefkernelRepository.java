package com.cellinfo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTaskRefkernel;

public interface TlGammaTaskRefkernelRepository extends PagingAndSortingRepository<TlGammaTaskRefkernel, Long>{

	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_refkernel (id,kernel_guid,task_guid)(select nextval('seq_task_refkernel'),kernel_guid,?1 from Tl_Gamma_task_tmp a  where a.tmp_guid = ?2 and f_num = ?3)",nativeQuery = true) 
	public int createTaskFilter(String taskGuid, String tmpGuid, Integer filterNum);

}
