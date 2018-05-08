package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTaskLine;
import com.vividsolutions.jts.geom.Geometry;

public interface ViewTaskLineRepository  extends PagingAndSortingRepository<ViewTaskLine,String>{

	@Query("select a from ViewTaskLine a  where a.kernelClassid = ?1 and a.taskGuid = ?2 and a.kernelStatus = ?3 and (intersects( a.kernelGeom , ?4) = true)") 
	public List<ViewTaskLine> getDataByFilter(String kernelClassid,String taskGuid,Integer status,Geometry ptGeom,Pageable pageable);

}
