package com.cellinfo;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.controller.entity.AttrParameter;
import com.cellinfo.controller.entity.KernelParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaUser;

public class SysDataTest {

	private RestTemplate testRestTemplate = new RestTemplate();
	
	private String serverPath = "http://47.94.88.135:8181/gammaa";

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdGFkbWluIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfR1JPVVBfQURNSU4ifV0sIm5vbl9leHBpcmVkIjp0cnVlLCJleHAiOjE1MjU5MTgyNzIsImVuYWJsZWQiOnRydWUsIm5vbl9sb2NrZWQiOnRydWUsImdyb3VwIjoiMTM5MDM2NmItZmViZC00Nzc2LWI0YTktOWY4ZTI4ZjE4MWI3In0.0Dw4YrZYrQFun8w8iYZveuaRuf3h2MeQrKUwlO2hTWM7t5gWQP2IlLhhOLRuQk4SS078P5h_KBQ-npM-K9q7Lg";
	
	@Test
    public void groupKernelUpdate() throws Exception {
		System.out.println("-----------------/service/group/kernel/update---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        KernelParameter para = new KernelParameter();
        para.setClassId("fc1bc0b2-d2ab-4b48-9268-5b3c5da849c5");
        para.setClassName(testname);
        para.setDescInfo("descinfo");
        para.setGeomType("POINT");
        List<AttrParameter> fList = new LinkedList<AttrParameter>();
        AttrParameter fpara1 = new AttrParameter();
        fpara1.setAttrName("名称3");
        fpara1.setAttrType("STRING");
        fpara1.setAttrGrade("TASKGRADE");
        fList.add(fpara1);
        AttrParameter fpara2 = new AttrParameter();
        fpara2.setAttrName("数量3");
        fpara2.setAttrType("INTEGER");
        fpara2.setAttrGrade("USERGRADE");
        fList.add(fpara2);

        
        HttpEntity<KernelParameter> entity = new HttpEntity<KernelParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject(this.serverPath+"/service/group/kernel/update",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/kernel/update---------end-----------  ");
    }
	

}
