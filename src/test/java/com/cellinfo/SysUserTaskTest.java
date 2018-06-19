package com.cellinfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONValue;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.controller.entity.AttrParameter;
import com.cellinfo.controller.entity.ExtTypeParameter;
import com.cellinfo.controller.entity.FilterParameter;
import com.cellinfo.controller.entity.GAParameter;
import com.cellinfo.controller.entity.GeoFilterParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.TaskAttrParameter;
import com.cellinfo.controller.entity.TaskLayerParameter;
import com.cellinfo.controller.entity.TaskParameter;
import com.cellinfo.controller.entity.TaskUserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaKernelExt;
import com.cellinfo.entity.TlGammaUser;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class SysUserTaskTest {

    private RestTemplate testRestTemplate = new RestTemplate();
	
	private String serverPath1 = "http://47.94.88.135:8181/gammaa";
	
	private String serverPath = "http://127.0.0.1:8081";

	//private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdHVzZXIxIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwibm9uX2V4cGlyZWQiOnRydWUsImV4cCI6MTUyOTAyMTg1MSwiZW5hYmxlZCI6dHJ1ZSwibm9uX2xvY2tlZCI6dHJ1ZSwiZ3JvdXAiOiIxMzkwMzY2Yi1mZWJkLTQ3NzYtYjRhOS05ZjhlMjhmMTgxYjcifQ.wRYvF-TyMkHg1VAHszNVzBALjznjbJNQt1cVkqlJBTr4s_nGwTUc6DgUKrSnxL25zDHXXxBc7_aAFfLHA2LnsA";
	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5dWhhbzEyMyIsInNjb3BlIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sIm5vbl9leHBpcmVkIjp0cnVlLCJleHAiOjE1Mjk1Njc2MjMsImVuYWJsZWQiOnRydWUsIm5vbl9sb2NrZWQiOnRydWUsImdyb3VwIjoiMGY3N2FjMWYtYTg5MC00NGViLTg5MjQtNzQ1OWNjNDlkNWJhIn0.Zs-gTGvRRhlakha4Cz0Gw_JYxUQxFBXDsN84QtBKVakplWPxqItaoajX3J8jVMXHnijqGNFjFjFct3nPkpjTmg";
	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
	
	@Test
    public void service_user_tasks() throws Exception {
		System.out.println("-----------------/service/user/tasks---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setSkey("");
        para.setIkey(2);
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/tasks",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        System.out.println(rlist);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/tasks---------end-----------  ");
    }
	

	@Test
    public void service_user_task() throws Exception {
		System.out.println("-----------------/service/user/task---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        GAParameter para = new GAParameter();
        para.setId("11248ef0-141f-4b45-b160-d7d74dd6999a");
       
        
        HttpEntity<GAParameter> entity = new HttpEntity<GAParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/task",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        rlist.keySet().stream().forEach(key -> System.out.println(key+" " + rlist.get(key)));
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/task---------end-----------  ");
    }
	
	@Test
    public void kernelSubtypeList() throws Exception {
		System.out.println("-----------------/service/user/kernel/exttypes---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setSkey("");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/exttypes",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        rlist.keySet().stream().forEach(key -> System.out.println(key+" " + rlist.get(key)));
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/kernel/exttypes---------end-----------  ");
    }
	
	@Test
    public void testAddKernelExtType() throws Exception {
		System.out.println("-----------------/service/user/kernel/exttype/save---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        ExtTypeParameter para = new ExtTypeParameter();
        
        para.setExtDesc("extDesc"+UUID.randomUUID().toString());
        para.setExtName("extName"+UUID.randomUUID().toString());
        para.setClassId("54b63474-112d-447f-ba01-4628b7386c0b");
        
        List<FilterParameter> fList = new LinkedList<FilterParameter>();
        FilterParameter fpara1 = new FilterParameter();
        fpara1.setAttrId("fc201d68-0f01-45ab-9eb7-bcca241bb6aa");
        fpara1.setMinValue("10");
        fList.add(fpara1);
        FilterParameter fpara2 = new FilterParameter();
        fpara2.setAttrId("3f1dea7d-3f2a-4f8d-9260-da9d65e2171a");
        fpara2.setMinValue("0");
        fList.add(fpara2);
        //para.setFilterList(fList);
        
        Polygon mpolygon = createPolygonByWKT();
        Map<String,Object> feaMap =new HashMap<String,Object>();
        feaMap.put("type", "Feature");
		feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(mpolygon)));
		

        //para.setFilterGeom(feaMap);
        
        HttpEntity<ExtTypeParameter> entity = new HttpEntity<ExtTypeParameter>(para, headers);
        Result<TlGammaKernelExt> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/exttype/save",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");   
        System.out.println("-----------------/service/user/kernel/exttype/save---------end-----------  "); 
    }
	
	public Polygon createPolygonByWKT() throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        Polygon mpolygon = (Polygon) reader.read("POLYGON((110 35, 110 45, 118 45, 118 35, 110 35))");
        return mpolygon;
    }
	
	@Test
	public void user_kernel_exttype_filterlist() throws Exception {
		System.out.println("-----------------/service/user/kernel/exttype/filterlist---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        ExtTypeParameter para = new ExtTypeParameter();
        
        para.setExtId("ac5af310-915c-47bd-9985-2560a223ebc6");
        
        HttpEntity<ExtTypeParameter> entity = new HttpEntity<ExtTypeParameter>(para, headers);
        Result<List<Map<String,String>>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/exttype/filterlist",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");   
        System.out.println("-----------------/service/user/kernel/exttype/filterlist---------end-----------  "); 
    }
	
	@Test
	public void user_kernel_exttype_geofilterlist() throws Exception {
		System.out.println("-----------------/service/user/kernel/exttype/geofilterlist---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        ExtTypeParameter para = new ExtTypeParameter();
        
        para.setExtId("0174bfbf-c4ab-42d4-8d2c-e036ddb5f8be");
        
        HttpEntity<ExtTypeParameter> entity = new HttpEntity<ExtTypeParameter>(para, headers);
        Result<List<Map<String,String>>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/exttype/geofilterlist",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");   
        System.out.println("-----------------/service/user/kernel/exttype/geofilterlist---------end-----------  "); 
    }
	
	
    public void testTaskNoExtSave() throws Exception {
		System.out.println("-----------------/service/user/task/save---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        TaskParameter para = new TaskParameter();
        
        para.setTaskName("taskname"+UUID.randomUUID().toString());
        para.setTaskDesc("taskDesc"+UUID.randomUUID().toString());
        para.setStartTime("2017-01-01 12:00:01");
        para.setTerminalTime("2018-05-01 08:00:01");
        para.setClassId("54b63474-112d-447f-ba01-4628b7386c0b");
        
        HttpEntity<TaskParameter> entity = new HttpEntity<TaskParameter>(para, headers);
//        Result<TlGammaUser> result = testRestTemplate.postForObject(this.serverPath+"/service/user/task/save",entity,Result.class);
//        System.out.println(result.getData());
//        Assert.assertEquals(result.getMsg(),"成功");   
        System.out.println("-----------------/service/user/task/save---------end-----------  "); 
    }
	
	@Test
    public void testTaskSave() throws Exception {
		System.out.println("-----------------/service/user/task/save---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        TaskParameter para = new TaskParameter();
        
        para.setTaskName("taskname"+UUID.randomUUID().toString());
        para.setTaskDesc("taskDesc"+UUID.randomUUID().toString());
        para.setStartTime("2017-01-01 12:00:01");
        para.setTerminalTime("2018-05-01 08:00:01");
        para.setClassId("8a4d7b02-9b34-4d82-b740-9c0a184cdf1b");
        para.setExtId("17ba169d-d6e5-4980-930c-634704bfcf44");
        
        HttpEntity<TaskParameter> entity = new HttpEntity<TaskParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject(this.serverPath+"/service/user/task/save",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");   
        System.out.println("-----------------/service/user/task/save---------end-----------  "); 
    }
	
	@Test
    public void testTask_update() throws Exception {
		System.out.println("-----------------/service/user/task/update---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        TaskParameter para = new TaskParameter();
        
        para.setTaskId("087bd8d3-c804-40b5-b2af-3333d84e9e32");
        para.setTaskName("taskname"+UUID.randomUUID().toString());
        para.setTaskDesc("taskDesc"+UUID.randomUUID().toString());
        para.setStartTime("2017-01-01 12:00:01");
        para.setTerminalTime("2018-06-12 08:00:01");
        para.setClassId("54b63474-112d-447f-ba01-4628b7386c0b");
        para.setExtId("70eb14ff-778a-4a34-9357-a58c7aba52a8");
        para.setBusPassword("087bd8d3-c804-40b5-b2af-3333d84e9e32");
        
        HttpEntity<TaskParameter> entity = new HttpEntity<TaskParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject(this.serverPath+"/service/user/task/update",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");   
        System.out.println("-----------------/service/user/task/update---------end-----------  "); 
    }
	
	@Test
    public void service_task_user_add() throws Exception {
		System.out.println("-----------------/service/user/taskuser/add---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        TaskUserParameter para = new TaskUserParameter();

        para.setTaskId("54b63474-112d-447f-ba01-4628b7386c0b");
        para.setUserName("jtuser1");
        
        HttpEntity<TaskUserParameter> entity = new HttpEntity<TaskUserParameter>(para, headers);
        Result<Map<String,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/taskuser/add",entity,Result.class);
        Map<String,Object> rlist = result.getData();
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/taskuser/add---------end-----------  ");
    }
	
	@Test
    public void service_task_user_delete() throws Exception {
		System.out.println("-----------------/service/user/taskuser/delete---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        TaskUserParameter para = new TaskUserParameter();

        para.setTaskId("54b63474-112d-447f-ba01-4628b7386c0b");
        para.setUserName("jtuser1");
        
        HttpEntity<TaskUserParameter> entity = new HttpEntity<TaskUserParameter>(para, headers);
        Result<String> result = testRestTemplate.postForObject(this.serverPath+"/service/user/taskuser/delete",entity,Result.class);
        String rlist = result.getData();
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/taskuser/delete---------end-----------  ");
    }
	
	
	@Test
    public void service_task_user_list() throws Exception {
		System.out.println("-----------------/service/user/taskuser/list---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setSkey("54b63474-112d-447f-ba01-4628b7386c0b");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/taskuser/list",entity,Result.class);
        Map<String, Object> rlist = result.getData();
        System.out.println(result.getData());

        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/taskuser/list---------end-----------  ");
    }
	
	//4ecb7b5a-444b-4341-a9ae-7c3e9d527dc3
	@Test
    public void service_taskattr_ranklist() throws Exception {
		System.out.println("-----------------/service/user/taskattr/ranklist---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        TaskAttrParameter para = new TaskAttrParameter();
        para.setTaskId("4ecb7b5a-444b-4341-a9ae-7c3e9d527dc3");
       
        
        HttpEntity<TaskAttrParameter> entity = new HttpEntity<TaskAttrParameter>(para, headers);
        Result<List<Map<String, Object>>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/taskattr/ranklist",entity,Result.class);
        List<Map<String, Object>> rlist = result.getData();
        System.out.println(result.getData());
        rlist.stream().forEach(key -> System.out.println(key+" " + rlist.indexOf(key)));
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/taskattr/ranklist---------end-----------  ");
    }
	
	///task/kernel/avaliable
	@Test
    public void service_task_kernel_avaliable() throws Exception {
		System.out.println("-----------------/service/user/task/kernel/avaliable---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        TaskParameter para = new TaskParameter();
        //para.setTaskId("4ecb7b5a-444b-4341-a9ae-7c3e9d527dc3");
       
        
        HttpEntity<TaskParameter> entity = new HttpEntity<TaskParameter>(para, headers);
        Result<List<Map<String, Object>>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/task/kernel/avaliable",entity,Result.class);
        List<Map<String, Object>> rlist = result.getData();
        System.out.println(result.getData());
        rlist.stream().forEach(key -> System.out.println(key+" " + rlist.indexOf(key)));
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/task/kernel/avaliable---------end-----------  ");
    }
	
	
	///task/attr/avaliable
	@Test
    public void service_task_attr_avaliable() throws Exception {
		System.out.println("-----------------/service/user/task/attr/avaliable---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        TaskParameter para = new TaskParameter();
        para.setTaskId("7a62197d-5178-4f0b-910e-5f72a563c06a");
        para.setRefClassId("159462a8-64df-4d07-be74-afa73f5f6a1f");
       
        
        HttpEntity<TaskParameter> entity = new HttpEntity<TaskParameter>(para, headers);
        Result<List<Map<String, Object>>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/task/attr/avaliable",entity,Result.class);
        List<Map<String, Object>> rlist = result.getData();
        System.out.println(result.getData());
        rlist.stream().forEach(key -> System.out.println(key+" " + rlist.indexOf(key)));
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/task/attr/avaliable---------end-----------  ");
    }
	//taskattr/list
	@Test
    public void service_taskattr_list() throws Exception {
		System.out.println("-----------------/service/user/taskattr/list---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setSkey("b5adb148-4afd-4814-ba78-d833095c2800");
       
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/taskattr/list",entity,Result.class);
        Map<String, Object> rlist = result.getData();
        System.out.println(result.getData());
        
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/taskattr/list---------end-----------  ");
    }
	
	@Test
    public void service_taskref_add() throws Exception {
		System.out.println("-----------------/service/user/taskref/add---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        TaskParameter para = new TaskParameter();
        para.setTaskId("db8e14de-595e-413c-8d33-7f682c230740");
        para.setRefClassId("eab1a6a1-0f10-4cd3-a169-446def5051eb");
        
        HttpEntity<TaskParameter> entity = new HttpEntity<TaskParameter>(para, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/taskref/add",entity,Result.class);
        Map<String, Object> rlist = result.getData();
        System.out.println(result.getData());
        
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/taskref/add---------end-----------  ");
    }
	
	@Test
    public void service_taskref_list() throws Exception {
		System.out.println("-----------------/service/user/taskref/list---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        TaskLayerParameter para = new TaskLayerParameter();
        para.setTaskId("db8e14de-595e-413c-8d33-7f682c230740");
       
        
        HttpEntity<TaskLayerParameter> entity = new HttpEntity<TaskLayerParameter>(para, headers);
        Result<List<Map<String, Object>>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/taskref/list",entity,Result.class);
        List<Map<String, Object>> rlist = result.getData();
        System.out.println(result.getData());
        
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/taskref/list---------end-----------  ");
    }
		
	@Test
    public void service_user_kernel_addattr() throws Exception {
		System.out.println("-----------------/service/user/kernel/addattr--------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        AttrParameter para = new AttrParameter();
        para.setClassId("0fd5f2e6-2bb9-423b-bd97-790940f9997e");
        para.setAttrName(UUID.randomUUID().toString());
        para.setAttrType("STRING");
        para.setAttrGrade("NORMAL");
        para.setAttrDesc("f8");
        
        HttpEntity<AttrParameter> entity = new HttpEntity<AttrParameter>(para, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/addattr",entity,Result.class);
        Map<String, Object> rlist = result.getData();
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/kernel/addattr---------end-----------  ");
    }
	//updategeofilter
	
	@Test
    public void service_task_updategeofilter() throws Exception {
		System.out.println("-----------------/service/user/kernel/exttype/updategeofilter---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        GeoFilterParameter geofilter = new GeoFilterParameter();
        geofilter.setFilterId("0807527f-cf0b-41e3-8049-94eff8316928");
        geofilter.setFilterName("GEOFILTER"+UUID.randomUUID().toString().substring(0, 5));
        
        HttpEntity<GeoFilterParameter> entity = new HttpEntity<GeoFilterParameter>(geofilter, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/exttype/updategeofilter",entity,Result.class);
        Map<String, Object> rlist = result.getData();
        System.out.println(rlist);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/kernel/exttype/updategeofilter---------end-----------  ");
    }
	
	
	@Test
    public void service_task_user_avaliable() throws Exception {
		System.out.println("-----------------/service/user/task/user/avaliable---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        TaskParameter taskPara = new TaskParameter();
        taskPara.setTaskId("54b63474-112d-447f-ba01-4628b7386c0b");
        
        HttpEntity<TaskParameter> entity = new HttpEntity<TaskParameter>(taskPara, headers);
        Result<List<Map<String, Object>>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/task/user/avaliable",entity,Result.class);
        List<Map<String, Object>> rlist = result.getData();
        rlist.stream().forEach(key -> System.out.println(key+" " + rlist.indexOf(key)));
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/task/user/avaliable---------end-----------  ");
    }
	
	//kernel/attrs
	@Test
    public void service_user_kernel_attrs() throws Exception {
		System.out.println("-----------------/service/user/kernel/attrs---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setClassId("54b63474-112d-447f-ba01-4628b7386c0b");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/attrs",entity,Result.class);
        Map<String, Object> rlist = result.getData();

        System.out.println(rlist);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/kernel/attrs---------end-----------  ");
    }
	
	@Test
    public void service_user_kernel_exttype_geofilter() throws Exception {
		System.out.println("-----------------/service/user/kernel/exttype/geofilter---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        GAParameter para = new GAParameter();

        para.setId("66a645df-b07b-44b9-ba4a-0ae0247b63e4");
        
        HttpEntity<GAParameter> entity = new HttpEntity<GAParameter>(para, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/exttype/geofilter",entity,Result.class);
        Map<String, Object> rlist = result.getData();

        System.out.println(rlist);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/kernel/exttype/geofilter---------end-----------  ");
    }
	
	@Test
    public void service_user_kernel_attr() throws Exception {
		System.out.println("-----------------/service/user/kernel/attr---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        GAParameter para = new GAParameter();

        para.setId("824b3004-1822-42d0-b5c9-52dc7d3416dc");
        
        HttpEntity<GAParameter> entity = new HttpEntity<GAParameter>(para, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/attr",entity,Result.class);
        Map<String, Object> rlist = result.getData();

        System.out.println(rlist);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/kernel/attr---------end-----------  ");
    }
	
	@Test
    public void service_user_kernel_exttype_filter() throws Exception {
		System.out.println("-----------------/service/user/kernel/exttype/filter---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        GAParameter para = new GAParameter();

        para.setId("034d0d7e-1fc9-439e-8892-7c31bcf165b5");
        
        HttpEntity<GAParameter> entity = new HttpEntity<GAParameter>(para, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/exttype/filter",entity,Result.class);
        Map<String, Object> rlist = result.getData();

        System.out.println(rlist);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/kernel/exttype/filter---------end-----------  ");
    }
	
	@Test
    public void service_user_relate_kernels() throws Exception {
		System.out.println("-----------------/service/user/relate/kernels---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        HttpEntity entity = new HttpEntity(headers);
        Result<List<Map<String, Object>>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/relate/kernels",entity,Result.class);
        List<Map<String, Object>> rlist = result.getData();

        System.out.println(rlist);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/relate/kernels---------end-----------  ");
    }
	
	@Test
    public void service_user_relate_attrs() throws Exception {
		System.out.println("-----------------/service/user/relate/attrs---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        GAParameter para = new GAParameter();

        para.setId("34b0c7e1-3b9c-4d96-9413-3031d909b5e8");
        HttpEntity<GAParameter> entity = new HttpEntity<GAParameter>(para, headers);
        Result<List<Map<String, Object>>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/relate/attrs",entity,Result.class);
        List<Map<String, Object>> rlist = result.getData();

        System.out.println(rlist);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/relate/attrs---------end-----------  ");
    }
	
	
}
