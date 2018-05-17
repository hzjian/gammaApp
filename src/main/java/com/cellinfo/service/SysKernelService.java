package com.cellinfo.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaKernelFilter;
import com.cellinfo.entity.TlGammaKernelGeoFilter;
import com.cellinfo.entity.TlGammaTaskKernel;
import com.cellinfo.entity.ViewLayerKernel;
import com.cellinfo.repository.TlGammaKernelAttrRepository;
import com.cellinfo.repository.TlGammaKernelFilterRepository;
import com.cellinfo.repository.TlGammaKernelGeoFilterRepository;
import com.cellinfo.repository.TlGammaKernelRepository;
import com.cellinfo.repository.TlGammaLayerLineRepository;
import com.cellinfo.repository.TlGammaLayerPointRepository;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;
import com.cellinfo.repository.TlGammaTaskKernelRepository;
import com.cellinfo.repository.ViewLayerKernelRepository;
import com.vividsolutions.jts.geom.MultiPolygon;

@Service
public class SysKernelService {

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
	private TlGammaTaskKernelRepository tlGammaTaskKernelRepository;
	
	@Autowired
	private TlGammaKernelFilterRepository tlGammaKernelFilterRepository;
	
	@Autowired
	private TlGammaKernelGeoFilterRepository tlGammaKernelGeoFilterRepository;
	
	@Autowired
	private ViewLayerKernelRepository viewLayerKernelRepository;
	
	private final static int MAX_RECORD = 9999;
	
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
	
