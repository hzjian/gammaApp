package com.cellinfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaLayerLine;
import com.cellinfo.entity.TlGammaLayerPoint;
import com.cellinfo.entity.TlGammaLayerPolygon;
import com.cellinfo.repository.TlGammaLayerLineRepository;
import com.cellinfo.repository.TlGammaLayerPointRepository;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;

@Service
public class SysBusdataService {
 
	@Autowired
	private TlGammaLayerPointRepository tlGammaLayerPointRepository;
	
	@Autowired
	private TlGammaLayerLineRepository tlGammaLayerLineRepository;
	
	@Autowired
	private TlGammaLayerPolygonRepository tlGammaLayerPolygonRepository;
	
	public Page<TlGammaLayerPolygon> getAll(PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaLayerPolygonRepository.findAll(pageInfo);
	}
	
	public void savePontGeom(TlGammaLayerPoint entity)
	{
		this.tlGammaLayerPointRepository.save(entity);
		
	}
	public void saveLineGeom(TlGammaLayerLine entity)
	{
		this.tlGammaLayerLineRepository.save(entity);
		
	}
	public void savePolygonGeom(TlGammaLayerPolygon entity)
	{
		this.tlGammaLayerPolygonRepository.save(entity);
		
	}
	
}
