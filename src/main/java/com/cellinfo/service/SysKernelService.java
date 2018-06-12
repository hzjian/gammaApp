package com.cellinfo.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.repository.TlGammaKernelAttrRepository;
import com.cellinfo.repository.TlGammaKernelFilterRepository;
import com.cellinfo.repository.TlGammaKernelGeoFilterRepository;
import com.cellinfo.repository.TlGammaKernelRepository;
import com.cellinfo.repository.TlGammaKernelSubsetRepository;
import com.cellinfo.repository.TlGammaLayerAttributeRepository;
import com.cellinfo.repository.TlGammaLayerLineRepository;
import com.cellinfo.repository.TlGammaLayerPointRepository;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;
import com.cellinfo.repository.TlGammaTaskTmpRepository;
import com.cellinfo.repository.ViewLayerKernelRepository;

@Service
public class SysKernelService {

	private static Logger logger = LoggerFactory.getLogger(SysKernelService.class);
	@Autowired
	private TlGammaKernelRepository tlGammaKernelRepository ;
	
	@Autowired
	private TlGammaKernelAttrRepository tlGammaKernelAttrRepository;
	
	@Autowired
	private TlGammaLayerLineRepository tlGammaLayerLineRepository;
	
	@Autowired
	private TlGammaLayerPointRepository tlGammaLayerPointRepository;
	
	@Autowired
	private TlGammaLayerPolygonRepository tlGammaLayerPolygonRepository;
	
	@Autowired
	private TlGammaKernelSubsetRepository tlGammaKernelSubsetRepository;
	
	@Autowired
	private TlGammaKernelFilterRepository tlGammaKernelFilterRepository;
	
	@Autowired
	private TlGammaKernelGeoFilterRepository tlGammaKernelGeoFilterRepository;
	
	@Autowired
	private ViewLayerKernelRepository viewLayerKernelRepository;
	
	@Autowired
	private TlGammaLayerAttributeRepository tlGammaLayerAttributeRepository;
	
	@Autowired
	private TlGammaTaskTmpRepository tlGammaTaskTmpRepository;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
	private final static int MAX_RECORD = 9999;
	
	public List<TlGammaKernelAttr>  getKernelAttrList(String kernelClassid)
	{
		return this.tlGammaKernelAttrRepository.findByKernelClassid(kernelClassid);
	}
	
	public Iterable<TlGammaKernelAttr>  saveKernelAttr(List<TlGammaKernelAttr> entities)
	{
		return this.tlGammaKernelAttrRepository.saveAll(entities);
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
			this.tlGammaKernelRepository.deleteById(kernelGuid);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "删除失败";
		}
		return "删除成功";
	}
	
	public Page<TlGammaKernel> getGroupKernelList(String groupGuid,String key,PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelRepository.findByGroupGuid(groupGuid,key,pageInfo);
	}

	public Optional<TlGammaKernel> getByKernelClassid(String kernelClassid) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelRepository.findById(kernelClassid);
	}

	public List<TlGammaKernel> getByKernelClassname(String kernelClassname) {
		return this.tlGammaKernelRepository.findByKernelClassname(kernelClassname);
	}

	public void deleteKernelAttr( String attrId) {
		// TODO Auto-generated method stub
		this.tlGammaKernelAttrRepository.deleteById(attrId);
	}

	public Optional<TlGammaKernelAttr> getAttrById(String attrId) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelAttrRepository.findById(attrId);
	}

	public void updateKernelAttr(TlGammaKernelAttr attr) {
		// TODO Auto-generated method stub
		this.tlGammaKernelAttrRepository.save(attr);
	}

	public Page<TlGammaKernelAttr> getKernelAttrList(String classId,String userName, String filterStr,Pageable pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelAttrRepository.getKernelAttrList(classId,userName,  filterStr,  pageInfo);
	}

	public List<TlGammaKernelAttr> getAttrByName(String classId,String attrName) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelAttrRepository.findAllByAttrName(classId,attrName);
	}

	public List<TlGammaKernelAttr> getTaskAttrAvalialble(String classId,String userName, String taskId) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelAttrRepository.getTaskAttrAvalialble(classId,userName, taskId);
	}

	public List<TlGammaKernel> getTaskKernelAvaliable(String groupId,String taskId) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelRepository.getTaskKernelAvaliable(groupId,taskId);
		
	}
}