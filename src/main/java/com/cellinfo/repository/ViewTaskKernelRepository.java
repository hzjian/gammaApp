package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskKernel;
import com.cellinfo.entity.ViewTaskKernelPK;

public interface ViewTaskKernelRepository extends PagingAndSortingRepository<ViewTaskKernel, ViewTaskKernelPK> {

	
	@Query("select a from ViewTaskKernel a  where a.id.taskGuid = ?1") 
	public List<ViewTaskKernel> getByTaskGuid(String taskGuid);

}
