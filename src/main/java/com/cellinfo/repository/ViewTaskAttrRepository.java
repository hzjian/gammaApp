package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskAttrPK;

public interface ViewTaskAttrRepository extends PagingAndSortingRepository<ViewTaskAttr, ViewTaskAttrPK> {

	@Query("select a from ViewTaskAttr a  where a.id.taskGuid = ?1") 
	public List<ViewTaskAttr> getByTaskGuid(String taskGuid);

	@Query("select a from ViewTaskAttr a  where a.id.taskGuid = ?1") 
	public Page<ViewTaskAttr> getByTaskGuid(String taskGuid, Pageable pageInfo);
	
}
