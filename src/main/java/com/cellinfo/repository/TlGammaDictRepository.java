package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaDict;

public interface TlGammaDictRepository extends PagingAndSortingRepository<TlGammaDict, String>{

	Page<TlGammaDict> findByGroupGuid(String groupGuid, Pageable pageable);

	@Query("select a from TlGammaDict a  where a.dictName =?1 and a.groupGuid = ?2") 
	List<TlGammaDict> getDictByName(String dictName, String groupGuid);

}
