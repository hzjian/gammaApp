package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerPoint;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerPointRepository extends PagingAndSortingRepository<TlGammaLayerPoint, String>{
	
	@Query("select a from TlGammaLayerPoint a  where intersects(a.kernelGeom , ?1) = true") 
	public List<TlGammaLayerPoint> getDataByFilter(Geometry ptGeom,Pageable pageable);

}
