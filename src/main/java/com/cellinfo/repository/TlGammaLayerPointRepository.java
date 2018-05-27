package com.cellinfo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerPoint;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerPointRepository extends PagingAndSortingRepository<TlGammaLayerPoint, String>{
	
	@Query("select a from TlGammaLayerPoint a  where a.kernelClassid = ?1 and intersects(a.kernelGeom , ?2) = true") 
	public List<TlGammaLayerPoint> getDataByFilter(String kernelClassid,Geometry ptGeom,Pageable pageable);
	
	@Query("select count(*) from TlGammaLayerPoint a  where a.kernelClassid = ?1 and intersects(a.kernelGeom , ?2) = true") 
	public long getDataCountByFilter(String kernelClassid,Geometry geom);
		
	@Query(value = "select kernel_guid from Tl_Gamma_Layer_Point a  where a.kernel_Classid = ?1 and st_intersects( a.kernel_geom , ?2) = true",nativeQuery = true) 
	public List<String> getKernelIdByGeoFilter(String kernelClassid,Geometry geom);
	
	@Transactional
	@Modifying
	@Query(value = "insert into  tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num)(select ?1,a.kernel_guid,1 from Tl_Gamma_Layer_Point a  where a.kernel_Classid = ?2 and st_intersects( a.kernel_geom , ?3) = true)",nativeQuery = true) 
	public int createGeoFilter(String tmpGuid,String kernelClassid,Geometry geom);
	
	public List<TlGammaLayerPoint> findByKernelClassid(String kernelClassid,Pageable pageable);

	public long countByKernelClassid(String kernelClassid);

}
