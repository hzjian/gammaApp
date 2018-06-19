package com.cellinfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cellinfo.entity.ViewTask;

public interface ViewTaskRepository  extends PagingAndSortingRepository<ViewTask, String> {

	@Query("select u from ViewTask u where u.userName = ?1 and u.taskName like %?2%")
	Page<ViewTask> getUserCreateTasks(String userName,String strFilter,Pageable pageable);
}
