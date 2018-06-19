package com.cellinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTaskLayer;

public interface TlGammaTaskLayerRepository extends PagingAndSortingRepository<TlGammaTaskLayer, String> {

	@Query("select u from TlGammaTaskLayer u where u.taskGuid = ?1 and u.layerGrade = ?2")
	List<TlGammaTaskLayer> getLayerList(String taskId, Integer status);

	void deleteByTaskGuid(String taskGuid);

}
