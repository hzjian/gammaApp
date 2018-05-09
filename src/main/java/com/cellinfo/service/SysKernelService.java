package com.cellinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.repository.TlGammaKernelAttrRepository;
import com.cellinfo.repository.TlGammaKernelRepository;

@Service
public class SysKernelService {

	@Autowired
	private TlGammaKernelRepository tlGammaKernelRepository ;
	
	@Autowired
	private TlGammaKernelAttrRepository tlGammaKernelAttrRepository;
	
	public List<TlGammaKernelAttr>  getKernelAttrList(String kernelClassid)
	{
		return this.tlGammaKernelAttrRepository.findByKernelClassid(kernelClassid);
	}
	
	public Iterable<TlGammaKernelAttr>  saveKernelAttr(List<TlGammaKernelAttr> entities)
	{
		return this.tlGammaKernelAttrRepository.save(entities);
	}
	
	public TlGammaKernelAttr  saveKernelAttr(TlGammaKernelAttr entity)
	{
		return this.tlGammaKernelAttrRepository.save(entity);
	}
	
	public TlGammaKernel addGroupKernel(TlGammaKernel entity)
	{
		return this.tlGammaKernelRepository.save(entity);
	}
	
	public TlGammaKernel updateGroupKernel(TlGammaKernel entity)
	{
		return this.tlGammaKernelRepository.save(entity);
	}
	
	public String  deleteGroupKernel(String kernelGuid)
	{
		try {
			this.tlGammaKernelRepository.delete(kernelGuid);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "删除失败";
		}
		return "删除成功";
	}
	
	public Page<TlGammaKernel> getGroupKernelList(String groupGuid,PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelRepository.findByGroupGuid(groupGuid,pageInfo);
	}

	public TlGammaKernel getByKernelClassid(String kernelClassid) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelRepository.findOne(kernelClassid);
	}

	public List<TlGammaKernel> getByKernelClassname(String kernelClassname) {
		return this.tlGammaKernelRepository.findByKernelClassname(kernelClassname);
	}
}
