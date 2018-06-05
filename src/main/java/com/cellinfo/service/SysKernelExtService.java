package com.cellinfo.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaKernelExt;
import com.cellinfo.entity.TlGammaKernelFilter;
import com.cellinfo.entity.TlGammaKernelGeoFilter;
import com.cellinfo.repository.TlGammaKernelAttrRepository;
import com.cellinfo.repository.TlGammaKernelExtRepository;
import com.cellinfo.repository.TlGammaKernelFilterRepository;
import com.cellinfo.repository.TlGammaKernelGeoFilterRepository;

@Service
public class SysKernelExtService {

	
	@Autowired
	private TlGammaKernelExtRepository tlGammaKernelExtRepository;
	
	@Autowired
	private TlGammaKernelFilterRepository tlGammaKernelFilterRepository;
	
	@Autowired
	private TlGammaKernelAttrRepository tlGammaKernelAttrRepository;
	
	@Autowired
	private TlGammaKernelGeoFilterRepository tlGammaKernelGeoFilterRepository ;
	
	
	public TlGammaKernelExt saveKernelExt(TlGammaKernelExt ext)
	{				
		return this.tlGammaKernelExtRepository.save(ext);
	}
	
	public TlGammaKernelGeoFilter saveGeoFilter(TlGammaKernelGeoFilter geoFilter)
	{
		return this.tlGammaKernelGeoFilterRepository.save(geoFilter);
	}
	
	public Optional<TlGammaKernelExt> getKernelExt(String extGuid)
	{
		return this.tlGammaKernelExtRepository.findById(extGuid);
	}
	
	public List<TlGammaKernelFilter> getKernelExtFilter(String extGuid)
	{
		return this.tlGammaKernelFilterRepository.findByExtGuid(extGuid);
	}
	
	@Transactional
	public void deleteKernelExt(String extGuid)
	{
		try {
			this.tlGammaKernelFilterRepository.deleteByExtGuid(extGuid);
			this.tlGammaKernelGeoFilterRepository.deleteByExtGuid(extGuid);
			this.tlGammaKernelExtRepository.deleteById(extGuid);
			//删除核心对象所在节点的标签
			//TODO
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Optional<TlGammaKernelAttr> getKernelAttr(String attrguid)
	{
		return this.tlGammaKernelAttrRepository.findById(attrguid);
	}

	public Page<Map<String ,String >> getKernelExtList(String filterStr,String userName,Pageable pageable) {
		List<Map<String ,String >> subTypeList = new LinkedList<Map<String ,String >>();
		Page<TlGammaKernelExt> extList = null;
		if(filterStr!= null && filterStr.length()>0)
			extList = this.tlGammaKernelExtRepository.filterByExtName(filterStr,userName,pageable);
		else
			extList = this.tlGammaKernelExtRepository.findByUserName(userName,pageable);
			
		if(extList!= null && extList.getSize()>0)
		{
			for(TlGammaKernelExt ext: extList.getContent())
			{
				
				Map<String ,String >  subType = new HashMap<String ,String>();
				subType.put("extDesc", ext.getExtDesc());
				subType.put("extId", ext.getExtGuid());
				subType.put("classId", ext.getKernelClassid());
				subType.put("extName", ext.getExtName());
				subType.put("extDesc", ext.getExtDesc());
				subType.put("kernelNum", String.valueOf(ext.getKernelNum()));
				subType.put("geoFilterNum", String.valueOf(ext.getGeofilterNum()));
				subType.put("filterNum", String.valueOf(ext.getFilterNum()));
				subTypeList.add(subType);
			}
		}
		
		return new PageImpl<Map<String ,String >>(subTypeList,pageable,extList.getTotalElements());
	}


	public List<Map<String ,String >> getKernelExtList(String kernelClassid, String userName) {
		List<Map<String ,String >> kList = new LinkedList<Map<String,String>>();
		List<TlGammaKernelExt> extList = this.tlGammaKernelExtRepository.findByKernelClassidAndUserName(kernelClassid,userName);
		if(extList!= null && extList.size()>0)
		{
			for(TlGammaKernelExt ext: extList)
			{
				Map<String ,String >  subType = new HashMap<String ,String>();
				subType.put("extDesc", ext.getExtDesc());
				subType.put("extId", ext.getExtGuid());
				subType.put("extName", ext.getExtName());
				
				kList.add(subType);
			}
		}
		return kList;
	}

	public void deleteGeoFilter(String filterId) {
		this.tlGammaKernelGeoFilterRepository.deleteById(filterId);
	}

	public void deleteFilter(String filterId) {
		this.tlGammaKernelFilterRepository.deleteById(filterId);
	}

	public TlGammaKernelFilter saveFilter(TlGammaKernelFilter nFilter) {
		return this.tlGammaKernelFilterRepository.save(nFilter);
	}

	public List<TlGammaKernelExt> getKernelExtByName(String extName, String userName) {
		return this.tlGammaKernelExtRepository.getKernelExtByName(extName,userName);
	}

	public List<TlGammaKernelGeoFilter> getGeoFilter(String extGuid) {
		return this.tlGammaKernelGeoFilterRepository.findByExtGuid(extGuid);
	}

	public List<TlGammaKernelFilter> getFilter(String extGuid) {
		return this.tlGammaKernelFilterRepository.findByExtGuid(extGuid);
	}

	public Optional<TlGammaKernelGeoFilter> getGeoFilterByFilterId(String filterId) {
		return this.tlGammaKernelGeoFilterRepository.findById(filterId);
		
	}
}
