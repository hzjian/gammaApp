package com.cellinfo.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.annotation.OperLog;
import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.PostGeoJsonParameter;
import com.cellinfo.controller.entity.PostPropParameter;
import com.cellinfo.controller.entity.PropItem;
import com.cellinfo.controller.entity.PropsQueryParameter;
import com.cellinfo.controller.entity.SpatialQueryParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaLayerAttribute;
import com.cellinfo.entity.TlGammaLayerLine;
import com.cellinfo.entity.TlGammaLayerPoint;
import com.cellinfo.entity.TlGammaLayerPolygon;
import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskExt;
import com.cellinfo.service.SysBusdataService;
import com.cellinfo.service.SysEnviService;
import com.cellinfo.service.SysTaskService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.FuncDesc;
import com.cellinfo.utils.OperDesc;
import com.cellinfo.utils.ResultUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;

/**
 * 用户任务列表
 * 执行任务
 * 绘图操作
 * 搜索
 * 定位
 * @author zhangjian
 */
@ServiceLog(moduleName = "我的内容功能模块")
@PreAuthorize("hasRole('ROLE_USER')")  
@RestController
@RequestMapping("/service/data")
public class SysDataController {

	private final static Logger logger = LoggerFactory.getLogger(SysDataController.class);
	
	@Autowired
	private SysEnviService sysEnviService;

