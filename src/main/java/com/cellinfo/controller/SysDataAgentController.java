package com.cellinfo.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.annotation.OperLog;
import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.GAAttrItem;
import com.cellinfo.controller.entity.GAGeoParameter;
import com.cellinfo.controller.entity.GAPropParameter;
import com.cellinfo.controller.entity.GAQueryParameter;
import com.cellinfo.controller.entity.GeoParameter;
import com.cellinfo.controller.entity.PropItem;
import com.cellinfo.controller.entity.PropParameter;
import com.cellinfo.controller.entity.SpatialQueryParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskExt;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysEnviService;
import com.cellinfo.service.SysKernelService;
import com.cellinfo.service.SysTaskService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.FuncDesc;
import com.cellinfo.utils.OperDesc;
import com.cellinfo.utils.ResultUtil;

/**
 * 用户任务列表
 * 执行任务
 * 绘图操作
 * 搜索
 * 定位
 * @author zhangjian
 */
@ServiceLog(moduleName = "数据访问代理模块")
@PreAuthorize("hasRole('ROLE_USER')")  
@RestController
@RequestMapping("/service/data")
public class SysDataAgentController {

	private final static Logger logger = LoggerFactory.getLogger(SysDataAgentController.class);
	
	@Autowired
	private SysEnviService sysEnviService;

	@Autowired
	private UtilService utilService;
	
	@Autowired
	private SysTaskService sysTaskService;
	
	@Autowired
	private SysKernelService sysKernelService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
	@OperLog(funcName = FuncDesc.QUERY_TASK_REF_DATA, methodName = OperDesc.QUERY)
	@PostMapping(value = "/query")
	public Result<List<Map<String, Object>>> queryData(HttpServletRequest request , @RequestBody SpatialQueryParameter para) {
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		
		if(para.getLayerId()!= null && para.getLayerId().length()>0)
		{
			Optional<ViewTaskExt> taskExtOptional = this.sysTaskService.getTaskLayer(para.getLayerId());
			if(taskExtOptional.isPresent())
			{
				ViewTaskExt taskExt = taskExtOptional.get();
				GAQueryParameter qPara = new GAQueryParameter();
		        qPara.setExtGuid(taskExt.getExtGuid());
		        qPara.setGeoType(taskExt.getGeomType());
		        qPara.setKernelClassid(taskExt.getKernelClassid());
		        qPara.setQueryRange(para.getQueryRange());
		        Optional<TlGammaKernel>  kernelOptional = this.sysKernelService.getByKernelClassid(taskExt.getKernelClassid());
		        if(kernelOptional.isPresent())
		        {
		        	HttpHeaders headers = new HttpHeaders();
		        	String serverPath = kernelOptional.get().getServerPath();
		        	//未配置服务地址
		        	if(serverPath== null || serverPath.length()<1)
		        	{
		        		String tmp = request.getRequestURL().toString();
		        		serverPath = tmp.substring(0,tmp.indexOf("service"));
		        		headers.add("x-auth-token", request.getHeader("x-auth-token") );
		        	}
			        HttpEntity<GAQueryParameter> entity = new HttpEntity<GAQueryParameter>(qPara,headers);
			        System.out.println("serverPath=="+serverPath);
			        System.out.println("qPara=="+  qPara);
			        return restTemplate.postForObject(serverPath+"ga/query",entity,Result.class);
		        }
			}
		}
		
		return ResultUtil.success(geoList);
	}
	
