package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernelAttr;

public interface TlGammaKernelAttrRepository extends PagingAndSortingRepository<TlGammaKernelAttr,String>{

	List<TlGammaKernelAttr> findByKernelClassid(String kernelClassid);

	@Query("select a from TlGammaKernelAttr a  where a.kernelClassid = ?1 and ( a.userName= ?2 or a.shareGrade='GROUP' ) and a.attrName like %?3%") 
	Page<TlGammaKernelAttr> getKernelAttrList(String classId,String userName, String filterStr, Pageable pageInfo);
	
	@Query("select a from TlGammaKernelAttr a  where a.kernelClassid = ?1 and ( a.userName= ?2 or a.shareGrade='GROUP' ) ") 
	Page<TlGammaKernelAttr> getKernelAttrList(String classId,String userName, Pageable pageInfo);

	@Query("select a from TlGammaKernelAttr a  where a.kernelClassid = ?1 and a.attrName = ?2") 
	List<TlGammaKernelAttr> findAllByAttrName(String classId,String attrName);

	@Query("select a from TlGammaKernelAttr a  where a.kernelClassid = ?1 and ( a.userName= ?2 or a.shareGrade='GROUP' ) and a.attrGuid not in (select attrGuid from TlGammaTaskAttr where taskGuid =?3)") 
	List<TlGammaKernelAttr> getTaskAttrAvalialble(String classId,String userName,String taskId);


}
