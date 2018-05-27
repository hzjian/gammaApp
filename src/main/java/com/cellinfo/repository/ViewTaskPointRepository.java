package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskPoint;
import com.vividsolutions.jts.geom.Geometry;

public interface ViewTaskPointRepository extends PagingAndSortingRepository<ViewTaskPoint, String>{
	
	@Query("select a from ViewTaskPoint a  where a.kernelClassid = ?1 and a.extGuid = ?2 and (intersects( a.kernelGeom , ?3) = true)") 
	public List<ViewTaskPoint> getDataByFilter(String kernelClassid,String extGuid,Geometry geom,Pageable pageable);

	@Query("select count(*) from ViewTaskPoint a  where a.kernelClassid = ?1 and a.extGuid = ?2 and (intersects( a.kernelGeom , ?3) = true)") 
	public long getDataCountByFilter(String kernelClassid,String extGuid,Geometry geom);
}
