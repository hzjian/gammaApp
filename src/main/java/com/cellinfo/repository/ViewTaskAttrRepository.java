package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskAttrPK;

public interface ViewTaskAttrRepository extends PagingAndSortingRepository<ViewTaskAttr, ViewTaskAttrPK> {

	@Query("select a from ViewTaskAttr a  where a.id.taskGuid = ?1") 
	public List<ViewTaskAttr> getByTaskGuid(String taskGuid);
	
}
