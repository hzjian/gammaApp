package com.cellinfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.repository.TlGammaGroupRepository;

@Service
public class SysGroupService {

	@Autowired
	private TlGammaGroupRepository tlGammaGroupRepository;
	
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
}
