package com.cellinfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaLayerPolygon;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;

@Service
public class SysBusdataService {
 
	@Autowired
	private TlGammaLayerPolygonRepository tlGammaLayerPolygonRepository;
	
	public Page<TlGammaLayerPolygon> getAll(PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaLayerPolygonRepository.findAll(pageInfo);
	}
	
	
	
}
