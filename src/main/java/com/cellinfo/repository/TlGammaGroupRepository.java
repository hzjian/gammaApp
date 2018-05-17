package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaGroup;

public interface TlGammaGroupRepository extends PagingAndSortingRepository<TlGammaGroup, String>{

	List<TlGammaGroup> findByGroupName(String groupName);

	@Query("select a from TlGammaGroup a  where a.groupName like %?1%") 
	Page<TlGammaGroup> filterByGroupNameLike(String groupName, Pageable pageInfo);

}
