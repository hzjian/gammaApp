package com.cellinfo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.TlGammaTaskAttr;
import com.cellinfo.entity.TlGammaTaskExt;
import com.cellinfo.entity.TlGammaTaskUser;
import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskUser;
import com.cellinfo.repository.TlGammaKernelAttrRepository;
import com.cellinfo.repository.TlGammaTaskAttrRepository;
import com.cellinfo.repository.TlGammaTaskExtRepository;
import com.cellinfo.repository.TlGammaTaskKernelRepository;
import com.cellinfo.repository.TlGammaTaskRepository;
import com.cellinfo.repository.TlGammaTaskUserRepository;
import com.cellinfo.repository.ViewTaskAttrRepository;
import com.cellinfo.repository.ViewTaskUserRepository;


@Service
public class SysTaskService {

	@Autowired
	private TlGammaTaskRepository tlGammaTaskRepository ;
	
	@Autowired
	private TlGammaTaskAttrRepository  tlGammaTaskAttrRepository;
	
	@Autowired
	private TlGammaTaskKernelRepository tlGammaTaskKernelRepository ;
	
	@Autowired
	private ViewTaskAttrRepository viewTaskAttrRepository;
	
	@Autowired
	private TlGammaTaskUserRepository tlGammaTaskUserRepository;
	
	@Autowired
	private ViewTaskUserRepository  viewTaskUserRepository;
	
	@Autowired
	private TlGammaKernelAttrRepository tlGammaKernelAttrRepository;
	
	@Autowired
	private TlGammaTaskExtRepository tlGammaTaskExtRepository;
	
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

	public Optional<TlGammaTask> getByGuid(String taskGuid) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findById(taskGuid);
	}

	public void deleteTask(String taskGuid) {
		// TODO Auto-generated method stub
		this.tlGammaTaskRepository.deleteById(taskGuid);
	}

	public TlGammaTask updateTask(TlGammaTask task) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.save(task);
	}

	public Iterable<TlGammaTask> getByName(String taskName) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findAll();
	}

	public List<ViewTaskAttr> getTaskAttr(String taskGuid){
		return this.viewTaskAttrRepository.getByTaskGuid(taskGuid);
	}
	
	public TlGammaTaskUser saveTaskUser(TlGammaTaskUser entity) {
		return this.tlGammaTaskUserRepository.save(entity);
	}
	
	public Page<ViewTaskUser> getTaskUserList (String taskGuid,Pageable pageable) {
		return this.viewTaskUserRepository.getByTaskGuid(taskGuid, pageable);
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

	public void deleteTaskUser(String taskGuid, String userName) {
		// TODO Auto-generated method stub
		this.tlGammaTaskUserRepository.deleteByTaskGuidAndUserName(taskGuid,userName);
	}

	public TlGammaTaskAttr addTaskField(TlGammaKernelAttr kernelattr, TlGammaTaskAttr taskattr) {
		// TODO Auto-generated method stub
		
		this.tlGammaKernelAttrRepository.save(kernelattr);
		
		return this.tlGammaTaskAttrRepository.save(taskattr);
	}
	
	public TlGammaTaskAttr addTaskField(TlGammaTaskAttr taskattr) {
		
		return this.tlGammaTaskAttrRepository.save(taskattr);
	}

	public Page<ViewTaskAttr> getTaskFieldList(String taskGuid, PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.viewTaskAttrRepository.getByTaskGuid(taskGuid,pageInfo);
	}

	public void saveTaskExt(TlGammaTaskExt taskExt) {
		// TODO Auto-generated method stub
		this.tlGammaTaskExtRepository.save(taskExt);
	}

	public Page<ViewTaskUser> getTaskByUserParticapate(String userName, PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.viewTaskUserRepository.getByUserName(userName, pageInfo);
	}
	
	
}
