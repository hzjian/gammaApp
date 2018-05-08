package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaLayerAttribute;

public interface TlGammaLayerAttributeRepository  extends PagingAndSortingRepository<TlGammaLayerAttribute,Long>{

	@Query("select a from TlGammaLayerAttribute a  where a.kernelGuid = ?1") 
	public List<TlGammaLayerAttribute> getKernelAttribute(String kernerlGuid);
	
	
	@Query("select a from TlGammaLayerAttribute a  where a.kernelGuid = ?1 and a.attrGuid = ?2") 
	public List<TlGammaLayerAttribute> getKernelAttribute(String kernerlGuid,String attrGuid);
	
	@Query("select a from TlGammaLayerAttribute a  where a.kernelGuid = ?1 and a.attrGuid = ?2 and a.taskGuid = ?3") 
	public List<TlGammaLayerAttribute> getKernelAttribute(String kernerlGuid,String attrGuid,String taskGuid);
	
	
	@Query("select a from TlGammaLayerAttribute a  where a.kernelGuid = ?1 and a.attrGuid = ?2 and a.taskGuid = ?3 and a.userName = ?4") 
	public List<TlGammaLayerAttribute> getKernelAttribute(String kernerlGuid,String attrGuid,String taskGuid,String userName);
	
	
//	@Modifying
//	@Query("update BsMicrogridEntity t set t.bsMicrogridRealarea =?1 where t.objectid = ?2")
//	void updateMicrogridArea(Double area,Long oId);

}
