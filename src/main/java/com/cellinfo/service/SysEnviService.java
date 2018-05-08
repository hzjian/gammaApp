package com.cellinfo.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaDict;
import com.cellinfo.entity.TlGammaDictItem;
import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.repository.TlGammaDictItemRepository;
import com.cellinfo.repository.TlGammaDictRepository;
import com.cellinfo.repository.TlGammaGroupRepository;
import com.cellinfo.repository.TlGammaKernelAttrRepository;
import com.cellinfo.repository.TlGammaKernelRepository;
import com.cellinfo.repository.TlGammaUserRepository;


@Service
public class SysEnviService {
	@Autowired
	private TlGammaDictRepository tlGammaDictRepository;
	
	@Autowired
	private TlGammaDictItemRepository tlGammaDictItemRepository; 
	
	@Autowired
	private TlGammaKernelRepository tlGammaKernelRepository;
	
	@Autowired
	private TlGammaKernelAttrRepository tlGammaKernelAttrRepository;
	
	@Autowired
	private TlGammaUserRepository tlGammaUserRepository;
	
	@Autowired
	private TlGammaGroupRepository tlGammaGroupRepository;
	
	private Iterable<TlGammaUser> userList;	
	
	private Iterable<TlGammaGroup> groupList;
	

	private Map<String ,LinkedList<String>>  dictMap  = new HashMap<String ,LinkedList<String>>();
	
	private Map<String ,TlGammaKernel>  kernelMap  = new HashMap<String ,TlGammaKernel>();
	
	private Map<String ,TlGammaKernelAttr>  attrMap  = new HashMap<String ,TlGammaKernelAttr>();
	
	private Map<String,TlGammaGroup> groupMap = new HashMap<String ,TlGammaGroup>();

	private static final Logger logger = Logger.getLogger(SysEnviService.class.getName());
	
	public void loadInitData() {
		
		this.groupList = this.tlGammaGroupRepository.findAll();
		
		this.userList = this.tlGammaUserRepository.findAll();
		
		Iterable<TlGammaDict>  dictList =  this.tlGammaDictRepository.findAll();
		
		for (TlGammaDict dict : dictList)
		{
			LinkedList<String> tmp = new LinkedList<String>();
			List<TlGammaDictItem>  items = this.tlGammaDictItemRepository.findByDictId(dict.getDictId());
			for( TlGammaDictItem item : items)
			{
				tmp.add(item.getDictItem());
			}
			this.dictMap.put(dict.getDictName(), tmp);
		}
		
		Iterable<TlGammaKernel>  kernelList =  this.tlGammaKernelRepository.findAll();
		
		for (TlGammaKernel kernel : kernelList)
		{	
			this.kernelMap.put(kernel.getKernelClassid(), kernel);
		}
		
		Iterable<TlGammaKernelAttr>  attrList = tlGammaKernelAttrRepository.findAll();
		
		for (TlGammaKernelAttr attr : attrList)
		{	
			this.attrMap.put(attr.getAttrGuid(), attr);
		}
		
		for (TlGammaGroup group: this.groupList)
		{
			this.groupMap.put(group.getGroupGuid(), group);
		}
		
	}
	public TlGammaUser getUserById(String userId)
	{
		for (TlGammaUser user: this.userList)
		{
			if(user.getUserName().equalsIgnoreCase(userId))
				return user;
		}
		return null;
	}
	
	public TlGammaGroup getGroupById(String groupid)
	{
		return this.groupMap.get(groupid);
	}
	public List<String> getDictItems(String dictname)
	{
		return this.dictMap.get(dictname);
	}
	
	public TlGammaKernel getKernelById(String kernelGuid)
	{
		return this.kernelMap.get(kernelGuid);
	}
	
	public TlGammaKernelAttr getAttrById(String attrGuid)
	{
		return this.attrMap.get(attrGuid);
	}
}
