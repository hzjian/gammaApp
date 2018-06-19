package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskAttrPK;

public interface ViewTaskAttrRepository extends PagingAndSortingRepository<ViewTaskAttr, ViewTaskAttrPK> {

	@Query("select a from ViewTaskAttr a  where a.id.taskGuid = ?1 and a.layerGrade = 1") 
	public List<ViewTaskAttr> getByTaskGuid(String taskGuid);

	@Query("select a from ViewTaskAttr a  where a.id.taskGuid = ?1 and a.layerGrade = 1") 
	public Page<ViewTaskAttr> getByTaskGuid(String taskGuid, Pageable pageInfo);
	
	@Query("select a from ViewTaskAttr a  where a.id.taskGuid = ?1 and a.kernelClassid =?2 and a.layerGrade = 0") 
	public Page<ViewTaskAttr> getByTaskGuid(String taskGuid, String classId, Pageable pageInfo);
	
	
}
