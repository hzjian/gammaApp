package com.cellinfo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerLine;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerLineRepository  extends PagingAndSortingRepository<TlGammaLayerLine,String>{

	@Query("select a from TlGammaLayerLine a  where intersects( a.kernelGeom , ?1) = true") 
	public List<TlGammaLayerLine> getDataByFilter(Geometry ptGeom,Pageable pageable);
	
	@Query(value = "select kernel_guid from Tl_Gamma_Layer_line a  where a.kernel_Classid = ?1 and st_intersects( a.kernel_geom , ?2) = true",nativeQuery = true) 
	public List<String> getDataByGeoFilter(String kernelClassid,Geometry geom);

	@Transactional
	@Modifying
	@Query(value = "insert into  tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num)(select ?1,a.kernel_guid,1 from Tl_Gamma_Layer_line a  where a.kernel_Classid = ?2 and st_intersects( a.kernel_geom , ?3) = true)",nativeQuery = true) 
	public int createGeoFilter(String tmpGuid,String kernelClassid,Geometry geom);
	
	
	public long countByKernelClassid(String kernelClassid);

	public List<TlGammaLayerLine> findByKernelClassid(String kernelClassid, Pageable page);

}
