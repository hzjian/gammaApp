package com.cellinfo.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.controller.entity.ExtTypeParameter;
import com.cellinfo.controller.entity.FilterParameter;
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
	
	
	public TlGammaKernelExt saveKernelExt(TlGammaKernelExt ext, List<TlGammaKernelFilter> filterList)
	{		
		this.tlGammaKernelFilterRepository.saveAll(filterList);
		
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
	
	
	public void deleteKernelExt(String extGuid)
	{
		this.tlGammaKernelFilterRepository.deleteByExtGuid(extGuid);
		this.tlGammaKernelGeoFilterRepository.deleteByExtGuid(extGuid);
		this.tlGammaKernelExtRepository.deleteById(extGuid);
		
	}
	
	public Optional<TlGammaKernelAttr> getKernelAttr(String attrguid)
	{
		return this.tlGammaKernelAttrRepository.findById(attrguid);
	}


	public Page<ExtTypeParameter> getKernelExtList(String kernelClassid,String userName,Pageable pageable) {
		List<ExtTypeParameter> subTypeList = new LinkedList<ExtTypeParameter>();
		Page<TlGammaKernelExt> extList = this.tlGammaKernelExtRepository.findByKernelClassidAndUserName(kernelClassid,userName,pageable);
		if(extList!= null && extList.getSize()>0)
		{
			for(TlGammaKernelExt ext: extList.getContent())
			{
				ExtTypeParameter subtype = new ExtTypeParameter();
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
		
		return new PageImpl<ExtTypeParameter>(subTypeList,pageable,extList.getTotalElements());
	}


	public List<ExtTypeParameter> getKernelExtList(String kernelClassid, String userName) {
		List<ExtTypeParameter> subTypeList = new LinkedList<ExtTypeParameter>();
		List<TlGammaKernelExt> extList = this.tlGammaKernelExtRepository.findByKernelClassidAndUserName(kernelClassid,userName);
		if(extList!= null && extList.size()>0)
		{
			for(TlGammaKernelExt ext: extList)
			{
				ExtTypeParameter subtype = new ExtTypeParameter();
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
