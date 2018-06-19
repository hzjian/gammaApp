package com.cellinfo.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.TlGammaTaskAttr;
import com.cellinfo.entity.TlGammaTaskAttrrank;
import com.cellinfo.entity.TlGammaTaskComment;
import com.cellinfo.entity.TlGammaTaskLayer;
import com.cellinfo.entity.TlGammaTaskUser;
import com.cellinfo.entity.ViewTask;
import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskExt;
import com.cellinfo.entity.ViewTaskUser;
import com.cellinfo.entity.ViewTaskUserPK;
import com.cellinfo.repository.TlGammaKernelFilterRepository;
import com.cellinfo.repository.TlGammaTaskAttrRepository;
import com.cellinfo.repository.TlGammaTaskAttrrankRepository;
import com.cellinfo.repository.TlGammaTaskCommentRepository;
import com.cellinfo.repository.TlGammaTaskLayerRepository;
import com.cellinfo.repository.TlGammaTaskRepository;
import com.cellinfo.repository.TlGammaTaskUserRepository;
import com.cellinfo.repository.ViewTaskAttrRepository;
import com.cellinfo.repository.ViewTaskExtRepository;
import com.cellinfo.repository.ViewTaskRepository;
import com.cellinfo.repository.ViewTaskUserRepository;


@Service
public class SysTaskService {

	@Autowired
	private TlGammaTaskRepository tlGammaTaskRepository ;
	
	@Autowired
	private TlGammaTaskAttrRepository  tlGammaTaskAttrRepository;
	
	@Autowired
	private ViewTaskAttrRepository viewTaskAttrRepository;
	
	@Autowired
	private TlGammaTaskUserRepository tlGammaTaskUserRepository;
	
	@Autowired
	private ViewTaskUserRepository  viewTaskUserRepository;
	
	@Autowired
	private ViewTaskRepository viewTaskRepository;
	
	@Autowired
	private TlGammaTaskLayerRepository tlGammaTaskLayerRepository;
	
	@Autowired
	private ViewTaskExtRepository viewTaskExtRepository;
	
	@Autowired
	private TlGammaKernelFilterRepository tlGammaKernelFilterRepository;
	
	@Autowired
	private TlGammaTaskAttrrankRepository tlGammaTaskAttrrankRepository;
	
	@Autowired
	private TlGammaTaskCommentRepository tlGammaTaskCommentRepository;
	
	
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

	@Transactional
	public void deleteTask(String taskGuid) {
		// TODO Auto-generated method stub
		this.tlGammaTaskRepository.deleteById(taskGuid);
		this.tlGammaTaskAttrrankRepository.deleteByTaskGuid(taskGuid);
		this.tlGammaTaskAttrRepository.deleteByTaskGuid(taskGuid);
		this.tlGammaTaskLayerRepository.deleteByTaskGuid(taskGuid);
		this.tlGammaTaskUserRepository.deleteByTaskGuid(taskGuid);
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

	public Page<ViewTask> getUserCreateTasks(String userName,String strFilter,Pageable pageable) {
		// TODO Auto-generated method stub
		return this.viewTaskRepository.getUserCreateTasks(userName,strFilter,pageable);
	}

	@Transactional
	public void deleteTaskUser(String taskGuid, String userName) {
		// TODO Auto-generated method stub
		this.tlGammaTaskUserRepository.deleteByTaskGuidAndUserName(taskGuid,userName);
	}
	
	public TlGammaTaskAttr addTaskField(TlGammaTaskAttr taskattr) {
		
		return this.tlGammaTaskAttrRepository.save(taskattr);
	}

	public Page<ViewTaskAttr> getTaskFieldList(String taskGuid, PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.viewTaskAttrRepository.getByTaskGuid(taskGuid,pageInfo);
	}

	public TlGammaTaskLayer saveTaskLayer(TlGammaTaskLayer taskLayer) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskLayerRepository.save(taskLayer);
	}

	public Optional<ViewTaskExt> getTaskLayer(String  layerId) {
		// TODO Auto-generated method stub
		return this.viewTaskExtRepository.findById(layerId);
	}
	
	public Page<ViewTaskUser> getTaskByUserParticapate(String userName, PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.viewTaskUserRepository.getByUserName(userName, pageInfo);
	}

	public void deleteTaskAttr(String taskGuid, String fieldGuid) {
		// TODO Auto-generated method stub
		this.tlGammaTaskAttrRepository.deleteTaskAttr(taskGuid,fieldGuid);
	}

	public Long getAttrApplyNum(String attrId) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskAttrRepository.countByAttrGuid(attrId);
	}

	public Page<ViewTaskUser> getByUserInvolveTasks(String userName, String filterStr,
			PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.viewTaskUserRepository.getUserTasks(userName,filterStr,pageInfo);
	}

	public Page<ViewTaskUser> getPasswordTasks( String userName, String filterStr,	PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.viewTaskUserRepository.getPasswordTasks(userName,filterStr,pageInfo);
	}

	public long getExtApplyNum(String attrId) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelFilterRepository.countByAttrGuid(attrId);
	}

	public List<TlGammaTaskLayer> getTaskLayerList(String taskId, Integer status) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskLayerRepository.getLayerList(taskId,status);
	}

	public void deleteTaskLayer(String layerId) {
		// TODO Auto-generated method stub
		this.tlGammaTaskLayerRepository.deleteById(layerId);
	}

	public List<TlGammaTaskAttrrank> getTaskAttrRankList(String taskId) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskAttrrankRepository.findByTaskGuid(taskId);
		
	}

	public Optional<TlGammaTaskAttrrank>  getTaskRank(String rankId) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskAttrrankRepository.findById(rankId);
	}

	public TlGammaTaskAttrrank saveTaskAttrRank(TlGammaTaskAttrrank rank) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskAttrrankRepository.save(rank);
	}

	public TlGammaTaskAttr getTaskAttr(String taskId, String attrId) {
		List<TlGammaTaskAttr> taskAttrList = this.tlGammaTaskAttrRepository.findByTaskGuidAndAttrGuid(taskId,attrId);
		if(taskAttrList!=null && taskAttrList.size()>0)
			return taskAttrList.get(0);
		else
			return null;
		
	}

	public TlGammaTaskAttr saveTaskAttr(TlGammaTaskAttr taskAttr) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskAttrRepository.save(taskAttr);
	}

	public Optional<TlGammaTask> getTaskbyId(String taskId) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.findById(taskId);
	}

	public Optional<ViewTaskUser> getTaskUser(String taskId, String userName) {
		// TODO Auto-generated method stub
		return this.viewTaskUserRepository.findById(new ViewTaskUserPK(taskId,userName));
	}

	public List<TlGammaTask> getByTaskNameExclude(String taskId,String groupId,String taskName) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskRepository.getByTaskNameExclude(taskId,groupId,taskName);
	}

	public Optional<ViewTask> getViewTaskByGuid(String id) {
		// TODO Auto-generated method stub
		return this.viewTaskRepository.findById(id);
	}

	public Page<TlGammaTaskComment> getTaskCommentList(String taskId, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskCommentRepository.findByTaskGuid(taskId,pageable);
	}

	public TlGammaTaskComment saveUserComment(TlGammaTaskComment taskcomment) {
		// TODO Auto-generated method stub
		return this.tlGammaTaskCommentRepository.save(taskcomment);
	}

	public Page<ViewTaskAttr> getTaskFieldList(String taskGuid, String id, PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.viewTaskAttrRepository.getByTaskGuid(taskGuid,id,pageInfo);
	}
	
	
}
