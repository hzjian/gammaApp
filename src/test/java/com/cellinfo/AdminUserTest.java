package com.cellinfo;

import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.controller.entity.GroupParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;


public class AdminUserTest {
	
    private RestTemplate testRestTemplate = new RestTemplate();
	
	private String serverPath1 = "http://47.94.88.135:8181/gammaa";
	
	private String serverPath = "http://127.0.0.1:8081";

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInNjb3BlIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn1dLCJub25fZXhwaXJlZCI6dHJ1ZSwiZXhwIjoxNTI2NDUyMTc3LCJlbmFibGVkIjp0cnVlLCJub25fbG9ja2VkIjp0cnVlLCJncm91cCI6IjEwMDAwIn0.t9tJphfw4U4ByVSOydoBmZN4w1HUxaTSY2g7-EWTnw-N6p58QS1VqxzLfcsFA7QkgVqlHt7MHxVr3w9aXJ4O3Q";
	
	@Test
    public void sysAminuserList() throws Exception {
		System.out.println("-----------------/service/api/users---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setGroupGuid("ff0bba88-1a8b-4714-9833-846df0e09519");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/api/users",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        rlist.keySet().forEach(item-> {
        	System.out.println(item+" " + rlist.get(item));
        });
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/api/users---------end-----------  ");
    }
	
	

	@Test
    public void sysAdminTestUserName() throws Exception {
		System.out.println("-----------------/service/api/user/testname---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        String testname ="test_5";
              
        HttpEntity<String> entity = new HttpEntity<String>(testname, headers);
        Result<String> result = testRestTemplate.postForObject(this.serverPath+"/service/api/user/testname",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/api/user/testname---------end-----------  ");
    }
	
	
	@Test
    public void sysAdminUserSave() throws Exception {
		System.out.println("-----------------/service/api/user/save--------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        UserParameter user = new UserParameter();
        String testname ="groupadmin"+ UUID.randomUUID().toString().substring(0, 7);
        user.setUserCnname("组织管理员");
        user.setUserName(testname);
        user.setUserPassword("password");
        HttpEntity<UserParameter> entity = new HttpEntity<UserParameter>(user, headers);
        Result<Object> result = testRestTemplate.postForObject(this.serverPath+"/service/api/user/save",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/api/user/save---------end-----------  ");
    }
	
	@Test
    public void sysAdminUserUpdate() throws Exception {
		System.out.println("-----------------/service/api/user/update--------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        UserParameter user = new UserParameter();
        user.setUserCnname("组织管理员_update_new");
        user.setUserName("groupadmin5f422c9");
        user.setUserPassword("password");
        HttpEntity<UserParameter> entity = new HttpEntity<UserParameter>(user, headers);
        Result<Object> result = testRestTemplate.postForObject(this.serverPath+"/service/api/user/update",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/api/user/update---------end-----------  ");
    }
	
	@Test
    public void sysAminGoupList() throws Exception {
		System.out.println("-----------------/service/api/groups---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/api/groups",entity,Result.class);
        Map<String ,Object> rlist = result.getData();
        if(rlist!= null)
        {
	        rlist.keySet().stream().forEach(item-> {
	        	System.out.println(item+" " + rlist.get(item));
	        });
        }
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/api/groups---------end-----------  ");
    }
	
	@Test
    public void sysAdminGroupSave() throws Exception {
		System.out.println("-----------------/service/api/group/save--------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        GroupParameter group = new GroupParameter();
        
        String testname ="group"+ UUID.randomUUID().toString().substring(0, 7);
        group.setGroupAddress("address_"+UUID.randomUUID().toString());
        group.setGroupCode("code_"+ UUID.randomUUID().toString());
        group.setGroupEmail("email_"+UUID.randomUUID().toString());
        group.setGroupName("name_"+UUID.randomUUID().toString());
        group.setGroupPhone("010_"+UUID.randomUUID().toString().substring(0, 7));
        group.setGroupPic("pic_"+UUID.randomUUID().toString().substring(0, 7));
        group.setGroupService("service_"+UUID.randomUUID().toString().substring(0, 7));
        group.setGroupStatus(1);
        
        HttpEntity<GroupParameter> entity = new HttpEntity<GroupParameter>(group, headers);
        Result<Object> result = testRestTemplate.postForObject(this.serverPath+"/service/api/group/save",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/api/group/save---------end-----------  ");
    }
	
	@Test
    public void sysAdminGroupUpdate() throws Exception {
		System.out.println("-----------------/service/api/group/update--------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        GroupParameter group = new GroupParameter();
        group.setGroupGuid("ae0039fb-937e-4550-994e-b26bb23f3681");
        group.setGroupAddress("update_"+UUID.randomUUID().toString());
        group.setGroupCode("update_"+ UUID.randomUUID().toString());
        group.setGroupEmail("update_"+UUID.randomUUID().toString());
        group.setGroupName("update_"+UUID.randomUUID().toString());
        group.setGroupPhone("010_"+UUID.randomUUID().toString().substring(0, 7));
        group.setGroupPic("pic_"+UUID.randomUUID().toString().substring(0, 7));
        group.setGroupService("service_"+UUID.randomUUID().toString().substring(0, 7));
        group.setGroupStatus(1);
        HttpEntity<GroupParameter> entity = new HttpEntity<GroupParameter>(group, headers);
        Result<Object> result = testRestTemplate.postForObject(this.serverPath+"/service/api/group/update",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        
        System.out.println("-----------------/service/api/group/update---------end-----------  ");
    }

}
