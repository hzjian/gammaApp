package com.cellinfo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTaskUser;

public interface TlGammaTaskUserRepository  extends PagingAndSortingRepository<TlGammaTaskUser, Long>{

	void deleteByTaskGuidAndUserName(String taskGuid, String userName);

	void deleteByTaskGuid(String taskGuid);

}
