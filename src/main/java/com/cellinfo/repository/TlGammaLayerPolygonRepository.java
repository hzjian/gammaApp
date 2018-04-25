package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerPolygon;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerPolygonRepository extends PagingAndSortingRepository<TlGammaLayerPolygon, String>{


	@Query("select a from TlGammaLayerPolygon a  where within(?1 , a.kernelGeom) = true and rownum < ?2") 
	public List<TlGammaLayerPolygon> getDataByFilter(Geometry ptGeom,Integer maxNum);
}
