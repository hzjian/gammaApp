package com.cellinfo.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.cellinfo.repository.TlGammaLayerAttributeRepository;
import com.cellinfo.repository.TlGammaLayerLineRepository;
import com.cellinfo.repository.TlGammaLayerPointRepository;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;
import com.cellinfo.repository.TlGammaTaskKernelRepository;
import com.cellinfo.repository.TlGammaTaskTmpRepository;
import com.cellinfo.repository.ViewLayerKernelRepository;
import com.vividsolutions.jts.geom.Polygon;

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
	private TlGammaTaskKernelRepository tlGammaTaskKernelRepository;
	
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
	
	public Page<TlGammaKernel> getGroupKernelList(String groupGuid,PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelRepository.findByGroupGuid(groupGuid,pageInfo);
	}

	public Optional<TlGammaKernel> getByKernelClassid(String kernelClassid) {
		// TODO Auto-generated method stub
		return this.tlGammaKernelRepository.findById(kernelClassid);
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
			PageRequest page = PageRequest.of(i,this.MAX_RECORD);
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
		msgList.put("recordnum", String.valueOf(recordNum));
		return msgList;
	}
	
	public Map<String,String> transferTaskKernelList(String taskGuid,String kernelClassid,String extGuid)
	{
		System.out.println("start transfer extGuid--------"+ System.currentTimeMillis());
		long recordNum =0;
		Map<String,String> msgList = new HashMap<String,String>();
		TlGammaKernel kernel = this.getByKernelClassid(kernelClassid).get();
		Integer filterNum = 0;
		Integer resultNum = 0;
		String tmpGuid = UUID.randomUUID().toString();
		List<TlGammaKernelFilter> filterList = this.tlGammaKernelFilterRepository.findByExtGuid(extGuid);
		
		try {
			List<TlGammaKernelGeoFilter> geofilterList = this.tlGammaKernelGeoFilterRepository.findByExtGuid(extGuid);
			if(geofilterList!=null && geofilterList.size()>0)
			{
				Polygon filterGeom =  geofilterList.get(0).getFilterGeom();
				if(filterGeom!= null)
				{
					switch(kernel.getGeomType()) {
					case "POINT" :
						resultNum = this.tlGammaLayerPointRepository.createGeoFilter(tmpGuid,kernelClassid, filterGeom);
						break;
					case "LINE" :
						resultNum = this.tlGammaLayerLineRepository.createGeoFilter(tmpGuid,kernelClassid, filterGeom);
						break;
					case "POLYGON" :
						resultNum = this.tlGammaLayerPolygonRepository.createGeoFilter(tmpGuid,kernelClassid, filterGeom);
						break;
					}
				}
				filterNum +=1;
				logger.warn("filterGeom num =="+resultNum);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(TlGammaKernelFilter filter: filterList)
		{
			Optional<TlGammaKernelAttr> kernelAtrr = this.tlGammaKernelAttrRepository.findById(filter.getAttrGuid());
			if(kernelAtrr.isPresent())
			{
				if(kernelAtrr.get().getAttrType().equalsIgnoreCase("INTEGER"))
				{
					Long minvalue = null,maxvalue = null;
					try
					{
						if(filter.getMinValue()!= null)
							minvalue = Long.parseLong(filter.getMinValue());
						if(filter.getMaxValue()!= null)
							maxvalue = Long.parseLong(filter.getMaxValue());
					}catch(Exception e) {}
					if(minvalue !=null && maxvalue !=null) {
						if(filterNum>0)
							resultNum = this.tlGammaLayerAttributeRepository.updateBetweenLongValue(tmpGuid,filterNum,filter.getAttrGuid(), minvalue, maxvalue);
						else
							resultNum = this.tlGammaLayerAttributeRepository.insertBetweenLongValue(tmpGuid,filter.getAttrGuid(), minvalue, maxvalue);
					}else if(minvalue != null) {
						if(filterNum>0)
							resultNum = this.tlGammaLayerAttributeRepository.updateGTLongValue(tmpGuid,filterNum,filter.getAttrGuid(), minvalue);
						else
							resultNum = this.tlGammaLayerAttributeRepository.insertGTLongValue(tmpGuid,filter.getAttrGuid(), minvalue);
					}else if(maxvalue != null) {
						this.tlGammaLayerAttributeRepository.updateLTLongValue(tmpGuid,filterNum,filter.getAttrGuid(), maxvalue);
					}
				}
				if(kernelAtrr.get().getAttrType().equalsIgnoreCase("DOUBLE"))
				{
					Double minvalue = null,maxvalue = null;
					try
					{
						if(filter.getMinValue()!= null)
							minvalue = Double.parseDouble(filter.getMinValue());
						if(filter.getMaxValue()!= null)
							maxvalue = Double.parseDouble(filter.getMaxValue());
					}catch(Exception e) {}
					if(minvalue !=null && maxvalue !=null) {
						if(filterNum>0)
							resultNum = this.tlGammaLayerAttributeRepository.updateBetweenDoubleValue(tmpGuid,filterNum,filter.getAttrGuid(), minvalue, maxvalue);
						else
							resultNum = this.tlGammaLayerAttributeRepository.insertBetweenDoubleValue(tmpGuid,filter.getAttrGuid(), minvalue, maxvalue);
					}else if(minvalue !=null) {
						if(filterNum>0)
							resultNum = this.tlGammaLayerAttributeRepository.updateGTDoubleValue(tmpGuid,filterNum,filter.getAttrGuid(), minvalue);
						else
							resultNum = this.tlGammaLayerAttributeRepository.insertGTDoubleValue(tmpGuid,filter.getAttrGuid(), minvalue);
					}else if(maxvalue !=null) {
						if(filterNum>0)
							resultNum = this.tlGammaLayerAttributeRepository.updateLTDoubleValue(tmpGuid,filterNum,filter.getAttrGuid(), maxvalue);
						else
							resultNum = this.tlGammaLayerAttributeRepository.insertLTDoubleValue(tmpGuid,filter.getAttrGuid(), maxvalue);
					}
				}
				if(kernelAtrr.get().getAttrType().equalsIgnoreCase("DATETIME"))
				{
					Timestamp minvalue = null;
					Timestamp maxvalue = null;
					try
					{
						if(filter.getMinValue()!= null)
							minvalue = new Timestamp(df.parse(filter.getMinValue()).getTime());
						if(filter.getMaxValue()!= null)
							maxvalue = new Timestamp(df.parse(filter.getMaxValue()).getTime());
					}catch(Exception e) {}
					if(minvalue != null && maxvalue !=null)
					{
						if(filterNum>0)
							resultNum = this.tlGammaLayerAttributeRepository.updateBetweenTimeStampValue(tmpGuid,filterNum,filter.getAttrGuid(), minvalue, maxvalue);
						else
							resultNum = this.tlGammaLayerAttributeRepository.insertBetweenTimeStampValue(tmpGuid,filter.getAttrGuid(), minvalue, maxvalue);
					}else if(minvalue != null) {
						if(filterNum>0)
							resultNum = this.tlGammaLayerAttributeRepository.updateGTTimeStampValue(tmpGuid,filterNum,filter.getAttrGuid(), minvalue);
						else
							resultNum = this.tlGammaLayerAttributeRepository.insertGTTimeStampValue(tmpGuid,filter.getAttrGuid(), minvalue);
					}else if(maxvalue !=null) {
						if(filterNum>0)
							resultNum = this.tlGammaLayerAttributeRepository.updateLTTimeStampValue(tmpGuid,filterNum,filter.getAttrGuid(), maxvalue);
						else
							resultNum = this.tlGammaLayerAttributeRepository.insertLTTimeStampValue(tmpGuid,filter.getAttrGuid(), maxvalue);
					}
				}
				if(kernelAtrr.get().getAttrType().equalsIgnoreCase("STRING"))
				{
					if(filter.getMinValue()!=null && filter.getMinValue().length()>0)
					{
						if(filterNum>0)
							resultNum = this.tlGammaLayerAttributeRepository.updateLikeStringValue(tmpGuid,filterNum,filter.getAttrGuid(), filter.getMinValue());
						else
							resultNum = this.tlGammaLayerAttributeRepository.insertLikeStringValue(tmpGuid,filter.getAttrGuid(), filter.getMinValue());
					}
				}
				filterNum +=1;
			}
			logger.warn("property filter num =="+resultNum);
		}
		try 
		{
			resultNum = this.tlGammaTaskKernelRepository.createTaskFilter(taskGuid, tmpGuid, filterNum);

			logger.warn("insert task num =="+taskGuid+"  "+resultNum);
			///TODO
			///DELETE TMP TABLE RECORDS
			this.tlGammaTaskTmpRepository.deleteTaskTmp(tmpGuid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		msgList.put("recordnum", String.valueOf(recordNum));
		System.out.println("end transfer extGuid---recordnum-+"+recordNum+"--"+ System.currentTimeMillis());
		return msgList;
	}

	public void deleteKernelAttr( String attrId) {
		// TODO Auto-generated method stub
		this.tlGammaKernelAttrRepository.deleteById(attrId);
	}
}