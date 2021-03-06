package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernel;

public interface TlGammaKernelRepository extends PagingAndSortingRepository<TlGammaKernel,String>{

	public List<TlGammaKernel> findByGroupGuid(String groupGuid);

	@Query("select a from TlGammaKernel a  where a.groupGuid = ?1 and a.kernelClassname like %?2%") 
	public Page<TlGammaKernel> findByGroupGuid(String groupGuid,String key, Pageable page);

	public List<TlGammaKernel> findByKernelClassname(String kernelClassname);

	@Query("select a from TlGammaKernel a  where a.groupGuid = ?1 and a.kernelClassid not in (select kernelClassid  from TlGammaTaskLayer where taskGuid = ?2)") 
	public List<TlGammaKernel> getTaskKernelAvaliable(String groupId, String taskId);

	@Query("select a from TlGammaKernel a  where a.kernelClassid <> ?1 and a.kernelClassname = ?2")
	public List<TlGammaKernel> getByKernelClassnameExclude(String classId, String className);

}
