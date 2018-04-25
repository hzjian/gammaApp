package com.cellinfo.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.PostGeoJsonParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.SpatialQueryParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaLayerLine;
import com.cellinfo.entity.TlGammaLayerPoint;
import com.cellinfo.entity.TlGammaLayerPolygon;
import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysBusdataService;
import com.cellinfo.service.SysTaskService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.ResultUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

@ServiceLog(moduleName = "业务数据操作")
@PreAuthorize("hasRole('ROLE_ADMIN')")  
@RestController
@RequestMapping("/service/busdata")
public class SysDataController {

	private final static int  MAX_LOADELEMENT_NUM = 400;
	private final static Logger logger = LoggerFactory.getLogger(SysDataController.class);
	
	@Autowired
	private SysBusdataService sysBusdataService;
	
	@Autowired
	private SysTaskService sysTaskService ;
	
	@Autowired
	private UtilService utilService;
	
	
	@PostMapping(value = "/userTaskList")
	public Result<List<Map<String, Object>>> userTaskList(HttpServletRequest request,@RequestBody RequestParameter para) {
		
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize);
		UserInfo cUser = utilService.getCurrentUser(request);
		List<Map<String, Object>> taskInfoList =  new LinkedList<Map<String, Object>>();
		
		List<TlGammaTask> taskList =  new LinkedList<TlGammaTask>();
		Iterable<TlGammaTask> mList  = sysTaskService.getAll(pageInfo);

		for (TlGammaTask eachTask : mList) {

			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("key", eachTask.getTaskGuid());
			tMap.put("taskName",eachTask.getTaskName());
			tMap.put("classId", "0fd5f2e6-2bb9-423b-bd97-790940f9997e");
			tMap.put("userName", eachTask.getUserName());
			if(eachTask.getTaskTimestart() != null)
			{
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			    String tsStr = sdf.format(eachTask.getTaskTimestart());  
				tMap.put("statDate",tsStr);
			}
			tMap.put("geomType", eachTask.getGeomType());
			taskInfoList.add(tMap);
		}
		return ResultUtil.success(taskInfoList);
	}
	
	@PostMapping(value = "/userTaskData")
	public Result<List<Map<String, Object>>> userTaskData( @RequestBody SpatialQueryParameter para, 
													BindingResult bindingResult) {
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();

		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		try {
			String rangeStr = JSONValue.toJSONString(para.getQueryRange());
			Geometry filterGeom = new GeometryJSON(10).read(rangeStr);
			filterGeom.setSRID(4326);
			
			geoList = this.sysBusdataService.getTaskData(MAX_LOADELEMENT_NUM,para.getClassId(),filterGeom);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResultUtil.success(geoList);
	}
	
	@PostMapping(value = "/testData")
	public Result<List<String>> getJsonList() {
		PageRequest pageInfo = new PageRequest(0, 450);
		
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		Iterable<TlGammaLayerPolygon> mList  = sysBusdataService.getAll(pageInfo);
		System.out.println("start"+System.currentTimeMillis());

		for (TlGammaLayerPolygon eachGeom : mList) {
			Map<String, Object> feaMap = new HashMap<String, Object>();
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put("guid", eachGeom.getKernelGuid());
			propMap.put("anno",eachGeom.getKernelAnno());
			
			feaMap.put("type", "Feature");
			//feaMap.put("geometry",eachGeom.getKernelGeom().toString());
			feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			//feaMap.put("geometry", new GeometryJSON(10).toString(eachGeom.getKernelGeom()));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
		}
		
		System.out.println("end"+System.currentTimeMillis());
		return ResultUtil.success(geoList);
	}
	
	
	@PostMapping(value = "/saveData")
	public Result<List<String>> saveGeoJsonData(HttpServletRequest request,@RequestBody PostGeoJsonParameter para, BindingResult bindingResult) {
		
		UserInfo cUser = utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		
		try {			
			JSONObject jobject = new JSONObject();
			String jsonStr = JSONValue.toJSONString(para.getGeoJson());
			Geometry geom = new GeometryJSON(10).read(jsonStr);
			geom.setSRID(4326);
			String feaGuid = para.getFeaGuid();
			if(feaGuid == null || feaGuid.trim().length()<1)
				feaGuid = UUID.randomUUID().toString();
			switch(para.getFeatype().toUpperCase()) {
				case "MARKER" :
					TlGammaLayerPoint point = new TlGammaLayerPoint();
					point.setKernelGuid(feaGuid);
					point.setTaskGuid(para.getTaskGuid());
					point.setUserName(cUser.getUserName());
					point.setGroupGuid(cUser.getGroupGuid());
					point.setKernelGeom(geom);
					point.setKernelClassid("");
					point.setKernelId(this.utilService.generateShortUuid());
					this.sysBusdataService.savePontGeom(point);
					break;
				case "POLYLINE" :
					TlGammaLayerLine line = new TlGammaLayerLine();
					line.setKernelGuid(feaGuid);
					line.setTaskGuid(para.getTaskGuid());
					line.setGroupGuid(cUser.getGroupGuid());
					line.setKernelGeom((LineString)geom);
					line.setUserName(cUser.getUserName());
					line.setKernelClassid("");
					line.setKernelId(this.utilService.generateShortUuid());
					this.sysBusdataService.saveLineGeom(line);
					break;
				case "POLYGON" :
					TlGammaLayerPolygon polygon = new TlGammaLayerPolygon();
					polygon.setKernelGuid(feaGuid);
					polygon.setGroupGuid(cUser.getGroupGuid());
					polygon.setTaskGuid(para.getTaskGuid());
					polygon.setKernelGeom((Polygon)geom);
					polygon.setUserName(cUser.getUserName());
					polygon.setKernelClassid("");
					polygon.setKernelId(this.utilService.generateShortUuid());
					this.sysBusdataService.savePolygonGeom(polygon);
					break;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResultUtil.success("SUCCESS");
	}
	
	public String geoToJson(Geometry geom){  
		GeometryJSON g = new GeometryJSON(15);
		StringWriter sw = new StringWriter();
	    try {
	        g.write(geom, sw);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    System.out.println(sw.toString());
        return sw.toString();  
	}
	
}
