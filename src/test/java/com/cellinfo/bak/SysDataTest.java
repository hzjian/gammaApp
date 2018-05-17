package com.cellinfo.bak;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import com.cellinfo.GammaAppApplication;
import com.cellinfo.controller.entity.FieldParameter;
import com.cellinfo.controller.entity.KernelParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.service.SysLogService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GammaAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class SysDataTest {

	@Autowired
    private TestRestTemplate testRestTemplate;
	
	@MockBean
    private SysLogService sysLogService;

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdGFkbWluIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfR1JPVVBfQURNSU4ifV0sIm5vbl9leHBpcmVkIjp0cnVlLCJleHAiOjE1MjU5MTgyNzIsImVuYWJsZWQiOnRydWUsIm5vbl9sb2NrZWQiOnRydWUsImdyb3VwIjoiMTM5MDM2NmItZmViZC00Nzc2LWI0YTktOWY4ZTI4ZjE4MWI3In0.0Dw4YrZYrQFun8w8iYZveuaRuf3h2MeQrKUwlO2hTWM7t5gWQP2IlLhhOLRuQk4SS078P5h_KBQ-npM-K9q7Lg";
	

	@Test
    public void groupKernelUpdate() throws Exception {
		System.out.println("-----------------/service/group/kernel/update---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        KernelParameter para = new KernelParameter();
        para.setClassGuid("fc1bc0b2-d2ab-4b48-9268-5b3c5da849c5");
        para.setClassName(testname);
        para.setDescInfo("descinfo");
        para.setGeomType("POINT");
        List<FieldParameter> fList = new LinkedList<FieldParameter>();
        FieldParameter fpara1 = new FieldParameter();
        fpara1.setFieldName("名称3");
        fpara1.setFieldType("STRING");
        fpara1.setFieldGrade("TASKGRADE");
        fList.add(fpara1);
        FieldParameter fpara2 = new FieldParameter();
        fpara2.setFieldName("数量3");
        fpara2.setFieldType("INTEGER");
        fpara2.setFieldGrade("USERGRADE");
        fList.add(fpara2);
        para.setAppendList(fList);
        
        HttpEntity<KernelParameter> entity = new HttpEntity<KernelParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject("/service/group/kernel/update",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/kernel/update---------end-----------  ");
    }
	

}
