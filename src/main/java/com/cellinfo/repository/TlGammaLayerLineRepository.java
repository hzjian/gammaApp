package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerLine;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerLineRepository  extends PagingAndSortingRepository<TlGammaLayerLine,String>{

	@Query("select a from TlGammaLayerLine a  where intersects( a.kernelGeom , ?1) = true") 
	public List<TlGammaLayerLine> getDataByFilter(Geometry ptGeom,Pageable pageable);
	
	
//	Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
//	User findByEmailAddress(String emailAddress);
	
	@Query(value = "select * from TlGammaLayerLine a  where a.kernelClassid =?1 and st_intersects( a.kernelGeom , ?2) = true and a.kernelGuid in "
			+ "(select kernelGuid from TlGammaLayerAttribute where ?3)", nativeQuery = true) 
	public List<TlGammaLayerLine> getDataByFilter(String kernelClassid,Geometry ptGeom,String condition);

	@Query(value = "select * from TlGammaLayerLine a  where a.kernelClassid =?1 and a.kernelGuid in "
			+ "(select kernelGuid from TlGammaLayerAttribute where ?2)", nativeQuery = true) 
	public List<TlGammaLayerLine> getDataByFilter(String kernelClassid,String condition);
	
	@Query(value = "select kernel_guid from Tl_Gamma_Layer_line a  where a.kernel_Classid = ?1 and st_intersects( a.kernel_geom , ?2) = true",nativeQuery = true) 
	public List<String> getDataByGeoFilter(String kernelClassid,Geometry geom);

	public long countByKernelClassid(String kernelClassid);

	public List<TlGammaLayerLine> findByKernelClassid(String kernelClassid, Pageable page);

}
