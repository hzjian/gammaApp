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
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.repository.TlGammaDictItemRepository;
import com.cellinfo.repository.TlGammaDictRepository;
import com.cellinfo.repository.TlGammaUserRepository;

@Service
public class SysEnviService {
	
	@Autowired
	private TlGammaUserRepository tlGammaUserRepository;
	
	@Autowired
	private TlGammaDictRepository tlGammaDictRepository;
	
	@Autowired
	private TlGammaDictItemRepository tlGammaDictItemRepository; 
	
	
	private Iterable<TlGammaUser> userList;	

	private Map<String ,LinkedList<String>>  dictMap  = new HashMap<String ,LinkedList<String>>();

	private static final Logger logger = Logger.getLogger(SysEnviService.class.getName());
	
	public void loadInitData() {
		
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
		
	}

	public List<String> getDictItems(String dictname)
	{
		return this.dictMap.get(dictname);
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
	
}
