package com.cellinfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewKernel;

public interface ViewKernelRepository  extends PagingAndSortingRepository<ViewKernel, String>{

	@Query("select a from ViewKernel a  where a.groupGuid = ?1 and a.kernelClassname like %?2%") 
	public Page<ViewKernel> findByGroupGuid(String groupGuid,String key, Pageable page);
}
