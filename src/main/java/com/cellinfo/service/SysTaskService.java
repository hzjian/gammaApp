package com.cellinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.TlGammaTaskUser;
import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskKernel;
import com.cellinfo.repository.TlGammaTaskAttrRepository;
import com.cellinfo.repository.TlGammaTaskKernelRepository;
import com.cellinfo.repository.TlGammaTaskRepository;
import com.cellinfo.repository.TlGammaTaskUserRepository;
import com.cellinfo.repository.ViewTaskAttrRepository;
import com.cellinfo.repository.ViewTaskKernelRepository;


@Service
public class SysTaskService {

	@Autowired
	private TlGammaTaskRepository tlGammaTaskRepository ;
	
	@Autowired
	private TlGammaTaskAttrRepository  tlGammaTaskAttrRepository;
	
	@Autowired
	private TlGammaTaskKernelRepository tlGammaTaskKernelRepository ;
	
	@Autowired
	private ViewTaskKernelRepository viewTaskKernelRepository ;
	
	@Autowired
	private ViewTaskAttrRepository viewTaskAttrRepository;
	
	@Autowired
	private TlGammaTaskUserRepository tlGammaTaskUserRepository;
	
	public Page<TlGammaTask> getAll(PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findAll(pageInfo);
	}

	public Page<TlGammaTask> getTaskList(PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findAll(pageInfo);
	}

	public TlGammaTask addTask(TlGammaTask task) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.save(task);
	}

	public TlGammaTask getByGuid(String taskGuid) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findOne(taskGuid);
	}

	public void deleteTask(String taskGuid) {
		// TODO Auto-generated method stub
		this.tlGammaTaskRepository.delete(taskGuid);
	}

	public TlGammaTask updateTask(TlGammaTask task) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.save(task);
	}

	public Iterable<TlGammaTask> getByName(String taskName) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findAll();
	}
	
	public List<ViewTaskKernel> getTaskKernel(String taskGuid){
		return this.viewTaskKernelRepository.getByTaskGuid(taskGuid);
	}

	public List<ViewTaskAttr> getTaskAttr(String taskGuid){
		return this.viewTaskAttrRepository.getByTaskGuid(taskGuid);
	}
	
	public void saveTaskUser(TlGammaTaskUser entity) {
		this.tlGammaTaskUserRepository.save(entity);
	}

	public Page<TlGammaTask> getTaskByGroupGuid(String groupGuid,Pageable pageable) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findByGroupGuid(groupGuid,pageable);
	}
	
	public Page<TlGammaTask> getTaskByUsername(String userName,Pageable pageable) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findByUserName(userName,pageable);
	}

	public Page<TlGammaTask> getByUserName(String userName,Pageable pageable) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findByUserName(userName,pageable);
	}
}
