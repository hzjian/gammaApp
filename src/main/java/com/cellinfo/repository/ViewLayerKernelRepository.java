package com.cellinfo.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;

import com.cellinfo.entity.ViewLayerKernel;

public interface ViewLayerKernelRepository extends PagingAndSortingRepository<ViewLayerKernel, String>{

	long countByKernelClassid(String kernelClassid);

	@Async
	CompletableFuture<List<ViewLayerKernel>> findByKernelClassid(String kernelClassid, Pageable page);

}
