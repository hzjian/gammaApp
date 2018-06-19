package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaFile;

public interface TlGammaFileRepository extends PagingAndSortingRepository<TlGammaFile, String>{
	public List<TlGammaFile> findByRelateId(String rid);

	public Page<TlGammaFile> findByRelateId(String skey, Pageable pageable);
}
