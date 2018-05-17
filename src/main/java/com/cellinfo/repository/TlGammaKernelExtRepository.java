package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaKernelExt;

public interface TlGammaKernelExtRepository extends PagingAndSortingRepository<TlGammaKernelExt,String>{

	Page<TlGammaKernelExt> findByKernelClassidAndUserName(String kernelClassid, String userName,Pageable pageable);

	List<TlGammaKernelExt> findByKernelClassidAndUserName(String kernelClassid, String userName);

}