	@PostMapping(value = "/query/count")
	public Result<Long> queryDataCount( HttpServletRequest request , @RequestBody SpatialQueryParameter para) {
		Long dataCount = 0L;
		List<Map<String, Object>> geoList =  new LinkedList<Map<String, Object>>();
		
		if(para.getLayerId()!= null && para.getLayerId().length()>0)
		{
			Optional<ViewTaskExt> taskExtOptional = this.sysTaskService.getTaskLayer(para.getLayerId());
			if(taskExtOptional.isPresent())
			{
				ViewTaskExt taskExt = taskExtOptional.get();
				GAQueryParameter qPara = new GAQueryParameter();
		        qPara.setExtGuid(taskExt.getExtGuid());
		        qPara.setGeoType(taskExt.getGeomType());
		        qPara.setKernelClassid(taskExt.getKernelClassid());
		        qPara.setQueryRange(para.getQueryRange());
		        Optional<TlGammaKernel>  kernelOptional = this.sysKernelService.getByKernelClassid(taskExt.getKernelClassid());
		        if(kernelOptional.isPresent())
		        {
		        	HttpHeaders headers = new HttpHeaders();
		        	String serverPath = kernelOptional.get().getServerPath();
		        	if(serverPath== null || serverPath.length()<1)
		        	{
		        		String tmp = request.getRequestURL().toString();
		        		serverPath = tmp.substring(0,tmp.indexOf("service"));
		        		headers.add("x-auth-token", request.getHeader("x-auth-token") );
		        	}
			        HttpEntity<GAQueryParameter> entity = new HttpEntity<GAQueryParameter>(qPara,headers);
			        return restTemplate.postForObject(serverPath+"ga/query/count",entity,Result.class);
		        }
			}
		}
		
		return ResultUtil.success(dataCount);
	}
	
