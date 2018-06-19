package com.cellinfo.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaDict;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.ViewKernel;
import com.cellinfo.entity.ViewKernelAttr;
import com.cellinfo.repository.TlGammaDictRepository;
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
import com.cellinfo.repository.ViewKernelAttrRepository;
import com.cellinfo.repository.ViewKernelRepository;
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
	
	@Autowired
	private ViewKernelAttrRepository viewKernelAttrRepository;
	
	@Autowired
	private TlGammaDictRepository tlGammaDictRepository;
	
	@Autowired
	private ViewKernelRepository viewKernelRepository;
	
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
	
	public Page<ViewKernel> getGroupKernelList(String groupGuid,String key,PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.viewKernelRepository.findByGroupGuid(groupGuid,key,pageInfo);
	}

	public Optional<ViewKernel> getViewKernelByKernelClassid(String kernelClassid) {
		// TODO Auto-generated method stub
		return this.viewKernelRepository.findById(kernelClassid);
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

	public Page<TlGammaKernelAttr> getKernelAttrList(String classId,String userName, String filterStr,Pageable pageable) {
		// TODO Auto-generated method stub
		Page<TlGammaKernelAttr> attrList = null;
		if(filterStr!= null && filterStr.length()>0)
			attrList = this.tlGammaKernelAttrRepository.getKernelAttrList(classId,userName,filterStr,pageable);
		else
			attrList = this.tlGammaKernelAttrRepository.getKernelAttrList(classId,userName,pageable);
		return attrList;
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

	public List<TlGammaKernel> getByKernelClassnameExclude(String classId, String className) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelRepository.getByKernelClassnameExclude(classId,className);
	}
	
	public List<Map<String,String>> getUserRelateKernelList(String userName)
	{
		List<Map<String,String>> kernelList = new LinkedList<Map<String,String>>();
		Map<String,String> kernels = new HashMap<String,String>();
		List<ViewKernelAttr> viewkernellist = this.viewKernelAttrRepository.findByUserName(userName);
		
		for(ViewKernelAttr attr : viewkernellist)
		{
			if(!kernels.containsKey(attr.getKernelClassid()))
			{
				kernels.put(attr.getKernelClassid(), attr.getKernelClassname());
				Map<String,String> fieldMap = new HashMap<String,String>();
				fieldMap.put("classId", attr.getKernelClassid());
				fieldMap.put("className", attr.getKernelClassname());
				kernelList.add(fieldMap);
			}
		}
		return kernelList;
	}
	
	public List<Map<String,String>> getUserRelateAttrList(String classId,String userName)
	{
		List<Map<String,String>> attrList = new LinkedList<Map<String,String>>();
		List<ViewKernelAttr> viewkernellist = this.viewKernelAttrRepository.findByKernelClassidAndUserName(classId,userName);
		
		for(ViewKernelAttr item : viewkernellist)
		{
			Map<String,String> fieldMap = new HashMap<String,String>();
			
			fieldMap.put("attrId", item.getAttrGuid());
			fieldMap.put("attrName",item.getAttrName());
			fieldMap.put("attrType",item.getAttrType());
			fieldMap.put("attrFgrade",item.getAttrFgrade());
			fieldMap.put("attrDesc",item.getAttrDesc());
			fieldMap.put("minValue",item.getAttrMin());
			fieldMap.put("maxValue",item.getAttrMax());
			fieldMap.put("shareGrade", item.getShareGrade());
			if(item.getDictId()!=null)
			{
				Optional<TlGammaDict> opDict = this.tlGammaDictRepository.findById(item.getDictId());
				if(opDict.isPresent())
				{
					fieldMap.put("dictName",opDict.get().getDictName());
					fieldMap.put("dictId",item.getDictId());
				}
			}
			attrList.add(fieldMap);
		}
		return attrList;
	}

	public Optional<TlGammaKernel> getByKernelClassid(String classId) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelRepository.findById(classId);
	}
}