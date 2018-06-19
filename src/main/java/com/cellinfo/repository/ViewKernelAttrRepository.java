package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewKernelAttr;

public interface ViewKernelAttrRepository extends PagingAndSortingRepository<ViewKernelAttr,String>{

	List<ViewKernelAttr> findByUserName(String userName);

	List<ViewKernelAttr> findByKernelClassidAndUserName(String classId, String userName);

}
