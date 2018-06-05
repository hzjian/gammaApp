package com.cellinfo.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.controller.entity.GAAttrItem;
import com.cellinfo.controller.entity.GAExtParameter;
import com.cellinfo.controller.entity.GAGeoParameter;
import com.cellinfo.controller.entity.GAPropParameter;
import com.cellinfo.controller.entity.GAQueryParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaLayerAttribute;
import com.cellinfo.entity.TlGammaLayerLine;
import com.cellinfo.entity.TlGammaLayerPoint;
import com.cellinfo.entity.TlGammaLayerPolygon;
import com.cellinfo.service.SysDataService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.ResultUtil;
import com.cellinfo.utils.ReturnDesc;
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
@RestController
@RequestMapping("/ga")
public class SysDataController {

	private final static Logger logger = LoggerFactory.getLogger(SysDataController.class);

	@Autowired
	private SysDataService sysDataService;
	
	@Autowired
	private UtilService utilService;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
	///更新核心对象子类
	@PostMapping(value = "/kernel/exttype/refresh")
	public Result<Map<String,String>> updateExttypeKernels(@RequestBody GAExtParameter extParam, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		if(extParam.getExtGuid()!= null && extParam.getKernelClassid()!= null && extParam.getGeoType()!= null)
		{
			this.sysDataService.createExtKernelList(extParam);
			return ResultUtil.success(rMap);
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/query")
	public Result<List<Map<String, Object>>> queryData( @RequestBody GAQueryParameter para) {
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		System.out.println("111");
		try {
				String rangeStr = JSONValue.toJSONString(para.getQueryRange());
				Geometry filterGeom = new GeometryJSON(10).read(rangeStr);
				filterGeom.setSRID(4326);
				if(para.getExtGuid()!= null && para.getExtGuid().length()>0)
					geoList = this.sysDataService.getLayerData(para.getKernelClassid(),para.getExtGuid(),para.getGeoType(),filterGeom);
				else
					geoList = this.sysDataService.getKernelData(para.getKernelClassid(),para.getGeoType(),filterGeom);
				System.out.println("222");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResultUtil.success(geoList);
	}
	
	@PostMapping(value = "/query/count")
	public Result<Long> queryDataCount( @RequestBody GAQueryParameter para) {
		Long dataCount = 0L;
		try {
			if(para.getKernelClassid()!=null)
			{
				String rangeStr = JSONValue.toJSONString(para.getQueryRange());
				Geometry filterGeom = new GeometryJSON(10).read(rangeStr);
				filterGeom.setSRID(4326);
				if(para.getExtGuid()!= null && para.getExtGuid().length()>0)
					dataCount = this.sysDataService.getLayerDataCount(para.getKernelClassid(),para.getExtGuid(),para.getGeoType(),filterGeom);
				else
					dataCount = this.sysDataService.getKernelDataCount(para.getKernelClassid(),para.getGeoType(),filterGeom);
			}
		} catch (IOException e) {
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
	public Result<List<Map<String, Object>>> queryPropData( @RequestBody GAPropParameter  propParam) {
		List<Map<String, Object>> propValueList =  new LinkedList<Map<String, Object>>();
		try {
			List<TlGammaLayerAttribute> propsList = this.sysDataService.getKernelAttrByGuid(propParam.getKernelGuid());
			
			propValueList = propParam.getAttrs().stream().map(attrItem ->{
				Map<String, Object> fieldMap = new HashMap<String ,Object >();
				fieldMap.put("attrGuid", attrItem.getAttrGuid() );
				fieldMap.put("attrName", attrItem.getAttrName());
				
				String kGuid = propParam.getKernelGuid();
				String aGuid = attrItem.getAttrGuid();
				
				List<Map<String,String>> attrValues= propsList.stream().filter((attr)->
							attr.getKernelGuid().equalsIgnoreCase(kGuid) && 
							attr.getAttrGuid().equalsIgnoreCase(aGuid) )
				.map((attr)->{
					Map<String, String> fMap = new HashMap<String ,String >();
					switch (attrItem.getAttrType().toUpperCase())
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
					if(attr.getUpdateTime()!= null)
					{
						fMap.put("updateTime", df.format(attr.getUpdateTime()));
					}
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
	public Result<Map<String, String>> saveGeoJsonData(@RequestBody GAGeoParameter para) {
		Map<String, String> geoMap =  new HashMap<String, String>();
		
		try {			
			JSONObject jobject = new JSONObject();
			String jsonStr = JSONValue.toJSONString(para.getGeoJson());
			Geometry geom = new GeometryJSON(10).read(jsonStr);
			geom.setSRID(4326);
			String kernelId = para.getKernelId();
			if(kernelId == null || kernelId.trim().length()<1)
				kernelId = UUID.randomUUID().toString();
			String featureId = para.getFeatureId();
			if(featureId == null || featureId.trim().length()<1)
				featureId = this.utilService.generateShortUuid(kernelId);
			switch(para.getGeoType()) {
				case "POINT" :
					TlGammaLayerPoint point = new TlGammaLayerPoint();
					point.setKernelGuid(kernelId);
					point.setKernelGeom((Point)geom);
					point.setKernelClassid(para.getClassId());
					point.setKernelId(featureId);
					this.sysDataService.savePontGeom(point);
					break;
				case "LINE" :
					TlGammaLayerLine line = new TlGammaLayerLine();
					line.setKernelGuid(kernelId);
					line.setKernelGeom((LineString)geom);
					line.setKernelClassid(para.getClassId());
					line.setKernelId(featureId);
					this.sysDataService.saveLineGeom(line);
					break;
				case "POLYGON" :
					TlGammaLayerPolygon polygon = new TlGammaLayerPolygon();
					polygon.setKernelGuid(kernelId);
					polygon.setKernelGeom((MultiPolygon)geom);
					polygon.setKernelClassid(para.getClassId());
					polygon.setKernelId(featureId);
					this.sysDataService.savePolygonGeom(polygon);
					break;
			}
			geoMap.put("feaId", kernelId);
			geoMap.put("taskId", para.getTaskId());
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
	public Result<Map<String, String>> savePropsData(@RequestBody GAPropParameter para) {
		Map<String, String> resMap =  new HashMap<String, String>();
		
		List<TlGammaLayerAttribute> entities = para.getAttrs().parallelStream().map((attrItem)->{
			return getAttributeEntity(para.getKernelGuid(),para.getTaskGuid(),para.getUserName(),attrItem);
		}).collect(Collectors.toList());
		
		Iterable<TlGammaLayerAttribute> attrList =  this.sysDataService.saveAttribute(entities);
		for(TlGammaLayerAttribute tAttr: attrList)
		{
			resMap.put(tAttr.getAttrGuid(),tAttr.getUserName());
		}
		///返回属性列表
		return ResultUtil.success(resMap);
	}
	 
	private TlGammaLayerAttribute getAttributeEntity(String kernelGuid,String taskGuid,String userName,GAAttrItem attrItem)
	{
		List<TlGammaLayerAttribute> tmpAttrList;
		TlGammaLayerAttribute attr = new TlGammaLayerAttribute();
		attr.setKernelGuid(kernelGuid);
		attr.setTaskGuid(taskGuid);
		attr.setUserName(userName);
		attr.setAttrGuid(attrItem.getAttrGuid());
		attr.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		if(attrItem.getAttrFgrade() != null)
		{
			if(attrItem.getAttrFgrade().equalsIgnoreCase("TASKGRADE"))
			{
				tmpAttrList = this.sysDataService.getKernelAttrByTaskId(kernelGuid, attrItem.getAttrGuid(), taskGuid);
				if(tmpAttrList!= null && tmpAttrList.size()>0)
					attr.setId(tmpAttrList.get(0).getId());
			}
			else if(attrItem.getAttrFgrade().equalsIgnoreCase("USERGRADE"))
			{
				tmpAttrList = this.sysDataService.getKernelAttrByUsername(kernelGuid, attrItem.getAttrGuid(), taskGuid,userName);
				if(tmpAttrList!= null && tmpAttrList.size()>0)
					attr.setId(tmpAttrList.get(0).getId());
			}
			else
			{
				tmpAttrList = this.sysDataService.getKernelAttrById(kernelGuid, attrItem.getAttrGuid());
				if(tmpAttrList!= null && tmpAttrList.size()>0)
					attr.setId(tmpAttrList.get(0).getId());
			}
		}
		switch (attrItem.getAttrType().toUpperCase())
		{
			case "INTEGER":
				try {
				attr.setAttrLong(Long.valueOf(attrItem.getAttrValue()));
				}catch (Exception e) {
		             e.printStackTrace();
				}
				break;
			case "DOUBLE":
				try {
					attr.setAttrDouble(Double.valueOf(attrItem.getAttrValue()));
				} catch (Exception e) {
		            e.printStackTrace();
				}
				break;	 
			case "STRING":
				attr.setAttrText(attrItem.getAttrValue());
				break;
			case "DATETIME":
				Date tmpDate;
				try {
					tmpDate = df.parse(attrItem.getAttrValue());
					attr.setAttrTime(new Timestamp(tmpDate.getTime()));
			     } catch (Exception e) {
			             e.printStackTrace();
			     }
				break;	
		}
		return attr;
	}
}
