package com.cellinfo.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaKernelFilter;
import com.cellinfo.entity.TlGammaKernelGeoFilter;
import com.cellinfo.entity.TlGammaTaskKernel;
import com.cellinfo.entity.TlGammaTaskRefkernel;
import com.cellinfo.entity.ViewLayerKernel;
import com.cellinfo.repository.TlGammaKernelAttrRepository;
import com.cellinfo.repository.TlGammaKernelFilterRepository;
import com.cellinfo.repository.TlGammaKernelGeoFilterRepository;
import com.cellinfo.repository.TlGammaKernelRepository;
import com.cellinfo.repository.TlGammaLayerAttributeRepository;
import com.cellinfo.repository.TlGammaLayerLineRepository;
import com.cellinfo.repository.TlGammaLayerPointRepository;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;
import com.cellinfo.repository.TlGammaTaskKernelRepository;
import com.cellinfo.repository.TlGammaTaskRefkernelRepository;
import com.cellinfo.repository.ViewLayerKernelRepository;
import com.vividsolutions.jts.geom.Polygon;

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
	private TlGammaTaskRefkernelRepository tlGammaTaskRefkernelRepository;
	
	@Autowired
	private TlGammaKernelFilterRepository tlGammaKernelFilterRepository;
	
	@Autowired
	private TlGammaKernelGeoFilterRepository tlGammaKernelGeoFilterRepository;
	
	@Autowired
	private ViewLayerKernelRepository viewLayerKernelRepository;
	
	@Autowired
	private TlGammaLayerAttributeRepository tlGammaLayerAttributeRepository;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
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
	
	public Map<String,String> transferTaskKernelList(String taskGuid,String kernelClassid,Integer grade)
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
					if(grade == 1)
						this.tlGammaTaskKernelRepository.save(new TlGammaTaskKernel(taskGuid,item.getKernelGuid()));
					else
						this.tlGammaTaskRefkernelRepository.save(new TlGammaTaskRefkernel(taskGuid,item.getKernelGuid()));
				});
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("end transfer --------"+ System.currentTimeMillis());
		msgList.put("recordnum", String.valueOf(recordNum));
		return msgList;
	}
	
	public Map<String,String> transferTaskKernelList(String taskGuid,String kernelClassid,String extGuid,Integer grade)
	{
		System.out.println("start transfer extGuid--------"+ System.currentTimeMillis());
		long recordNum =0;
		Map<String,String> msgList = new HashMap<String,String>();
		TlGammaKernel kernel = this.getByKernelClassid(kernelClassid);
		
		Polygon filterGeom = null;
		List<TlGammaKernelFilter> filterList = this.tlGammaKernelFilterRepository.findByExtGuid(extGuid);
		List<TlGammaKernelGeoFilter> geofilterList = this.tlGammaKernelGeoFilterRepository.findByExtGuid(extGuid);
		HashSet<String> geoKenelList = new HashSet<String>();
		if(geofilterList!=null && geofilterList.size()>0)
		{
			filterGeom =  geofilterList.get(0).getFilterGeom();
			if(filterGeom!= null)
			{
				switch(kernel.getGeomType()) {
				case "POINT" :
					geoKenelList = new HashSet(this.tlGammaLayerPointRepository.getDataByGeoFilter(kernelClassid, filterGeom));
					break;
				case "LINE" :
					geoKenelList = new HashSet(this.tlGammaLayerLineRepository.getDataByGeoFilter(kernelClassid, filterGeom));
					break;
				case "POLYGON" :
					geoKenelList = new HashSet(this.tlGammaLayerPolygonRepository.getDataByGeoFilter(kernelClassid, filterGeom));
					break;
				}
			}
		}
		for(TlGammaKernelFilter filter: filterList)
		{
			List<String> condList = new ArrayList<String>();
			if(filter.getAttrType().equalsIgnoreCase("INTEGER"))
			{
				Long minvalue =0L,maxvalue =0L;
				try
				{
					minvalue = Long.parseLong(filter.getMinValue());
					maxvalue = Long.parseLong(filter.getMaxValue());
				}catch(Exception e) {}
				if(minvalue > 0 && maxvalue > 0) {
					condList = this.tlGammaLayerAttributeRepository.betweenLongValue(filter.getAttrGuid(), minvalue, maxvalue);
				}else if(minvalue>0) {
					condList = this.tlGammaLayerAttributeRepository.gtLongValue(filter.getAttrGuid(), minvalue);
				}else if(maxvalue>0) {
					condList = this.tlGammaLayerAttributeRepository.ltLongValue(filter.getAttrGuid(), maxvalue);
				}
			}
			if(filter.getAttrType().equalsIgnoreCase("DOUBLE"))
			{
				Double minvalue =0D,maxvalue =0D;
				try
				{
					minvalue = Double.parseDouble(filter.getMinValue());
					maxvalue = Double.parseDouble(filter.getMaxValue());
				}catch(Exception e) {}
				if(minvalue > 0 && maxvalue > 0) {
					condList = this.tlGammaLayerAttributeRepository.betweenDoubleValue(filter.getAttrGuid(), minvalue, maxvalue);
				}else if(minvalue>0) {
					condList = this.tlGammaLayerAttributeRepository.gtDoubleValue(filter.getAttrGuid(), minvalue);
				}else if(maxvalue>0) {
					condList = this.tlGammaLayerAttributeRepository.ltDoubleValue(filter.getAttrGuid(), maxvalue);
				}
			}
			if(filter.getAttrType().equalsIgnoreCase("DATETIME"))
			{
				Timestamp minvalue = null;
				Timestamp maxvalue = null;
				try
				{
					minvalue = new Timestamp(df.parse(filter.getMinValue()).getTime());
					maxvalue = new Timestamp(df.parse(filter.getMaxValue()).getTime());
				}catch(Exception e) {}
				if(minvalue != null && maxvalue !=null)
				{
					condList = this.tlGammaLayerAttributeRepository.betweenTimeStampValue(filter.getAttrGuid(), minvalue, maxvalue);
				}else if(minvalue != null) {
					condList = this.tlGammaLayerAttributeRepository.gtTimeStampValue(filter.getAttrGuid(), minvalue);
				}else if(maxvalue !=null) {
					condList = this.tlGammaLayerAttributeRepository.ltTimeStampValue(filter.getAttrGuid(), maxvalue);
				}
			}
			if(filter.getAttrType().equalsIgnoreCase("STRING"))
			{
				if(filter.getMinValue()!=null && filter.getMinValue().length()>0)
					condList = this.tlGammaLayerAttributeRepository.likeStringValue(filter.getAttrGuid(), filter.getMinValue());
			}
			//如果有一条件返回结果为空
			if(condList == null || condList.size()<1)
			{
				geoKenelList = new HashSet<String>();
				break;
			}
			if(geoKenelList !=null &&  geoKenelList.size()>0)
			{
				System.out.println("start transfer merge---"+geoKenelList.size()+"  "+condList.size()+"-----"+ System.currentTimeMillis());
				HashSet<String> tmpList = geoKenelList;
				geoKenelList = new HashSet(condList.parallelStream().filter(item -> tmpList.contains(item)).collect(Collectors.toList()));
				
				System.out.println("end transfer merge---"+geoKenelList.size()+"  "+condList.size()+"-----"+ System.currentTimeMillis());
			}
			else 
			{
				geoKenelList = new HashSet(condList);
			}
		}

		try {
			geoKenelList.stream().forEach(item->{
				if(grade == 1)
					this.tlGammaTaskKernelRepository.save(new TlGammaTaskKernel(taskGuid,item));
				else
					this.tlGammaTaskRefkernelRepository.save(new TlGammaTaskRefkernel(taskGuid,item));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		recordNum = geoKenelList.size();
		msgList.put("recordnum", String.valueOf(recordNum));
		System.out.println("end transfer extGuid---recordnum-+"+recordNum+"--"+ System.currentTimeMillis());
		return msgList;
	}
}