package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerLine;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerLineRepository  extends PagingAndSortingRepository<TlGammaLayerLine,String>{

	@Query("select a from TlGammaLayerLine a  where within(?1 , a.kernelGeom) = true and rownum < ?2") 
	public List<TlGammaLayerLine> getDataByFilter(Geometry ptGeom,Integer maxNum);

}
