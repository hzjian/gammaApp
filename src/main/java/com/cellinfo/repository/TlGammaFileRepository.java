package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaFile;

public interface TlGammaFileRepository extends PagingAndSortingRepository<TlGammaFile, String>{
	public List<TlGammaFile> findByRelateid(String rid);
}
