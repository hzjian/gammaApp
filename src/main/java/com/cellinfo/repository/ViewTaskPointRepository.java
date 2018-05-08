package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskPoint;
import com.vividsolutions.jts.geom.Geometry;

public interface ViewTaskPointRepository extends PagingAndSortingRepository<ViewTaskPoint, String>{
	
	@Query("select a from ViewTaskPoint a  where a.kernelClassid = ?1 and a.taskGuid = ?2 and a.kernelStatus = ?3 and (intersects( a.kernelGeom , ?4) = true)") 
	public List<ViewTaskPoint> getDataByFilter(String kernelClassid,String taskGuid,Integer status,Geometry ptGeom,Pageable pageable);

}