	@Autowired
	private SysBusdataService sysBusdataService;
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private SysTaskService sysTaskService;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
	@PostMapping(value = "/task/layerinfo")
	public Result<List<Map<String, String>>> queryTaskLayerInfo( @RequestBody String taskId) {
		List<Map<String, String>> layerlist =  new LinkedList<Map<String, String>>();

		try {
				List<ViewTaskExt> taskExtList =  this.sysBusdataService.getTaskLayerList(taskId);
				layerlist = taskExtList.stream().map(item ->{
					Map<String,String> layer = new HashMap<String,String>();
					layer.put("layerId",item.getLayerGuid());
					layer.put("className", item.getKernelClassname());
					layer.put("extName", item.getExtName());
					layer.put("geoType", item.getGeomType());
					layer.put("layerGrade", String.valueOf(item.getLayerGrade()));
					return layer;
				}).collect(Collectors.toList());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultUtil.success(layerlist);
	}
	
	@OperLog(funcName = FuncDesc.QUERY_TASK_REF_DATA, methodName = OperDesc.QUERY)
	@PostMapping(value = "/query")
	public Result<List<Map<String, Object>>> queryData( @RequestBody SpatialQueryParameter para) {
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		try {
			if(para.getLayerId()!=null)
			{
				Optional<ViewTaskExt> layer =  this.sysTaskService.getTaskLayer(para.getLayerId());
				if(layer.isPresent())
				{
					ViewTaskExt tmpLayer = layer.get();
					String rangeStr = JSONValue.toJSONString(para.getQueryRange());
					Geometry filterGeom = new GeometryJSON(10).read(rangeStr);
					filterGeom.setSRID(4326);
					if(tmpLayer.getExtGuid()!= null && tmpLayer.getExtGuid().length()>0)
						geoList = this.sysBusdataService.getLayerData(tmpLayer.getKernelClassid(),tmpLayer.getExtGuid(),tmpLayer.getGeomType(),filterGeom);
					else
						geoList = this.sysBusdataService.getKernelData(tmpLayer.getKernelClassid(),tmpLayer.getGeomType(),filterGeom);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultUtil.success(geoList);
	}
	
	@PostMapping(value = "/query/count")
	public Result<Long> queryDataCount( @RequestBody SpatialQueryParameter para) {
		Long dataCount = 0L;
		try {
			if(para.getLayerId()!=null)
			{
				Optional<ViewTaskExt> layer =  this.sysTaskService.getTaskLayer(para.getLayerId());
				if(layer.isPresent())
				{
					ViewTaskExt tmpLayer = layer.get();
					String rangeStr = JSONValue.toJSONString(para.getQueryRange());
					Geometry filterGeom = new GeometryJSON(10).read(rangeStr);
					filterGeom.setSRID(4326);
					if(tmpLayer.getExtGuid()!= null && tmpLayer.getExtGuid().length()>0)
						dataCount = this.sysBusdataService.getLayerDataCount(tmpLayer.getKernelClassid(),tmpLayer.getExtGuid(),tmpLayer.getGeomType(),filterGeom);
					else
						dataCount = this.sysBusdataService.getKernelDataCount(tmpLayer.getKernelClassid(),tmpLayer.getGeomType(),filterGeom);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultUtil.success(dataCount);
	}
	
	/**
	 * 属性信息查询
	 * @param propParam
	 * @return
	 */
	@PostMapping(value = "/props/query")
	public Result<List<Map<String, Object>>> queryPropData( @RequestBody PropsQueryParameter  propParam) {
		List<Map<String, Object>> propValueList =  new LinkedList<Map<String, Object>>();
		try {
			List<ViewTaskAttr> taskAttrList = this.sysBusdataService.getTaskProps(propParam.getTaskGuid());
			List<TlGammaLayerAttribute> propsList = this.sysBusdataService.getKernelAttrByGuid(propParam.getGuid());
			
			propValueList = taskAttrList.parallelStream().map(taskAttr ->{
				Map<String, Object> fieldMap = new HashMap<String ,Object >();
				fieldMap.put("attrGuid", taskAttr.getId().getAttrGuid());
				fieldMap.put("attrName", taskAttr.getAttrName());
				
				List<Map<String,String>> attrValues= propsList.parallelStream().filter((attr)->
							attr.getKernelGuid().equalsIgnoreCase(propParam.getGuid()) && 
							attr.getAttrGuid().equalsIgnoreCase(taskAttr.getId().getAttrGuid()) )
				.map((attr)->{
					Map<String, String> fMap = new HashMap<String ,String >();
					switch (taskAttr.getAttrType().toUpperCase())
					{
						case "INTEGER":
							fMap.put("value", String.valueOf(attr.getAttrLong()));
							break;
						case "DOUBLE":
							fMap.put("value", String.valueOf(attr.getAttrLong()));
							break;	
						case "STRING":
							fMap.put("value", attr.getAttrText());
							break;
						case "DATETIME":
							fMap.put("value", attr.getAttrTime().toString());
							break;	
					}
					//增加按任务分组
					//按用户分组
					fMap.put("userName", attr.getUserName());
					fMap.put("updateTime", attr.getUpdateTime().toString());
					return fMap;
				})
				.collect(Collectors.toList());
				
				fieldMap.put("attrValue", attrValues);
				return fieldMap;
			}).collect(Collectors.toList());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ResultUtil.success(propValueList);
	}
	
	/**
	 * 保存GEOJSON 空间数据
	 * @param para
	 * @return
	 */
	@PostMapping(value = "/save")
	public Result<Map<String, String>> saveGeoJsonData(@RequestBody PostGeoJsonParameter para) {
		Map<String, String> geoMap =  new HashMap<String, String>();
		
		try {			
			JSONObject jobject = new JSONObject();
			String jsonStr = JSONValue.toJSONString(para.getGeoJson());
			Geometry geom = new GeometryJSON(10).read(jsonStr);
			geom.setSRID(4326);
			String feaGuid = para.getFeaGuid();
			if(feaGuid == null || feaGuid.trim().length()<1)
				feaGuid = UUID.randomUUID().toString();
			String sId = para.getFeasid();
			if(sId == null || sId.trim().length()<1)
				sId = this.utilService.generateShortUuid(feaGuid);
			String tmpGeoType = this.sysEnviService.getKernelById(para.getFeaClassid()).getGeomType();
			switch(tmpGeoType) {
				case "POINT" :
					TlGammaLayerPoint point = new TlGammaLayerPoint();
					point.setKernelGuid(feaGuid);
					point.setKernelGeom((Point)geom);
					point.setKernelClassid(para.getFeaClassid());
					point.setKernelId(sId);
					this.sysBusdataService.savePontGeom(point);
					break;
				case "LINE" :
					TlGammaLayerLine line = new TlGammaLayerLine();
					line.setKernelGuid(feaGuid);
					line.setKernelGeom((LineString)geom);
					line.setKernelClassid(para.getFeaClassid());
					line.setKernelId(sId);
					this.sysBusdataService.saveLineGeom(line);
					break;
				case "POLYGON" :
					TlGammaLayerPolygon polygon = new TlGammaLayerPolygon();
					polygon.setKernelGuid(feaGuid);
					polygon.setKernelGeom((MultiPolygon)geom);
					polygon.setKernelClassid(para.getFeaClassid());
					polygon.setKernelId(sId);
					this.sysBusdataService.savePolygonGeom(polygon);
					break;
			}
			geoMap.put("feaId", feaGuid);
			geoMap.put("taskId", para.getTaskGuid());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ResultUtil.success(geoMap);
	}
	
	/**
	 * 保存属性数据
	 * @param para
	 * @return
	 */
	@PostMapping(value = "/props/save")
	public Result<Map<String, String>> savePropsData(@RequestBody PostPropParameter para) {
		Map<String, String> resMap =  new HashMap<String, String>();
		
		List<TlGammaLayerAttribute> entities = para.getProps().parallelStream().map((item)->{
			return getAttributeEntity(para.getGuid(),para.getTaskGuid(),para.getUserName(),item);
		}).collect(Collectors.toList());
		
		Iterable<TlGammaLayerAttribute> attrList =  this.sysBusdataService.saveAttribute(entities);
		///返回属性列表
		return ResultUtil.success(resMap);
	}
	 
	private TlGammaLayerAttribute getAttributeEntity(String guid,String taskId,String username,PropItem propItem)
	{
		List<TlGammaLayerAttribute> tmpAttrList;
		TlGammaLayerAttribute attr = new TlGammaLayerAttribute();
		attr.setKernelGuid(guid);
		attr.setTaskGuid(taskId);
		attr.setUserName(username);
		attr.setAttrGuid(propItem.getAttrGuid());
		TlGammaKernelAttr  kernelAttr = this.sysEnviService.getAttrById(propItem.getAttrGuid());
		if(kernelAttr.getAttrFgrade().equalsIgnoreCase("TASKGRADE"))
		{
			tmpAttrList = this.sysBusdataService.getKernelAttrByTaskId(guid, propItem.getAttrGuid(), taskId);
			if(tmpAttrList!= null && tmpAttrList.size()>0)
				attr.setId(tmpAttrList.get(0).getId());
		}
		else if(kernelAttr.getAttrFgrade().equalsIgnoreCase("USERGRADE"))
		{
			tmpAttrList = this.sysBusdataService.getKernelAttrByUsername(guid, propItem.getAttrGuid(), taskId,username);
			if(tmpAttrList!= null && tmpAttrList.size()>0)
				attr.setId(tmpAttrList.get(0).getId());
		}
		else
		{
			tmpAttrList = this.sysBusdataService.getKernelAttrById(guid, propItem.getAttrGuid());
			if(tmpAttrList!= null && tmpAttrList.size()>0)
				attr.setId(tmpAttrList.get(0).getId());
		}
		switch (kernelAttr.getAttrType().toUpperCase())
		{
			case "INTEGER":
				try {
				attr.setAttrLong(Long.valueOf(propItem.getAttrValue()));
				}catch (Exception e) {
		             e.printStackTrace();
				}
				break;
			case "DOUBLE":
				try {
					attr.setAttrDouble(Double.valueOf(propItem.getAttrValue()));
				} catch (Exception e) {
		            e.printStackTrace();
				}
				break;	 
			case "STRING":
				attr.setAttrText(propItem.getAttrValue());
				break;
			case "DATETIME":
				Date tmpDate;
				try {
					tmpDate = df.parse(propItem.getAttrValue());
					attr.setAttrTime(new Timestamp(tmpDate.getTime()));
			     } catch (Exception e) {
			             e.printStackTrace();
			     }
				break;	
		}
		return attr;
	}
}
