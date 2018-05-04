package com.cellinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.repository.TlGammaGroupRepository;
import com.cellinfo.repository.TlGammaKernelRepository;
import com.cellinfo.repository.TlGammaUserRepository;

@Service
public class SysGroupService {

	@Autowired
	private TlGammaGroupRepository tlGammaGroupRepository;
	
	@Autowired
	private TlGammaUserRepository tlGammaUserRepository;
	
	@Autowired
	private TlGammaKernelRepository tlGammaKernelRepository;
	
	public TlGammaGroup addGroup(TlGammaGroup entity)
	{
		return this.tlGammaGroupRepository.save(entity);
	}
	
	public TlGammaGroup updateGroup(TlGammaGroup entity)
	{
		return this.tlGammaGroupRepository.save(entity);
	}
	
	public String  deleteGroup(String groupGuid)
	{
		try {
			this.tlGammaGroupRepository.delete(groupGuid);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "删除组织失败";
		}
		return "删除组织成功";
	}
	
	public Page<TlGammaGroup> getGroupList(PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaGroupRepository.findAll(pageInfo);
	}

	public TlGammaGroup getByGUID(String groupGuid) {
		// TODO Auto-generated method stub
		return this.tlGammaGroupRepository.findOne(groupGuid);
	}

	public Iterable<TlGammaGroup> getByName(String groupName) {
		// TODO Auto-generated method stub
		return this.tlGammaGroupRepository.findByGroupName(groupName);
	}
	
	
	public List<TlGammaUser> getUserByGroup(String groupGuid)
	{
		return this.tlGammaUserRepository.findByGroupGuid( groupGuid);
	}
	
	public List<TlGammaKernel> getKernelClassByGroup(String groupGuid)
	{
		return this.tlGammaKernelRepository.findByGroupGuid( groupGuid);
	}
}
