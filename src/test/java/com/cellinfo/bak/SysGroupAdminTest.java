package com.cellinfo.bak;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.cellinfo.GammaAppApplication;
import com.cellinfo.controller.entity.FieldParameter;
import com.cellinfo.controller.entity.KernelParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.service.SysLogService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GammaAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class SysGroupAdminTest {

	@Autowired
    private TestRestTemplate testRestTemplate;
	
	@MockBean
    private SysLogService sysLogService;

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdGFkbWluIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfR1JPVVBfQURNSU4ifV0sIm5vbl9leHBpcmVkIjp0cnVlLCJleHAiOjE1MjU5MTgyNzIsImVuYWJsZWQiOnRydWUsIm5vbl9sb2NrZWQiOnRydWUsImdyb3VwIjoiMTM5MDM2NmItZmViZC00Nzc2LWI0YTktOWY4ZTI4ZjE4MWI3In0.0Dw4YrZYrQFun8w8iYZveuaRuf3h2MeQrKUwlO2hTWM7t5gWQP2IlLhhOLRuQk4SS078P5h_KBQ-npM-K9q7Lg";
	
	@Test
    public void longin() throws Exception {
		UserParameter user = new UserParameter();
		user.setUserName("jtadmin");
		user.setUserPassword("admin");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserParameter> entity = new HttpEntity<UserParameter>(user, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject("/service/auth/login",entity,Result.class);
        Assert.assertEquals(result.getMsg(),"成功");
    }
	
	@Test
    public void curUserInfo() throws Exception {
		System.out.println("-----------------/service/group/curUserInfo---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token);
        
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<String,String>();
        multiValueMap.add("username","lake");
        
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<MultiValueMap<String,String>>(multiValueMap, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject("/service/group/curUserInfo",entity,Result.class);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/group/curUserInfo---------start-----------  ");
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
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<List<Map<String ,Object>>> result = testRestTemplate.postForObject("/service/group/members",entity,Result.class);
        List<Map<String ,Object>> rlist = result.getData();
        rlist.stream().forEach(item-> {
        	item.keySet().stream().forEach(key -> System.out.println(key+" " + item.get(key)));
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
        Result<String> result = testRestTemplate.postForObject("/service/group/member/testname",entity,Result.class);
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
        Result<TlGammaUser> result = testRestTemplate.postForObject("/service/group/member/save",entity,Result.class);
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
        Result<TlGammaUser> result = testRestTemplate.postForObject("/service/group/member/update",entity,Result.class);
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
        Result<List<Map<String ,Object>>> result = testRestTemplate.postForObject("/service/group/tasklist",entity,Result.class);
        List<Map<String ,Object>> rlist = result.getData();
        rlist.stream().forEach(item-> {
        	item.keySet().stream().forEach(key -> System.out.println(key+" " + item.get(key)));
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
        Result<List<Map<String ,Object>>> result = testRestTemplate.postForObject("/service/group/usertasklist",entity,Result.class);
        List<Map<String ,Object>> rlist = result.getData();
        rlist.stream().forEach(item-> {
        	item.keySet().stream().forEach(key -> System.out.println(key+" " + item.get(key)));
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
        Result<List<Map<String ,Object>>> result = testRestTemplate.postForObject("/service/group/kernels",entity,Result.class);
        List<Map<String ,Object>> rlist = result.getData();
        rlist.stream().forEach(item-> {
        	item.keySet().stream().forEach(key -> System.out.println(key+" " + item.get(key)));
        });
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/kernels---------end-----------  ");
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
        List<FieldParameter> fList = new LinkedList<FieldParameter>();
        FieldParameter fpara1 = new FieldParameter();
        fpara1.setFieldName("名称");
        fpara1.setFieldType("STRING");
        fpara1.setFieldGrade("TASKGRADE");
        fList.add(fpara1);
        FieldParameter fpara2 = new FieldParameter();
        fpara2.setFieldName("数量");
        fpara2.setFieldType("INTEGER");
        fpara2.setFieldGrade("USERGRADE");
        fList.add(fpara2);
        para.setFieldList(fList);
        
        HttpEntity<KernelParameter> entity = new HttpEntity<KernelParameter>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject("/service/group/kernel/save",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/kernel/save---------end-----------  ");
    }
	
	@Test
    public void groupKernelquery() throws Exception {
		System.out.println("-----------------/service/group/kernel/query---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        String para ="7d3ec60f-d0a5-4d84-a774-343bb9b6c92a";
        HttpEntity<String> entity = new HttpEntity<String>(para, headers);
        Result<TlGammaUser> result = testRestTemplate.postForObject("/service/group/kernel/query",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/group/kernel/query---------end-----------  ");
    }
	
	
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
	
	/*
	
	@Test
    public void get() throws Exception {
        Map<String,String> multiValueMap = new HashMap<>();
        multiValueMap.put("username","lake");//传值，但要在url上配置相应的参数
        ActResult result = testRestTemplate.getForObject("/test/get?username={username}",ActResult.class,multiValueMap);
        Assert.assertEquals(result.getCode(),0);
    }
	 
	@Test
    public void post() throws Exception {
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("username","lake");
        ActResult result = testRestTemplate.postForObject("/test/post",multiValueMap,ActResult.class);
        Assert.assertEquals(result.getCode(),0);
    }
	@Test
    public void upload() throws Exception {
        Resource resource = new FileSystemResource("/home/lake/github/wopi/build.gradle");
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("username","lake");
        multiValueMap.add("files",resource);
        ActResult result = testRestTemplate.postForObject("/test/upload",multiValueMap,ActResult.class);
        Assert.assertEquals(result.getCode(),0);
    }
	 
	@Test
    public void download() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("token","xxxxxx");
        HttpEntity formEntity = new HttpEntity(headers);
        String[] urlVariables = new String[]{"admin"};
        ResponseEntity<byte[]> response = testRestTemplate.exchange("/test/download?username={1}",HttpMethod.GET,formEntity,byte[].class,urlVariables);
        if (response.getStatusCode() == HttpStatus.OK) {
            Files.write(response.getBody(),new File("/home/lake/github/file/test.gradle"));
        }
    }
	@Test
    public void getHeader() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("token","xxxxxx");
        HttpEntity formEntity = new HttpEntity(headers);
        String[] urlVariables = new String[]{"admin"};
        ResponseEntity<ActResult> result = testRestTemplate.exchange("/test/getHeader?username={username}", HttpMethod.GET,formEntity,ActResult.class,urlVariables);
        Assert.assertEquals(result.getBody().getCode(),0);
    }
    
    */
}
