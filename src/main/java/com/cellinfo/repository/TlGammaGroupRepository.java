package com.cellinfo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaGroup;

public interface TlGammaGroupRepository extends PagingAndSortingRepository<TlGammaGroup, String>{

	Iterable<TlGammaGroup> findByGroupName(String groupName);

}
