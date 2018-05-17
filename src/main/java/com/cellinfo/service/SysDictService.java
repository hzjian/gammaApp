package com.cellinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaDict;
import com.cellinfo.entity.TlGammaDictItem;
import com.cellinfo.repository.TlGammaDictItemRepository;
import com.cellinfo.repository.TlGammaDictRepository;

@Service
public class SysDictService {

	@Autowired
	private TlGammaDictRepository tlGammaDictRepository;
	
	@Autowired
	private TlGammaDictItemRepository tlGammaDictItemRepository;

	public Page<TlGammaDict> getDictsByGroupGuid(String groupGuid, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.tlGammaDictRepository.findByGroupGuid(groupGuid,pageable);
	}

	public TlGammaDict save(TlGammaDict tmpDict) {
		// TODO Auto-generated method stub
		return this.tlGammaDictRepository.save(tmpDict);
	}

	public List<TlGammaDict> getDictbyName(String dictName, String groupGuid) {
		// TODO Auto-generated method stub
		return tlGammaDictRepository.getDictByName(dictName,groupGuid);
	}

	public void deleteDict(String dictId) {
		this.tlGammaDictRepository.delete(dictId);
		this.tlGammaDictItemRepository.deleteByDictId(dictId);
		
	}

	public TlGammaDictItem addDictItem(String dictId, String dictItem) {
		// TODO Auto-generated method stub
		TlGammaDictItem tlGammaDictItem =new TlGammaDictItem();
		tlGammaDictItem.setDictId(dictId);
		tlGammaDictItem.setDictItem(dictItem);
		return this.tlGammaDictItemRepository.save(tlGammaDictItem);
		
	}

	public void deleteItem(Long itemId) {
		// TODO Auto-generated method stub
		this.tlGammaDictItemRepository.delete(itemId);
	}

	public List<TlGammaDictItem> getDictItems(String dictId) {
		// TODO Auto-generated method stub
		return this.tlGammaDictItemRepository.findByDictId(dictId);
	}
}
