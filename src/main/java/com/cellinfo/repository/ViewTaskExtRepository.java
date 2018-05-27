package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskExt;

public interface ViewTaskExtRepository extends PagingAndSortingRepository<ViewTaskExt, String>  {

	public List<ViewTaskExt> findByTaskGuid(String taskGuid);

}
