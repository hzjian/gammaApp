package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cellinfo.entity.TlGammaDictItem;

public interface TlGammaDictItemRepository extends CrudRepository<TlGammaDictItem, Long>{

	@Query("select a from TlGammaDictItem a  where a.dictId= ?1 order by a.updateTime desc") 
	public  List<TlGammaDictItem> findByDictId(String dictId);
	
	public int deleteByDictId(String dictId);

	public List<TlGammaDictItem> findByDictIdAndDictItem(String dictId, String dictItem);
}
