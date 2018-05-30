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
import com.cellinfo.entity.ViewTaskExt;
import com.cellinfo.entity.ViewTaskLine;
import com.cellinfo.entity.ViewTaskPoint;
import com.cellinfo.entity.ViewTaskPolygon;
import com.cellinfo.repository.TlGammaLayerAttributeRepository;
import com.cellinfo.repository.TlGammaLayerLineRepository;
import com.cellinfo.repository.TlGammaLayerPointRepository;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;
import com.cellinfo.repository.ViewTaskAttrRepository;
import com.cellinfo.repository.ViewTaskExtRepository;
import com.cellinfo.repository.ViewTaskLineRepository;
import com.cellinfo.repository.ViewTaskPointRepository;
import com.cellinfo.repository.ViewTaskPolygonRepository;
import com.vividsolutions.jts.geom.Geometry;

@Service
public class SysBusdataService {
 
	private final static int  MAX_LOADELEMENT_NUM = 1000;
	
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
	
	@Autowired
	private ViewTaskExtRepository viewTaskExtRepository;
	
	public List<ViewTaskExt> getTaskLayerList(String taskGuid)
	{
		return this.viewTaskExtRepository.findByTaskGuid(taskGuid);
	}
	
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
	
	public  List<ViewTaskAttr> getTaskProps(String taskGuid)
	{
		return this.viewTaskAttrRepository.getByTaskGuid(taskGuid);
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

	

	
	
}