	public Map<String,String> transferTaskKernelList(String taskGuid,String kernelClassid)
	{
		long recordNum =0;
		int exeNum=0;
		Map<String,String> msgList = new HashMap<String,String>();
		//TlGammaKernel kernel = this.getByKernelClassid(kernelClassid);
		
		System.out.println("start transfer --------"+ System.currentTimeMillis());
		recordNum = this.viewLayerKernelRepository.countByKernelClassid(kernelClassid);
		exeNum = Double.valueOf(Math.ceil(recordNum/MAX_RECORD)).intValue();
		for(int i =0;i<exeNum;i++)
		{
			PageRequest page = new PageRequest(i,this.MAX_RECORD);
			CompletableFuture<List<ViewLayerKernel>> pointList = this.viewLayerKernelRepository.findByKernelClassid(kernelClassid,page);
			try {
				pointList.get().parallelStream().forEach(item->{
					this.tlGammaTaskKernelRepository.save(new TlGammaTaskKernel(taskGuid,item.getKernelGuid()));
				});
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("end transfer --------"+ System.currentTimeMillis());
//		switch(kernel.getGeomType()) {
//		case "POINT" :
//			recordNum = this.tlGammaLayerPointRepository.countByKernelClassid(kernelClassid);
//			exeNum = Double.valueOf(Math.ceil(recordNum/MAX_RECORD)).intValue();
//			for(int i =0;i<exeNum;i++)
//			{
//				PageRequest page = new PageRequest(i,this.MAX_RECORD);
//				List<TlGammaLayerPoint> pointList = this.tlGammaLayerPointRepository.findByKernelClassid(kernelClassid,page);
//				pointList.parallelStream().forEach(item->{
//					this.tlGammaTaskKernelRepository.save(new TlGammaTaskKernel(taskGuid,item.getKernelGuid()));
//				});
//			}
//			break;
//		case "LINE" :
//			recordNum = this.tlGammaLayerLineRepository.countByKernelClassid(kernelClassid);
//			exeNum = Double.valueOf(Math.ceil(recordNum/MAX_RECORD)).intValue();
//			for(int i =0;i<exeNum;i++)
//			{
//				PageRequest page = new PageRequest(i,this.MAX_RECORD);
//				List<TlGammaLayerLine> lineList = this.tlGammaLayerLineRepository.findByKernelClassid(kernelClassid,page);
//				lineList.parallelStream().forEach(item->{
//					this.tlGammaTaskKernelRepository.save(new TlGammaTaskKernel(taskGuid,item.getKernelGuid()));
//				});
//			}
//			break;
//		case "POLYGON" :
//			recordNum = this.tlGammaLayerPolygonRepository.countByKernelClassid(kernelClassid);
//			exeNum = Double.valueOf(Math.ceil(recordNum/MAX_RECORD)).intValue();
//			for(int i =0;i<exeNum;i++)
//			{
//				PageRequest page = new PageRequest(i,this.MAX_RECORD);
//				List<TlGammaLayerPolygon> lineList = this.tlGammaLayerPolygonRepository.findByKernelClassid(kernelClassid,page);
//				lineList.parallelStream().forEach(item->{
//					this.tlGammaTaskKernelRepository.save(new TlGammaTaskKernel(taskGuid,item.getKernelGuid()));
//				});
//			}
//			break;
//		}
		msgList.put("recordnum", String.valueOf(recordNum));
		return msgList;
	}
	
	public Map<String,String> transferTaskKernelList(String taskGuid,String kernelClassid,String extGuid)
	{
		long recordNum =0;
		Map<String,String> msgList = new HashMap<String,String>();
		TlGammaKernel kernel = this.getByKernelClassid(kernelClassid);
		
		MultiPolygon filterGeom = null;
		List<TlGammaKernelFilter> filterList = this.tlGammaKernelFilterRepository.findByExtGuid(extGuid);
		List<TlGammaKernelGeoFilter> geofilterList = this.tlGammaKernelGeoFilterRepository.findByExtGuid(extGuid);
		List<String> geoKenelList = new LinkedList<String>();
		if(geofilterList!=null && geofilterList.size()>0)
		{
			filterGeom =  geofilterList.get(0).getFilterGeom();
			if(filterGeom!= null)
			{
				switch(kernel.getGeomType()) {
				case "POINT" :
					geoKenelList = this.tlGammaLayerPointRepository.getDataByGeoFilter(kernelClassid, filterGeom);
					break;
				case "LINE" :
					geoKenelList = this.tlGammaLayerLineRepository.getDataByGeoFilter(kernelClassid, filterGeom);
					break;
				case "POLYGON" :
					geoKenelList = this.tlGammaLayerPolygonRepository.getDataByGeoFilter(kernelClassid, filterGeom);
					break;
				}
			}
			
		}
		List<String> condList = new LinkedList<String>();
		for(TlGammaKernelFilter filter: filterList)
		{
			if(filter.getAttrType().equalsIgnoreCase("INTEGER"))
			{
				if(filter.getMaxValue()!=null && filter.getMaxValue().length()>0)
					condList.add("attrGuid = "+filter.getAttrGuid()+" and attrLong < "+filter.getMaxValue());
				if(filter.getMinValue()!=null && filter.getMinValue().length()>0)
					condList.add("attrGuid = "+filter.getAttrGuid()+" and attrLong > "+filter.getMinValue());
			}
			if(filter.getAttrType().equalsIgnoreCase("DOUBLE"))
			{
				if(filter.getMaxValue()!=null && filter.getMaxValue().length()>0)
					condList.add("attrGuid = "+filter.getAttrGuid()+" and attrDouble < "+filter.getMaxValue());
				if(filter.getMinValue()!=null && filter.getMinValue().length()>0)
					condList.add("attrGuid = "+filter.getAttrGuid()+" and attrDouble > "+filter.getMinValue());
			}
			if(filter.getAttrType().equalsIgnoreCase("DATETIME"))
			{
				if(filter.getMaxValue()!=null && filter.getMaxValue().length()>0)
					condList.add("attrGuid = "+filter.getAttrGuid()+" and attrTime < "+filter.getMaxValue());
				if(filter.getMinValue()!=null && filter.getMinValue().length()>0)
					condList.add("attrGuid = "+filter.getAttrGuid()+" and attrTime > "+filter.getMinValue());
			}
			if(filter.getAttrType().equalsIgnoreCase("STRING"))
			{
				if(filter.getMinValue()!=null && filter.getMinValue().length()>0)
					condList.add("attrGuid = "+filter.getAttrGuid()+" and attrText like %"+filter.getMinValue().trim()+"%");
			}
		}
		
		
		
		msgList.put("recordnum", String.valueOf(recordNum));
		return msgList;
	}
}