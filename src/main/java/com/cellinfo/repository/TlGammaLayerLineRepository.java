package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerLine;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerLineRepository  extends PagingAndSortingRepository<TlGammaLayerLine,String>{

	@Query("select a from TlGammaLayerLine a  where within( a.kernelGeom , ?1) = true") 
	public List<TlGammaLayerLine> getDataByFilter(Geometry ptGeom,Pageable pageable);

}
