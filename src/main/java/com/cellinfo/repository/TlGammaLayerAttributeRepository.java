package com.cellinfo.repository;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
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
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_Long > ?2 order by a.kernel_guid",nativeQuery = true) 
	public List<String> gtLongValue(String attrGuid,Long intvalue);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_Long < ?2 order by a.kernel_guid",nativeQuery = true) 
	public List<String> ltLongValue(String attrGuid,Long intvalue);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_Long > ?2 and attr_Long < ?3 order by a.kernel_guid",nativeQuery = true) 
	public List<String> betweenLongValue(String attrGuid,Long minvalue,Long maxvalue);
	

	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_double > ?2 order by a.kernel_guid",nativeQuery = true) 
	public List<String> gtDoubleValue(String attrGuid,Double value);
	 
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_double < ?2 order by a.kernel_guid",nativeQuery = true) 
	public List<String> ltDoubleValue(String attrGuid,Double value);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_double > ?2 and attr_double < ?3 order by a.kernel_guid",nativeQuery = true) 
	public List<String> betweenDoubleValue(String attrGuid,Double minvalue,Double maxvalue);
	
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_time > ?2 order by a.kernel_guid",nativeQuery = true) 
	public List<String> gtTimeStampValue(String attrGuid,Timestamp value);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_time < ?2 order by a.kernel_guid",nativeQuery = true) 
	public List<String> ltTimeStampValue(String attrGuid,Timestamp value);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_time > ?2 and attr_time < ?3 order by a.kernel_guid",nativeQuery = true) 
	public List<String> betweenTimeStampValue(String attrGuid,Timestamp minvalue,Timestamp maxvalue);
	
	@Query(value = "select distinct(a.kernel_guid) from tl_Gamma_Layer_Attribute a  where attr_guid = ?1 and attr_text like %?2% order by a.kernel_guid",nativeQuery = true) 
	public List<String> likeStringValue(String attrGuid,String value);
	
	//////////////// insert 
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_Long > ?3 group by a.kernel_guid)",nativeQuery = true) 
	public int insertGTLongValue(String tmpGuid,String attrGuid,Long intvalue);
	
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_Long < ?3 group by a.kernel_guid)",nativeQuery = true) 
	public int insertLTLongValue(String tmpGuid,String attrGuid,Long intvalue);
	
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_Long > ?3 and attr_Long < ?4 group by a.kernel_guid)",nativeQuery = true) 
	public int insertBetweenLongValue(String tmpGuid,String attrGuid,Long minvalue,Long maxvalue);
	
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_double > ?3 group by a.kernel_guid)",nativeQuery = true) 
	public int insertGTDoubleValue(String tmpGuid,String attrGuid,Double value);
	 
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_double < ?3 group by a.kernel_guid)",nativeQuery = true) 
	public int insertLTDoubleValue(String tmpGuid,String attrGuid,Double value);
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_double > ?3 and attr_double < ?4 group by a.kernel_guid)",nativeQuery = true) 
	public int insertBetweenDoubleValue(String tmpGuid,String attrGuid,Double minvalue,Double maxvalue);
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_time > ?3 group by a.kernel_guid)",nativeQuery = true) 
	public int insertGTTimeStampValue(String tmpGuid,String attrGuid,Timestamp value);
	
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_time < ?3 group by a.kernel_guid)",nativeQuery = true) 
	public int insertLTTimeStampValue(String tmpGuid,String attrGuid,Timestamp value);
	
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_time > ?3 and attr_time < ?4 group by a.kernel_guid)",nativeQuery = true) 
	public int insertBetweenTimeStampValue(String tmpGuid,String attrGuid,Timestamp minvalue,Timestamp maxvalue);
	
	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_text like %?3% group by a.kernel_guid)",nativeQuery = true) 
	public int insertLikeStringValue(String tmpGuid,String attrGuid,String value);

	@Transactional
	@Modifying
	@Query(value = "insert into tl_gamma_task_tmp (tmp_guid,kernel_guid,f_num) (select ?1,a.kernel_guid,1 from tl_Gamma_Layer_Attribute a  where attr_guid = ?2 and attr_text in (?3) group by a.kernel_guid)",nativeQuery = true) 
	public int insertInStringValue(String tmpGuid,String attrGuid,String value);

	
	//////////////// update
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_Long > ?4 group by a.kernel_guid)",nativeQuery = true) 
	public int updateGTLongValue(String tmpGuid,Integer fNum,String attrGuid,Long intvalue);
	
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_Long < ?4 group by a.kernel_guid)",nativeQuery = true) 
	public int updateLTLongValue(String tmpGuid,Integer fNum,String attrGuid,Long intvalue);
	
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_Long > ?4 and attr_Long < ?5 group by a.kernel_guid)",nativeQuery = true) 
	public int updateBetweenLongValue(String tmpGuid,Integer fNum,String attrGuid,Long minvalue,Long maxvalue);
	
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_double > ?4 group by a.kernel_guid)",nativeQuery = true) 
	public int updateGTDoubleValue(String tmpGuid,Integer fNum,String attrGuid,Double value);
	 
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_double < ?4 group by a.kernel_guid)",nativeQuery = true) 
	public int updateLTDoubleValue(String tmpGuid,Integer fNum,String attrGuid,Double value);
	
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_double > ?4 and attr_double < ?5 group by a.kernel_guid)",nativeQuery = true) 
	public int updateBetweenDoubleValue(String tmpGuid,Integer fNum,String attrGuid,Double minvalue,Double maxvalue);
	
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_time > ?4 group by a.kernel_guid)",nativeQuery = true) 
	public int updateGTTimeStampValue(String tmpGuid,Integer fNum,String attrGuid,Timestamp value);
	
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_time < ?4 group by a.kernel_guid)",nativeQuery = true) 
	public int updateLTTimeStampValue(String tmpGuid,Integer fNum,String attrGuid,Timestamp value);
	
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_time > ?4 and attr_time < ?5 group by a.kernel_guid)",nativeQuery = true) 
	public int updateBetweenTimeStampValue(String tmpGuid,Integer fNum,String attrGuid,Timestamp minvalue,Timestamp maxvalue);
	
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_text like %?4% group by a.kernel_guid)",nativeQuery = true) 
	public int updateLikeStringValue(String tmpGuid,Integer fNum,String attrGuid,String value);
	
	@Transactional
	@Modifying
	@Query(value = "update tl_gamma_task_tmp set f_num= f_num+1 where tmp_guid = ?1 and f_num =?2 and kernel_guid in (select a.kernel_guid from tl_Gamma_Layer_Attribute a  where attr_guid = ?3 and attr_text in (?4) group by a.kernel_guid)",nativeQuery = true) 
	public int updateInStringValue(String tmpGuid,Integer fNum,String attrGuid,String value);
	
//	@Modifying
//	@Query("update BsMicrogridEntity t set t.bsMicrogridRealarea =?1 where t.objectid = ?2")
//	void updateMicrogridArea(Double area,Long oId);
	
	//public List<TlGammaLayerAttribute>  findBy

}
