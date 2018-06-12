package com.cellinfo.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.controller.entity.GAExtParameter;
import com.cellinfo.controller.entity.GAFilter;
import com.cellinfo.controller.entity.GAGeoFilter;
import com.cellinfo.entity.TlGammaLayerAttribute;
import com.cellinfo.entity.TlGammaLayerLine;
import com.cellinfo.entity.TlGammaLayerPoint;
import com.cellinfo.entity.TlGammaLayerPolygon;
import com.cellinfo.entity.ViewTaskLine;
import com.cellinfo.entity.ViewTaskPoint;
import com.cellinfo.entity.ViewTaskPolygon;
import com.cellinfo.repository.TlGammaKernelSubsetRepository;
import com.cellinfo.repository.TlGammaLayerAttributeRepository;
import com.cellinfo.repository.TlGammaLayerLineRepository;
import com.cellinfo.repository.TlGammaLayerPointRepository;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;
import com.cellinfo.repository.TlGammaTaskTmpRepository;
import com.cellinfo.repository.ViewTaskLineRepository;
import com.cellinfo.repository.ViewTaskPointRepository;
import com.cellinfo.repository.ViewTaskPolygonRepository;
import com.vividsolutions.jts.geom.Geometry;

@Service
public class SysDataService {
 
	private final static int  MAX_LOADELEMENT_NUM = 2000;
	
	private static Logger logger = LoggerFactory.getLogger(SysDataService.class);
	
	@Autowired
	private TlGammaKernelSubsetRepository tlGammaKernelSubsetRepository;
	
	@Autowired
	private TlGammaTaskTmpRepository tlGammaTaskTmpRepository;
	
	@Autowired
	private ViewTaskPointRepository viewTaskPointRepository;
	
	@Autowired
	private ViewTaskLineRepository viewTaskLineRepository;
	
	@Autowired
	private ViewTaskPolygonRepository viewTaskPolygonRepository;
	
	@Autowired
	private TlGammaLayerAttributeRepository tlGammaLayerAttributeRepository;

	@Autowired
	private TlGammaLayerPolygonRepository tlGammaLayerPolygonRepository;
	
	@Autowired
	private TlGammaLayerLineRepository tlGammaLayerLineRepository;
	
	@Autowired
	private TlGammaLayerPointRepository tlGammaLayerPointRepository;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void savePontGeom(TlGammaLayerPoint entity)
	{
		this.tlGammaLayerPointRepository.save(entity);
		
	}
	public void saveLineGeom(TlGammaLayerLine entity)
	{
		this.tlGammaLayerLineRepository.save(entity);
		
	}
	public void savePolygonGeom(TlGammaLayerPolygon entity)
	{
		this.tlGammaLayerPolygonRepository.save(entity);
	}

	public List<Map<String, Object>> getKernelData(String kernelClassId,String geomType,Geometry filterGeom) {
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		switch (geomType) {
			case "POINT":
				geoList = this.getPointGeojsonList(kernelClassId,filterGeom);
				break;
			case "LINE":
				geoList = this.getLineGeojsonList(kernelClassId,filterGeom);
				break;
			case "POLYGON":
				geoList = this.getPolygonGeojsonList(kernelClassId,filterGeom);
				break;
		}		return geoList;
	}
	
	public Long getKernelDataCount(String classId, String geomType, Geometry filterGeom) {
		Long dataCount =0L;
		switch (geomType) {
		case "POINT":
			dataCount = this.tlGammaLayerPointRepository.getDataCountByFilter(classId, filterGeom);
			break;
		case "LINE":
			dataCount = this.tlGammaLayerLineRepository.getDataCountByFilter(classId, filterGeom);
			break;
		case "POLYGON":
			dataCount = this.tlGammaLayerPolygonRepository.getDataCountByFilter(classId, filterGeom);
			break;
		}	
		return dataCount;
	}
	
	private  List<Map<String, Object>> getPointGeojsonList(String kernelClassId,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, MAX_LOADELEMENT_NUM);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<TlGammaLayerPoint> pointlist = this.tlGammaLayerPointRepository.getDataByFilter(kernelClassId,filterGeom, pageInfo);

		for (TlGammaLayerPoint eachGeom : pointlist) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			propMap.put("id",eachGeom.getKernelId());
			
