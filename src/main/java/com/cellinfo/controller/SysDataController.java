package com.cellinfo.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.geotools.geojson.geom.GeometryJSON;
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
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaLayerLine;
import com.cellinfo.entity.TlGammaLayerPoint;
import com.cellinfo.entity.TlGammaLayerPolygon;
import com.cellinfo.entity.TlGammaTask;
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

	private final static Logger logger = LoggerFactory.getLogger(SysDataController.class);
	
	@Autowired
	private SysBusdataService sysBusdataService;
	
	@Autowired
	private SysTaskService sysTaskService ;
	
	@Autowired
	private UtilService utilService;
	
	
	@PostMapping(value = "/userTaskData")
	public Result<List<TlGammaTask>> userTaskData() {
		PageRequest pageInfo = new PageRequest(0, 10);
		
		List<TlGammaTask> taskList =  new LinkedList<TlGammaTask>();
		Iterable<TlGammaTask> mList  = sysTaskService.getAll(pageInfo);

		for (TlGammaTask eachTask : mList) {

			taskList.add(eachTask);
		}
		
		return ResultUtil.success(taskList);
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
			//feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(eachGeom.getKernelGeom())));
			feaMap.put("geometry", new GeometryJSON(10).toString(eachGeom.getKernelGeom()));
			feaMap.put("properties", propMap);
			geoList.add(feaMap);
	
		}
		
		System.out.println("end"+System.currentTimeMillis());
		return ResultUtil.success(geoList);
	}
	
	
	@PostMapping(value = "/saveData")
	public Result<List<String>> saveGeoJsonData(HttpServletRequest request,@RequestBody PostGeoJsonParameter para, BindingResult bindingResult) {
		
		String userName = utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		
		try {
			Geometry geom = new GeometryJSON(10).read(para.getGeoJson());
			geom.setSRID(4326);
			switch(para.getFeatype()) {
				case "POINT" :
					TlGammaLayerPoint point = new TlGammaLayerPoint();
					point.setKernelGuid(UUID.randomUUID().toString());
					point.setTaskGuid(para.getTaskGuid());
					point.setUserName(userName);
					point.setKernelGeom(geom);
					point.setKernelClassid("");
					point.setKernelId(this.utilService.generateShortUuid());
					this.sysBusdataService.savePontGeom(point);
					break;
				case "LINE" :
					TlGammaLayerLine line = new TlGammaLayerLine();
					line.setKernelGuid(UUID.randomUUID().toString());
					line.setTaskGuid(para.getTaskGuid());
					line.setKernelGeom((LineString)geom);
					line.setUserName(userName);
					line.setKernelClassid("");
					line.setKernelId(this.utilService.generateShortUuid());
					this.sysBusdataService.saveLineGeom(line);
					break;
				case "POLYGON" :
					TlGammaLayerPolygon polygon = new TlGammaLayerPolygon();
					polygon.setKernelGuid(UUID.randomUUID().toString());
					polygon.setTaskGuid(para.getTaskGuid());
					polygon.setKernelGeom((Polygon)geom);
					polygon.setUserName(userName);
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