	/**
	 * 保存GEOJSON 空间数据
	 * @param para
	 * @return
	 */
	@PostMapping(value = "/save")
	public Result<Map<String, String>> saveGeoJsonData(HttpServletRequest request ,@RequestBody GeoParameter para) {
		Map<String, String> geoMap =  new HashMap<String, String>();
		
		if(para.getLayerId()!= null && para.getLayerId().length()>0)
		{
			Optional<ViewTaskExt> taskExtOptional = this.sysTaskService.getTaskLayer(para.getLayerId());
			if(taskExtOptional.isPresent())
			{
				ViewTaskExt taskExt = taskExtOptional.get();
				GAGeoParameter qPara = new GAGeoParameter();
				
				qPara.setClassId(taskExt.getKernelClassid());
				qPara.setGeoJson(para.getGeoJson());
				qPara.setGeoType(taskExt.getGeomType());
				qPara.setKernelId(para.getKernelId());
				qPara.setTaskId(taskExt.getTaskGuid());
		        Optional<TlGammaKernel>  kernelOptional = this.sysKernelService.getByKernelClassid(taskExt.getKernelClassid());
		        if(kernelOptional.isPresent())
		        {
		        	HttpHeaders headers = new HttpHeaders();
		        	String serverPath = kernelOptional.get().getServerPath();
		        	if(serverPath== null || serverPath.length()<1)
		        	{
		        		String tmp = request.getRequestURL().toString();
		        		serverPath = tmp.substring(0,tmp.indexOf("service"));
		        		headers.add("x-auth-token", request.getHeader("x-auth-token") );
		        	}
			        HttpEntity<GAGeoParameter> entity = new HttpEntity<GAGeoParameter>(qPara,headers);
			        return restTemplate.postForObject(serverPath+"ga/save",entity,Result.class);
		        }
			}
		}
		
		return ResultUtil.success(geoMap);
	}
	
	
	/**
	 * 属性信息查询
	 * @param propParam
	 * @return
	 */
	@PostMapping(value = "/props/query")
	public Result<List<Map<String, Object>>> queryPropData(HttpServletRequest request , @RequestBody PropParameter  propParam) {
		List<Map<String, Object>> propValueList =  new LinkedList<Map<String, Object>>();
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if(propParam.getLayerId()!= null && propParam.getLayerId().length()>0)
		{
			Optional<ViewTaskExt> taskExtOptional = this.sysTaskService.getTaskLayer(propParam.getLayerId());
			if(taskExtOptional.isPresent())
			{
				ViewTaskExt taskExt = taskExtOptional.get();
				GAPropParameter pPara = new GAPropParameter();
				pPara.setKernelGuid(propParam.getKernelId());
				pPara.setTaskGuid(taskExt.getTaskGuid());
				pPara.setUserName(cUser.getUserName());
				
				List<ViewTaskAttr> taskattrList = this.sysTaskService.getTaskAttr(taskExt.getTaskGuid());
				List<GAAttrItem> attrs = new LinkedList<GAAttrItem>();
				if(taskattrList!= null && taskattrList.size()>0)
				{
					for(ViewTaskAttr attr :taskattrList)
					{
						GAAttrItem attrItem = new GAAttrItem();
						attrItem.setAttrGuid(attr.getId().getAttrGuid());
						attrItem.setAttrName(attr.getAttrName());
						attrItem.setAttrType(attr.getAttrType());
						attrs.add(attrItem);
					}
				}
				pPara.setAttrs(attrs);
				Optional<TlGammaKernel>  kernelOptional = this.sysKernelService.getByKernelClassid(taskExt.getKernelClassid());
		        if(kernelOptional.isPresent())
		        {
		        	HttpHeaders headers = new HttpHeaders();
		        	String serverPath = kernelOptional.get().getServerPath();
		        	if(serverPath== null || serverPath.length()<1)
		        	{
		        		String tmp = request.getRequestURL().toString();
		        		serverPath = tmp.substring(0,tmp.indexOf("service"));
		        		headers.add("x-auth-token", request.getHeader("x-auth-token") );
		        	}
			        HttpEntity<GAPropParameter> entity = new HttpEntity<GAPropParameter>(pPara,headers);
			        return restTemplate.postForObject(serverPath+"ga/props/query",entity,Result.class);
		        }
			}
		}
		return ResultUtil.success(propValueList);
	}
	
	
	/**
	 * 保存属性数据
	 * @param para
	 * @return
	 */
	@PostMapping(value = "/props/save")
	public Result<Map<String, String>> savePropsData(HttpServletRequest request ,@RequestBody PropParameter para) {
		Map<String, String> resMap =  new HashMap<String, String>();
		UserInfo cUser = this.utilService.getCurrentUser(request);
		
		if(para.getLayerId()!= null && para.getLayerId().length()>0)
		{
			Optional<ViewTaskExt> taskExtOptional = this.sysTaskService.getTaskLayer(para.getLayerId());
			if(taskExtOptional.isPresent())
			{
				ViewTaskExt taskExt = taskExtOptional.get();
				GAPropParameter pPara = new GAPropParameter();
				pPara.setKernelGuid(para.getKernelId());
				pPara.setTaskGuid(taskExt.getTaskGuid());
				pPara.setUserName(cUser.getUserName());
				
				List<GAAttrItem> attrs = new LinkedList<GAAttrItem>();
				
				for(PropItem item : para.getProps())
				{
					Optional<TlGammaKernelAttr> attrOptional =  this.sysKernelService.getAttrById(item.getAttrGuid());
					if(attrOptional.isPresent())
					{
						TlGammaKernelAttr attr = attrOptional.get();
						GAAttrItem attrItem = new GAAttrItem();
						attrItem.setAttrGuid(attr.getAttrGuid());
						attrItem.setAttrName(attr.getAttrName());
						attrItem.setAttrType(attr.getAttrType());
						attrItem.setAttrFgrade(attr.getAttrFgrade());
						attrItem.setAttrValue(item.getAttrValue());
						attrs.add(attrItem);
					}
				}
				
				pPara.setAttrs(attrs);
				Optional<TlGammaKernel>  kernelOptional = this.sysKernelService.getByKernelClassid(taskExt.getKernelClassid());
		        if(kernelOptional.isPresent())
		        {
		        	HttpHeaders headers = new HttpHeaders();
		        	String serverPath = kernelOptional.get().getServerPath();
		        	if(serverPath== null || serverPath.length()<1)
		        	{
		        		String tmp = request.getRequestURL().toString();
		        		serverPath = tmp.substring(0,tmp.indexOf("service"));
		        		headers.add("x-auth-token", request.getHeader("x-auth-token") );
		        	}
			        HttpEntity<GAPropParameter> entity = new HttpEntity<GAPropParameter>(pPara,headers);
			        return restTemplate.postForObject(serverPath+"ga/props/save",entity,Result.class);
		        }
			}
		}	

		return ResultUtil.success(resMap);
	}
	
	
	

}
