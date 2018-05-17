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
		
	@Query(value = "select * from TlGammaLayerPoint a  where a.kernelClassid = ?1 and st_intersects( a.kernelGeom , ?2) = true and a.kernelGuid in (select kernelGuid from TlGammaLayerAttribute where ?3)",nativeQuery = true) 
	public List<TlGammaLayerPoint> getDataByFilter(String kernelClassid,Geometry ptGeom,String condition);
	
	@Query(value = "select * from TlGammaLayerPoint a  where a.kernelClassid = ?1 and a.kernelGuid in (select kernelGuid from TlGammaLayerAttribute where ?2)",nativeQuery = true) 
	public List<TlGammaLayerPoint> getDataByFilter(String kernelClassid,String condition);

	
	@Query(value = "select kernel_guid from Tl_Gamma_Layer_Point a  where a.kernel_Classid = ?1 and st_intersects( a.kernel_geom , ?2) = true",nativeQuery = true) 
	public List<String> getDataByGeoFilter(String kernelClassid,Geometry geom);
	
	public List<TlGammaLayerPoint> findByKernelClassid(String kernelClassid,Pageable pageable);

	public long countByKernelClassid(String kernelClassid);

}
