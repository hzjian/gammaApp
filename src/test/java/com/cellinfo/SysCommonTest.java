package com.cellinfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.controller.entity.DictItemParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.service.DictParameter;

public class SysCommonTest {

	private RestTemplate testRestTemplate = new RestTemplate();
		
	private String serverPath1 = "http://47.94.88.135:8181/gammaa";
	
	private String serverPath = "http://127.0.0.1:8081";
	
	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdHVzZXIxIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwibm9uX2V4cGlyZWQiOnRydWUsImV4cCI6MTUyNzAzNzM2MywiZW5hYmxlZCI6dHJ1ZSwibm9uX2xvY2tlZCI6dHJ1ZSwiZ3JvdXAiOiIxMzkwMzY2Yi1mZWJkLTQ3NzYtYjRhOS05ZjhlMjhmMTgxYjcifQ.hLCaBcj0ghfHtWsHkeRrSEcxFUNf02vFR7w1ztxpIAklJ50MPA0jgH5oix7wZ9wZADlJ0Bx21jmrv-YzJV-ERg";
	
	@Test
	public void common_dict_save() throws Exception {
		System.out.println("-----------------/service/common/dict/save---------start-----------  ");
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("x-auth-token", token );
	    
	    DictParameter para = new DictParameter();
	    para.setDictDesc("dictDesc"+UUID.randomUUID().toString());
	    para.setDictName("dictName"+UUID.randomUUID().toString());
	    
	    HttpEntity<DictParameter> entity = new HttpEntity<DictParameter>(para, headers);
	    Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/common/dict/save",entity,Result.class);
	    Map<String ,Object> rlist = result.getData();
	    rlist.keySet().stream().forEach(key -> System.out.println(key+" " + rlist.get(key)));
	    Assert.assertEquals(result.getMsg(),"成功");
	    System.out.println("-----------------/service/common/dict/save---------end-----------  ");
	}
	
	@Test
	public void common_dicts() throws Exception {
		System.out.println("-----------------/service/common/dicts---------start-----------  ");
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("x-auth-token", token );
	    
	    RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
	    
	    HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
	    Result<List<Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/common/dicts",entity,Result.class);
	    List<Object> rlist = result.getData();
	    rlist.stream().forEach(item -> System.out.println(item));
	    Assert.assertEquals(result.getMsg(),"成功");
	    System.out.println("-----------------/service/common/dicts---------end-----------  ");
	}
	
	@Test
	public void common_dict_additem() throws Exception {
		System.out.println("-----------------/service/common/dict/additem---------start-----------  ");
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("x-auth-token", token );
	    
	    DictItemParameter para = new DictItemParameter();
	    para.setDictId("e805c56e-e486-4e28-8440-08b82ad2f038");
	    para.setDictItem("dictName"+UUID.randomUUID().toString());
	    
	    HttpEntity<DictItemParameter> entity = new HttpEntity<DictItemParameter>(para, headers);
	    Result<Map<String ,Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/common/dict/additem",entity,Result.class);
	    Map<String ,Object> rlist = result.getData();
	    rlist.keySet().stream().forEach(key -> System.out.println(key+" " + rlist.get(key)));
	    Assert.assertEquals(result.getMsg(),"成功");
	    System.out.println("-----------------/service/common/dict/additem---------end-----------  ");
	}
}
