package com.cellinfo;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.cellinfo.controller.entity.TaskParameter;
import com.cellinfo.entity.Result;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

public class SysDataMaintance {

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	
	private String serverPath1 = "http://47.94.88.135:8181/gammaa";
	
	private String serverPath = "http://127.0.0.1:8081";

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5dWhhbzEyMyIsInNjb3BlIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sIm5vbl9leHBpcmVkIjp0cnVlLCJleHAiOjE1Mjk1Njc2MjMsImVuYWJsZWQiOnRydWUsIm5vbl9sb2NrZWQiOnRydWUsImdyb3VwIjoiMGY3N2FjMWYtYTg5MC00NGViLTg5MjQtNzQ1OWNjNDlkNWJhIn0.Zs-gTGvRRhlakha4Cz0Gw_JYxUQxFBXDsN84QtBKVakplWPxqItaoajX3J8jVMXHnijqGNFjFjFct3nPkpjTmg";
	
	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
	
	
	@Test
    public void service_task_delete() throws Exception {
		System.out.println("-----------------/service/user/task/delete---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        String[] tasks= {
        		"e6fd388b-7a68-4b27-ab63-ab095db9e949",
        		"2450bcad-2d55-4293-8705-09762fe1316b",
        		"8e57dec6-2949-450b-a934-c43cfbba4d29",
        		"0554ced4-1f83-441a-9207-c31a9e027fb7",
        		"453822be-c01d-4b33-9da9-8965dc58587e",
        		"fb4fba52-1132-45b1-84b1-8bc44db5a37c",
        		"cbbd2d07-1a43-4ce3-aa35-0ace9efb816d",
        		"a004014e-2482-4222-b9a1-82815fb96fb0",
        		"66c99101-e7f2-4b01-ae5a-596cfa6d2577",
        		"c7d04491-0e87-4251-8651-c21d7e2280ef",
        		"b31a9b2a-37c1-4436-b826-abe6bb1f763d",
        		"ed524827-2567-48a2-bb9a-1cf5063e8ed0"
        		};
        
        for(String task : tasks)
        {
	        TaskParameter tPara = new TaskParameter();
	        tPara.setId("TULIBJ_20140609");
	        tPara.setTaskId(task);
	        
	        HttpEntity<TaskParameter> entity = new HttpEntity<TaskParameter>(tPara, headers);
	        
	        Result<Map<String,String>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/task/delete",entity,Result.class);
	        System.out.println(result.getData());
	        Assert.assertEquals(result.getMsg(),"成功");
        }
        System.out.println("-----------------/service/user/task/delete---------end-----------  ");
    }
	
	
}
