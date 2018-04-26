package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerPolygon;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerPolygonRepository extends PagingAndSortingRepository<TlGammaLayerPolygon, String>{


	@Query("select a from TlGammaLayerPolygon a  where within(a.kernelGeom , ?1) = true ") 
	public List<TlGammaLayerPolygon> getDataByFilter(Geometry ptGeom,Pageable pageable);
}
