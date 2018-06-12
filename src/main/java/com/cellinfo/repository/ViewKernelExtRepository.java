package com.cellinfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewKernelExt;

public interface ViewKernelExtRepository extends PagingAndSortingRepository<ViewKernelExt,String>{

	@Query("select u from ViewKernelExt u where u.extName = ?1 and u.userName = ?2")
	Page<ViewKernelExt> filterByExtName(String filterStr, String userName, Pageable pageable);

	Page<ViewKernelExt> findByUserName(String userName, Pageable pageable);

}
