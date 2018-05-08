package com.cellinfo.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cellinfo.entity.TlGammaLayerAttribute;
import com.cellinfo.entity.TlGammaLayerLine;
import com.cellinfo.entity.TlGammaLayerPoint;
import com.cellinfo.entity.TlGammaLayerPolygon;
import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskLine;
import com.cellinfo.entity.ViewTaskPoint;
import com.cellinfo.entity.ViewTaskPolygon;
import com.cellinfo.repository.TlGammaLayerAttributeRepository;
import com.cellinfo.repository.TlGammaLayerLineRepository;
import com.cellinfo.repository.TlGammaLayerPointRepository;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;
import com.cellinfo.repository.ViewTaskAttrRepository;
import com.cellinfo.repository.ViewTaskLineRepository;
import com.cellinfo.repository.ViewTaskPointRepository;
import com.cellinfo.repository.ViewTaskPolygonRepository;
import com.vividsolutions.jts.geom.Geometry;

@Service
public class SysBusdataService {
 
	private final static int  MAX_LOADELEMENT_NUM = 500;
	
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
	
	@Autowired
	private ViewTaskAttrRepository viewTaskAttrRepository;
	
	
	public Page<TlGammaLayerPolygon> getAll(PageRequest pageInfo) {
		// TODO Auto-generated method stub
		return this.tlGammaLayerPolygonRepository.findAll(pageInfo);
	}
	
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

	public List<Map<String, Object>> getTaskData(String kernelClassId,String taskGuid,Integer status,String geomType,Geometry filterGeom) {
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		switch (geomType) {
			case "POINT":
				geoList = this.getPointGeojsonList(kernelClassId,taskGuid,status,filterGeom);
				break;
			case "LINE":
				geoList = this.getLineGeojsonList(kernelClassId,taskGuid,status,filterGeom);
				break;
			case "POLYGON":
				geoList = this.getPolygonGeojsonList(kernelClassId,taskGuid,status,filterGeom);
				break;
		}		return geoList;
	}
	
	private  List<Map<String, Object>> getPointGeojsonList(String kernelClassId,String taskGuid,Integer status,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, MAX_LOADELEMENT_NUM);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<ViewTaskPoint> pointlist = this.viewTaskPointRepository.getDataByFilter(kernelClassId,taskGuid,status,filterGeom, pageInfo);

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
	

	private  List<Map<String, Object>> getLineGeojsonList(String kernelClassId,String taskGuid,Integer status,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, MAX_LOADELEMENT_NUM);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<ViewTaskLine> linelist = this.viewTaskLineRepository.getDataByFilter(kernelClassId,taskGuid,status,filterGeom, pageInfo);

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
	
	private  List<Map<String, Object>> getPolygonGeojsonList(String kernelClassId,String taskGuid,Integer status,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, MAX_LOADELEMENT_NUM);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<ViewTaskPolygon> polygonlist = this.viewTaskPolygonRepository.getDataByFilter(kernelClassId,taskGuid,status,filterGeom, pageInfo);

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
	
	
	public  List<ViewTaskAttr> getTaskProps(String taskGuid)
	{
		return this.viewTaskAttrRepository.getByTaskGuid(taskGuid);
	}
	
	
	public void saveAttribute(List<TlGammaLayerAttribute> entities)
	{
		this.tlGammaLayerAttributeRepository.save(entities);
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
	
}
