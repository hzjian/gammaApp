package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskPolygon;
import com.vividsolutions.jts.geom.Geometry;

public interface ViewTaskPolygonRepository extends PagingAndSortingRepository< ViewTaskPolygon, String>{

	@Query("select a from  ViewTaskPolygon a  where a.kernelClassid = ?1 and a.taskGuid = ?2 and (intersects( a.kernelGeom , ?3) = true)") 
	public List< ViewTaskPolygon> getDataByFilter(String kernelClassid,String taskGuid,Geometry ptGeom,Pageable pageable);
}