			feaMap.put("type", "Feature");
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		return geoList;
	}

	private  List<Map<String, Object>> getLineGeojsonList(String kernelClassId,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, MAX_LOADELEMENT_NUM);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<TlGammaLayerLine> linelist = this.tlGammaLayerLineRepository.getDataByFilter(kernelClassId,filterGeom, pageInfo);

		for (TlGammaLayerLine eachGeom : linelist) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			propMap.put("id",eachGeom.getKernelId());
			
			feaMap.put("type", "Feature");
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		return geoList;
	}
	
	private  List<Map<String, Object>> getPolygonGeojsonList(String kernelClassId,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, MAX_LOADELEMENT_NUM);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<TlGammaLayerPolygon> polygonlist = this.tlGammaLayerPolygonRepository.getDataByFilter(kernelClassId,filterGeom, pageInfo);

		for (TlGammaLayerPolygon eachGeom : polygonlist) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			propMap.put("id",eachGeom.getKernelId());
			
			feaMap.put("type", "Feature");
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		return geoList;
	}
	
	public List<Map<String, Object>> getLayerData(String kernelClassId,String extGuid,String geomType,Geometry filterGeom) {
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		switch (geomType) {
			case "POINT":
				geoList = this.getExtPointGeojsonList(kernelClassId,extGuid,filterGeom);
				break;
			case "LINE":
				geoList = this.getExtLineGeojsonList(kernelClassId,extGuid,filterGeom);
				break;
			case "POLYGON":
				geoList = this.getExtPolygonGeojsonList(kernelClassId,extGuid,filterGeom);
				break;
		}		return geoList;
	}
	
	public Long getLayerDataCount(String classId, String extGuid, String geomType, Geometry filterGeom) {
		// TODO Auto-generated method stub
		Long dataCount =0L;
		switch (geomType) {
		case "POINT":
			dataCount = this.viewTaskPointRepository.getDataCountByFilter(classId, extGuid, filterGeom);
			break;
		case "LINE":
			dataCount = this.viewTaskLineRepository.getDataCountByFilter(classId, extGuid, filterGeom);
			break;
		case "POLYGON":
			dataCount = this.viewTaskPolygonRepository.getDataCountByFilter(classId, extGuid, filterGeom);
			break;
		}	
		return dataCount;
	}
	private  List<Map<String, Object>> getExtPointGeojsonList(String kernelClassId,String extGuid,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, MAX_LOADELEMENT_NUM);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<ViewTaskPoint> pointlist = this.viewTaskPointRepository.getDataByFilter(kernelClassId,extGuid,filterGeom, pageInfo);

		for (ViewTaskPoint eachGeom : pointlist) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			propMap.put("id",eachGeom.getKernelId());
			
			feaMap.put("type", "Feature");
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		return geoList;
	}

	private  List<Map<String, Object>> getExtLineGeojsonList(String kernelClassId,String extGuid,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, MAX_LOADELEMENT_NUM);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<ViewTaskLine> linelist = this.viewTaskLineRepository.getDataByFilter(kernelClassId,extGuid,filterGeom, pageInfo);

		for (ViewTaskLine eachGeom : linelist) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			propMap.put("id",eachGeom.getKernelId());
			
			feaMap.put("type", "Feature");
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		return geoList;
	}
	
	private  List<Map<String, Object>> getExtPolygonGeojsonList(String kernelClassId,String extGuid,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, MAX_LOADELEMENT_NUM);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<ViewTaskPolygon> polygonlist = this.viewTaskPolygonRepository.getDataByFilter(kernelClassId,extGuid,filterGeom, pageInfo);

		for (ViewTaskPolygon eachGeom : polygonlist) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			propMap.put("id",eachGeom.getKernelId());
			
			feaMap.put("type", "Feature");
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		return geoList;
	}
	
	public  List<TlGammaLayerAttribute> getKernelAttrByGuid(String kernerlGuid)
	{
		return this.tlGammaLayerAttributeRepository.getKernelAttribute(kernerlGuid);
	}
	
	public  Iterable<TlGammaLayerAttribute> saveAttribute(List<TlGammaLayerAttribute> entities)
	{
		return this.tlGammaLayerAttributeRepository.saveAll(entities);
	}
	public List<TlGammaLayerAttribute> getKernelAttrById(String kernerlGuid,String attrGuid)
	{
		return this.tlGammaLayerAttributeRepository.getKernelAttribute(kernerlGuid, attrGuid);
	}
	
	public List<TlGammaLayerAttribute> getKernelAttrByTaskId(String kernerlGuid,String attrGuid,String taskGuid)
	{
		return this.tlGammaLayerAttributeRepository.getKernelAttribute(kernerlGuid, attrGuid, taskGuid);
	}
	
	public List<TlGammaLayerAttribute> getKernelAttrByUsername(String kernerlGuid,String attrGuid,String taskGuid,String userName)
	{
		return this.tlGammaLayerAttributeRepository.getKernelAttribute(kernerlGuid, attrGuid, taskGuid,userName);
	}

	public Map<String,String> createExtKernelList(GAExtParameter ext)
	{
		System.out.println("start transfer extGuid--------"+ System.currentTimeMillis());
		long recordNum =0;
		Map<String,String> msgList = new HashMap<String,String>();
		Integer filterNum = 0;
		Integer resultNum = 0;
		String tmpGuid = UUID.randomUUID().toString();
		List<GAFilter> filterList = ext.getFilterList();
		try {
			List<GAGeoFilter> geofilterList = ext.getGeofilterList();
			if(geofilterList!=null && geofilterList.size()>0)
			{
				Geometry filterGeom = null;
				try {
					String rangeStr = JSONValue.toJSONString(geofilterList.get(0).getFilterGeom());
					filterGeom = new GeometryJSON(10).read(rangeStr);
					filterGeom.setSRID(4326);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(filterGeom!= null)
				{
					switch(ext.getGeoType()) {
					case "POINT" :
						resultNum = this.tlGammaLayerPointRepository.createGeoFilter(tmpGuid,ext.getKernelClassid(), filterGeom);
						break;
					case "LINE" :
						resultNum = this.tlGammaLayerLineRepository.createGeoFilter(tmpGuid,ext.getKernelClassid(), filterGeom);
						break;
					case "POLYGON" :
						resultNum = this.tlGammaLayerPolygonRepository.createGeoFilter(tmpGuid,ext.getKernelClassid(), filterGeom);
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
		
		for(GAFilter filter: filterList)
		{
			if(filter.getAttrType()!=null )
			{
				if(filter.getAttrType().equalsIgnoreCase("INTEGER"))
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
				if(filter.getAttrType().equalsIgnoreCase("DOUBLE"))
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
				if(filter.getAttrType().equalsIgnoreCase("DATETIME"))
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
				if(filter.getAttrType().equalsIgnoreCase("STRING") )
				{
					if(filter.getMinValue()!=null && filter.getMinValue().length()>0)
					{
						if(filter.getEnumStr() == null || filter.getEnumStr().length()<1)
						{
							if(filterNum>0)
								resultNum = this.tlGammaLayerAttributeRepository.updateLikeStringValue(tmpGuid,filterNum,filter.getAttrGuid(), filter.getMinValue());
							else
								resultNum = this.tlGammaLayerAttributeRepository.insertLikeStringValue(tmpGuid,filter.getAttrGuid(), filter.getMinValue());
						}
						else //enum
						{
							if(filterNum>0)
								resultNum = this.tlGammaLayerAttributeRepository.updateInStringValue(tmpGuid,filterNum,filter.getAttrGuid(), filter.getMinValue());
							else
								resultNum = this.tlGammaLayerAttributeRepository.insertInStringValue(tmpGuid,filter.getAttrGuid(), filter.getMinValue());
			
						}
					}
				}
				filterNum +=1;
			}
			logger.warn("property filter num =="+resultNum);
		}
		try 
		{
			this.tlGammaKernelSubsetRepository.removeExtGuid(ext.getExtGuid());
			
			resultNum = this.tlGammaKernelSubsetRepository.createExtFilter(ext.getExtGuid(), tmpGuid, filterNum);

			logger.warn("insert task num =="+ext.getExtGuid()+"  "+resultNum);
			///TODO
			///DELETE TMP TABLE RECORDS
			this.tlGammaTaskTmpRepository.deleteTaskTmp(tmpGuid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		msgList.put("recordnum", String.valueOf(resultNum));
		System.out.println("end transfer extGuid---recordnum-+"+resultNum+"--"+ System.currentTimeMillis());
		return msgList;
	}
}
