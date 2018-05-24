package com.cellinfo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTaskAttr;

public interface TlGammaTaskAttrRepository extends PagingAndSortingRepository<TlGammaTaskAttr, Long>{

	@Transactional
	@Modifying
	@Query(value = "delete from TlGammaTaskAttr a where a.taskGuid = ?1 and a.attrGuid =?2")
	void deleteTaskAttr(String taskGuid, String fieldGuid);

	long countByAttrGuid(String attrId);

}
