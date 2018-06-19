package com.cellinfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.TlGammaTaskComment;

public interface TlGammaTaskCommentRepository extends PagingAndSortingRepository<TlGammaTaskComment, String>{

	Page<TlGammaTaskComment> findByTaskGuid(String taskId, Pageable pageable);

}
