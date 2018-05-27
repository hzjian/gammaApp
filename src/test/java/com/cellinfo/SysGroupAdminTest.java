package com.cellinfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.controller.entity.AttrParameter;
import com.cellinfo.controller.entity.KernelParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaUser;


public class SysGroupAdminTest {

    private RestTemplate testRestTemplate = new RestTemplate();
	
	private String serverPath1 = "http://47.94.88.135:8181/gammaa";
	
	private String serverPath = "http://127.0.0.1:8081";

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdGFkbWluIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfR1JPVVBfQURNSU4ifV0sIm5vbl9leHBpcmVkIjp0cnVlLCJleHAiOjE1Mjc1MTkxMzYsImVuYWJsZWQiOnRydWUsIm5vbl9sb2NrZWQiOnRydWUsImdyb3VwIjoiMTM5MDM2NmItZmViZC00Nzc2LWI0YTktOWY4ZTI4ZjE4MWI3In0.5MWY79xXotfe3PkCXHC6MjWGgBwlqiDnf9GX-7UY4dk5KtCnhFM9nE2J4VEZIzAESfmJ3WPZM6iGUg8cToxUmQ";
	
	@Test
    public void longin() throws Exception {
		Map<String,String> user = new HashMap<String,String>();
		user.put("userName","jtadmin");
		user.put("userPassword","admin");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,String>> entity = new HttpEntity<Map<String,String>>(user, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/auth/login",entity,Result.class);
        Assert.assertEquals(result.getMsg(),"成功");
    }
	
	@Test
    public void groupMemberList() throws Exception {
		System.out.println("-----------------/service/group/members---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setSkey("a");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/group/members",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        rlist.keySet().stream().forEach(item-> {
        	 System.out.println(item+" " + rlist.get(item));
        });
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/members---------end-----------  ");
    }
	
	
	@Test
    public void testgroupMemberName() throws Exception {
		System.out.println("-----------------/service/group/member/testname---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        String testname ="test_5";
              
        HttpEntity<String> entity = new HttpEntity<String>(testname, headers);
        Result<String> result = testRestTemplate.postForObject(this.serverPath+"/service/group/member/testname",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/member/testname---------end-----------  ");
    }
	
	@Test
    public void groupMemberSave() throws Exception {
		System.out.println("-----------------/service/group/member/save---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        UserParameter para = new UserParameter();
        para.setUserName(testname);
        para.setUserCnname(testname);
        para.setUserPassword("password");
        
        HttpEntity<UserParameter> entity = new HttpEntity<UserParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject(this.serverPath+"/service/group/member/save",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/member/save---------end-----------  ");
    }
	
	
	@Test
    public void groupMemberUpdate() throws Exception {
		System.out.println("-----------------/service/group/member/update---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        String testname ="test_1";
        UserParameter para = new UserParameter();
        para.setUserName(testname);
        para.setUserCnname(testname);
        para.setUserPassword("password1");
        
        HttpEntity<UserParameter> entity = new HttpEntity<UserParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject(this.serverPath+"/service/group/member/update",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/member/update---------end-----------  ");
    }
	
	@Test
    public void groupTaskList() throws Exception {
		System.out.println("-----------------/service/group/tasklist---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/group/tasklist",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        rlist.keySet().stream().forEach(item-> {
        	System.out.println(item+" " + rlist.get(item));
        });
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/tasklist---------end-----------  ");
    }
	
	@Test
    public void ueserTaskList() throws Exception {
		System.out.println("-----------------/service/group/usertasklist---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setSkey("jtuser1");
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/group/usertasklist",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        rlist.keySet().stream().forEach(item-> {
        	System.out.println(item+" " + rlist.get(item));
        });
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/usertasklist---------end-----------  ");
    }
	
	
	@Test
    public void kernelsList() throws Exception {
		System.out.println("-----------------/service/group/kernels---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/group/kernels",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        rlist.keySet().stream().forEach(item-> {
        	System.out.println(item+" " + rlist.get(item));
        });
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/kernels---------end-----------  ");
    }
	
	@Test
    public void kernelinfo() throws Exception {
		System.out.println("-----------------/service/group/kernel/query---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
       String kernelClassid1 ="0fd5f2e6-2bb9-423b-bd97-790940f9997e";
       
       KernelParameter kParam = new KernelParameter();
       kParam.setClassId("0fd5f2e6-2bb9-423b-bd97-790940f9997e");
        
        HttpEntity<KernelParameter> entity = new HttpEntity<KernelParameter>(kParam, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/group/kernel/query",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        if(rlist!= null)
        {
	        rlist.keySet().stream().forEach(item-> {
	        	System.out.println(item+" " + rlist.get(item));
	        });	
        }
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/kernel/query---------end-----------  ");
    }
	
	
	@Test
    public void groupKernelSave() throws Exception {
		System.out.println("-----------------/service/group/kernel/save---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        KernelParameter para = new KernelParameter();
        para.setClassName(testname);
        para.setDescInfo("descinfo");
        para.setGeomType("POINT");

        
        HttpEntity<KernelParameter> entity = new HttpEntity<KernelParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject(this.serverPath+"/service/group/kernel/save",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/kernel/save---------end-----------  ");
    }
	
	@Test
    public void groupKernelAddAttr() throws Exception {
		System.out.println("-----------------/service/group/kernel/addattr---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        String testname ="test_"+ UUID.randomUUID().toString().substring(0, 7);
        AttrParameter para = new AttrParameter();
        para.setAttrAlias("attrAlias");
        para.setAttrEnum("attrEnum");
        para.setAttrGrade("NORMAL");
        para.setAttrName(testname);
        para.setAttrType("STRING");
        para.setClassId("d3ec60f-d0a5-4d84-a774-343bb9b6c92a");
        
        HttpEntity<AttrParameter> entity = new HttpEntity<AttrParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject(this.serverPath+"/service/group/kernel/addattr",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/kernel/addattr---------end-----------  ");
    }
	
		
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
