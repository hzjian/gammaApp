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

import com.cellinfo.controller.entity.ExtTypeParameter;
import com.cellinfo.controller.entity.FilterParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.TaskParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaKernelExt;
import com.cellinfo.entity.TlGammaUser;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class SysGroupUserTest {

    private RestTemplate testRestTemplate = new RestTemplate();
	
	private String serverPath1 = "http://47.94.88.135:8181/gammaa";
	
	private String serverPath = "http://127.0.0.1:8081";

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdGFkbWluIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfR1JPVVBfQURNSU4ifV0sIm5vbl9leHBpcmVkIjp0cnVlLCJleHAiOjE1MjcyMDk1ODAsImVuYWJsZWQiOnRydWUsIm5vbl9sb2NrZWQiOnRydWUsImdyb3VwIjoiMTM5MDM2NmItZmViZC00Nzc2LWI0YTktOWY4ZTI4ZjE4MWI3In0.J0znmSvVI40C7xwx_59-H1eOs9Iai2W4tqZzO0vMcsrHuFWaWiyz0Ut0dbvYvcThvEk97KEoJn_8owM481q6HA";
	
	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);


	@Test
    public void kernelSubtypeList() throws Exception {
		System.out.println("-----------------/service/user/kernel/exttypes---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setSkey("54b63474-112d-447f-ba01-4628b7386c0b");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/kernel/exttypes",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        rlist.keySet().stream().forEach(key -> System.out.println(key+" " + rlist.get(key)));
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
        para.setKernelClassid("54b63474-112d-447f-ba01-4628b7386c0b");
        
        List<FilterParameter> fList = new LinkedList<FilterParameter>();
        FilterParameter fpara1 = new FilterParameter();
        fpara1.setAttrGuid("fc201d68-0f01-45ab-9eb7-bcca241bb6aa");
        fpara1.setMinValue("10");
        fList.add(fpara1);
        FilterParameter fpara2 = new FilterParameter();
        fpara2.setAttrGuid("3f1dea7d-3f2a-4f8d-9260-da9d65e2171a");
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
	

	
    public void testTaskNoExtSave() throws Exception {
		System.out.println("-----------------/service/user/task/save---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        TaskParameter para = new TaskParameter();
        
        para.setTaskName("taskname"+UUID.randomUUID().toString());
        para.setTaskDesc("taskDesc"+UUID.randomUUID().toString());
        para.setStartDate("2017-01-01 12:00:01");
        para.setEndDate("2018-05-01 08:00:01");
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
        para.setStartDate("2017-01-01 12:00:01");
        para.setEndDate("2018-05-01 08:00:01");
        para.setClassId("54b63474-112d-447f-ba01-4628b7386c0b");
        para.setExtGuid("70eb14ff-778a-4a34-9357-a58c7aba52a8");
        
        HttpEntity<TaskParameter> entity = new HttpEntity<TaskParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject(this.serverPath+"/service/user/task/save",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");   
        System.out.println("-----------------/service/user/task/save---------end-----------  "); 
    }

}
