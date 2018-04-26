package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cellinfo.entity.TlGammaDictItem;

public interface TlGammaDictItemRepository extends CrudRepository<TlGammaDictItem, Long>{

	public  List<TlGammaDictItem> findByDictId(String dictId);
}
