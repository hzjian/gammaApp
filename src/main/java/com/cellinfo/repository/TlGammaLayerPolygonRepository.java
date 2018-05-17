package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.cellinfo.entity.TlGammaLayerPolygon;
import com.vividsolutions.jts.geom.Geometry;

public interface TlGammaLayerPolygonRepository extends PagingAndSortingRepository<TlGammaLayerPolygon, String>{


	@Query("select a from TlGammaLayerPolygon a  where intersects(a.kernelGeom , ?1) = true ") 
	public List<TlGammaLayerPolygon> getDataByFilter(Geometry ptGeom,Pageable pageable);
	
	
	@Query(value = "select a.kernel_guid from tl_gamma_layer_polygon a  where a.kernel_Classid = :kernelClassid and intersects( a.kernel_Geom , :geom) = true and a.kernel_Guid in "
			+ "(select kernel_Guid from tl_gamma_layer_attribute where :condition)", nativeQuery = true) 
	public List<String> getDataByFilter(@Param("kernelClassid") String kernelClassid,@Param("geom") Geometry geom, @Param("condition") String condition);

	
	@Query(value = "select a.kernel_guid from tl_gamma_layer_polygon a  where a.kernel_Classid = :kernelClassid and a.kernel_Guid in "
			+ "(select kernel_Guid from tl_gamma_layer_attribute where :condition)", nativeQuery = true) 
	public List<String> getDataByFilter(@Param("kernelClassid") String kernelClassid,@Param("condition") String condition);
	
	@Query(value = "select kernel_guid from Tl_Gamma_Layer_polygon a  where a.kernel_Classid = ?1 and st_intersects( a.kernel_geom , ?2) = true",nativeQuery = true) 
	public List<String> getDataByGeoFilter(String kernelClassid,Geometry geom);


	public long countByKernelClassid(String kernelClassid);


	public List<TlGammaLayerPolygon> findByKernelClassid(String kernelClassid, Pageable page);
}
