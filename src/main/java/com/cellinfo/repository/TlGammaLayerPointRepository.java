package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerPoint;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerPointRepository extends PagingAndSortingRepository<TlGammaLayerPoint, String>{
	
	@Query("select a from TlGammaLayerPoint a  where within(?1 , a.kernelGeom) = true and rownum < ?2") 
	public List<TlGammaLayerPoint> getDataByFilter(Geometry ptGeom,Integer maxNum);

}
