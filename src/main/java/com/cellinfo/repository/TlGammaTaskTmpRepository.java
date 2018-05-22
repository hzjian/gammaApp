package com.cellinfo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTaskTmp;
import com.cellinfo.entity.TlGammaTaskTmpPK;

public interface TlGammaTaskTmpRepository extends PagingAndSortingRepository<TlGammaTaskTmp, TlGammaTaskTmpPK>{
	
	@Transactional
	@Modifying
	@Query(value = "delete from TlGammaTaskTmp a where a.id.tmpGuid = ?1")
	public int deleteTaskTmp(String tmpGuid);
	
}
