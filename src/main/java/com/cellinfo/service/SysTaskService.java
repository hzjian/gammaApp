package com.cellinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskKernel;
import com.cellinfo.repository.TlGammaTaskAttrRepository;
import com.cellinfo.repository.TlGammaTaskKernelRepository;
import com.cellinfo.repository.TlGammaTaskRepository;
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

	public TlGammaTask getByGUID(String taskGUID) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findOne(taskGUID);
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
	
}
