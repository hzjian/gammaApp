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

import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaLayerLine;
import com.cellinfo.entity.TlGammaLayerPoint;
import com.cellinfo.entity.TlGammaLayerPolygon;
import com.cellinfo.repository.TlGammaKernelRepository;
import com.cellinfo.repository.TlGammaLayerLineRepository;
import com.cellinfo.repository.TlGammaLayerPointRepository;
import com.cellinfo.repository.TlGammaLayerPolygonRepository;
import com.vividsolutions.jts.geom.Geometry;

@Service
public class SysBusdataService {
 
	@Autowired
	private TlGammaLayerPointRepository tlGammaLayerPointRepository;
	
	@Autowired
	private TlGammaLayerLineRepository tlGammaLayerLineRepository;
	
	@Autowired
	private TlGammaLayerPolygonRepository tlGammaLayerPolygonRepository;
	
	@Autowired
	private TlGammaKernelRepository tlGammaKernelRepository;
	
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

	public List<Map<String, Object>> getTaskData(int maxLoadelementNum, String kernelClassId, Geometry filterGeom) {
		
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		TlGammaKernel kernelclass = tlGammaKernelRepository.findOne(kernelClassId);
		String geomType = kernelclass.getGeomType();
		switch (geomType) {
			case "POINT":
				geoList = this.getPointGeojsonList(maxLoadelementNum, filterGeom);
				break;
			case "LINE":
				geoList = this.getLineGeojsonList(maxLoadelementNum, filterGeom);
				break;
			case "POLYGON":
				geoList = this.getPolygonGeojsonList(maxLoadelementNum, filterGeom);
				break;
		}		return geoList;
	}
	
	private  List<Map<String, Object>> getPointGeojsonList(int maxLoadelementNum,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, maxLoadelementNum);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<TlGammaLayerPoint> pointlist = this.tlGammaLayerPointRepository.getDataByFilter(filterGeom, pageInfo);

		for (TlGammaLayerPoint eachGeom : pointlist) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			
			feaMap.put("type", "Feature");
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		
		return geoList;
	}
	

	private  List<Map<String, Object>> getLineGeojsonList(int maxLoadelementNum,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, maxLoadelementNum);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<TlGammaLayerLine> linelist = this.tlGammaLayerLineRepository.getDataByFilter(filterGeom, pageInfo);

		for (TlGammaLayerLine eachGeom : linelist) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			
			feaMap.put("type", "Feature");
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		
		return geoList;
	}
	
	private  List<Map<String, Object>> getPolygonGeojsonList(int maxLoadelementNum,Geometry filterGeom) {	
		PageRequest pageInfo = new PageRequest(0, maxLoadelementNum);
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		List<TlGammaLayerPolygon> polygonlist = this.tlGammaLayerPolygonRepository.getDataByFilter(filterGeom, pageInfo);

		for (TlGammaLayerPolygon eachGeom : polygonlist) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			
			feaMap.put("type", "Feature");
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		
		return geoList;
	}
	
	
}
