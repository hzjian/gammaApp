package com.cellinfo.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.controller.entity.FilterParameter;
import com.cellinfo.controller.entity.SubtypeParameter;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaKernelExt;
import com.cellinfo.entity.TlGammaKernelFilter;
import com.cellinfo.repository.TlGammaKernelAttrRepository;
import com.cellinfo.repository.TlGammaKernelExtRepository;
import com.cellinfo.repository.TlGammaKernelFilterRepository;

@Service
public class SysKernelExtService {

	
	@Autowired
	private TlGammaKernelExtRepository tlGammaKernelExtRepository;
	
	@Autowired
	private TlGammaKernelFilterRepository tlGammaKernelFilterRepository;
	
	@Autowired
	private TlGammaKernelAttrRepository tlGammaKernelAttrRepository;
	
	
	public TlGammaKernelExt saveKernelExt(TlGammaKernelExt ext, List<TlGammaKernelFilter> filterList)
	{		
		this.tlGammaKernelFilterRepository.save(filterList);
		
		return this.tlGammaKernelExtRepository.save(ext);
	}
	
	
	public TlGammaKernelExt getKernelExt(String extGuid)
	{
		return this.tlGammaKernelExtRepository.findOne(extGuid);
	}
	
	public List<TlGammaKernelFilter> getKernelExtFilter(String extGuid)
	{
		return this.tlGammaKernelFilterRepository.findByExtGuid(extGuid);
	}
	
	
	public void deleteKernelExt(String extGuid)
	{
		this.tlGammaKernelFilterRepository.deleteByExtGuid(extGuid);
		this.tlGammaKernelExtRepository.delete(extGuid);
	}
	
	public TlGammaKernelAttr getKernelAttr(String attrguid)
	{
		return this.tlGammaKernelAttrRepository.findOne(attrguid);
	}


	public Page<SubtypeParameter> getKernelExtList(String kernelClassid,String userName,Pageable pageable) {
		List<SubtypeParameter> subTypeList = new LinkedList<SubtypeParameter>();
		Page<TlGammaKernelExt> extList = this.tlGammaKernelExtRepository.findByKernelClassidAndUserName(kernelClassid,userName,pageable);
		if(extList!= null && extList.getSize()>0)
		{
			for(TlGammaKernelExt ext: extList.getContent())
			{
				SubtypeParameter subtype = new SubtypeParameter();
				subtype.setExtDesc(ext.getExtDesc());
				subtype.setExtGuid(ext.getExtGuid());
				subtype.setExtName(ext.getExtName());
				subtype.setKernelClassid(ext.getKernelClassid());
				
				List<FilterParameter>  resFilterList = new LinkedList<FilterParameter>();
				List<TlGammaKernelFilter> filterList = this.tlGammaKernelFilterRepository.findByExtGuid(ext.getExtGuid());
				for(TlGammaKernelFilter filter : filterList)
				{
					FilterParameter fPara = new FilterParameter();
					fPara.setAttrGuid(filter.getAttrGuid());
					fPara.setMaxValue(filter.getMaxValue());
					fPara.setMinValue(filter.getMinValue());
					fPara.setType(filter.getFilterType());
					resFilterList.add(fPara);
				}
				subtype.setFilterList(resFilterList);
				subTypeList.add(subtype);
			}
		}
		
		return new PageImpl<SubtypeParameter>(subTypeList,pageable,extList.getTotalElements());
	}


	public List<SubtypeParameter> getKernelExtList(String kernelClassid, String userName) {
		List<SubtypeParameter> subTypeList = new LinkedList<SubtypeParameter>();
		List<TlGammaKernelExt> extList = this.tlGammaKernelExtRepository.findByKernelClassidAndUserName(kernelClassid,userName);
		if(extList!= null && extList.size()>0)
		{
			for(TlGammaKernelExt ext: extList)
			{
				SubtypeParameter subtype = new SubtypeParameter();
				subtype.setExtDesc(ext.getExtDesc());
				subtype.setExtGuid(ext.getExtGuid());
				subtype.setExtName(ext.getExtName());
				subtype.setKernelClassid(ext.getKernelClassid());
				
				subTypeList.add(subtype);
			}
		}
		return subTypeList;
	}
}
