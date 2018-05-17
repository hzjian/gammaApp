package com.cellinfo.repository;

import java.sql.Timestamp;
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
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_Long > ?2",nativeQuery = true) 
	public List<String> gtLongValue(String attrGuid,Long intvalue);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_Long < ?2",nativeQuery = true) 
	public List<String> ltLongValue(String attrGuid,Long intvalue);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_Long > ?2 and attr_Long < ?3",nativeQuery = true) 
	public List<String> betweenLongValue(String attrGuid,Long minvalue,Integer maxvalue);
	

	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_double > ?2",nativeQuery = true) 
	public List<String> gtDoubleValue(String attrGuid,Double value);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_double < ?2",nativeQuery = true) 
	public List<String> ltDoubleValue(String attrGuid,Double value);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_double > ?2 and attr_double < ?3",nativeQuery = true) 
	public List<String> betweenDoubleValue(String attrGuid,Double minvalue,Double maxvalue);
	
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_time > ?2",nativeQuery = true) 
	public List<String> gtTimeStampValue(String attrGuid,Timestamp value);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_time < ?2",nativeQuery = true) 
	public List<String> ltDoubleValue(String attrGuid,Timestamp value);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_time > ?2 and attr_time < ?3",nativeQuery = true) 
	public List<String> betweenTimeStampValue(String attrGuid,Timestamp minvalue,Timestamp maxvalue);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_text like %?2%",nativeQuery = true) 
	public List<String> likeStringValue(String attrGuid,String value);
	
//	@Modifying
//	@Query("update BsMicrogridEntity t set t.bsMicrogridRealarea =?1 where t.objectid = ?2")
//	void updateMicrogridArea(Double area,Long oId);
	
	
	//public List<TlGammaLayerAttribute>  findBy

}
