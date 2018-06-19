package com.cellinfo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.cellinfo.controller.entity.CommentParameter;
import com.cellinfo.controller.entity.FileParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.entity.Result;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

public class SysFileTest {

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	
	private String serverPath1 = "http://47.94.88.135:8181/gammaa";
	
	private String serverPath = "http://127.0.0.1:8081";

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5dWhhbzEyMyIsInNjb3BlIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sIm5vbl9leHBpcmVkIjp0cnVlLCJleHAiOjE1Mjk1Njc2MjMsImVuYWJsZWQiOnRydWUsIm5vbl9sb2NrZWQiOnRydWUsImdyb3VwIjoiMGY3N2FjMWYtYTg5MC00NGViLTg5MjQtNzQ1OWNjNDlkNWJhIn0.Zs-gTGvRRhlakha4Cz0Gw_JYxUQxFBXDsN84QtBKVakplWPxqItaoajX3J8jVMXHnijqGNFjFjFct3nPkpjTmg";
	
	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
	
	
	@Test
    public void service_task_file_upload() throws Exception {
		System.out.println("-----------------/service/file/task/upload---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("file", new FileSystemResource(new File("D:/gamma.war")));
        multiValueMap.add("id", "f4f2993b-fee1-473c-9c2e-47a7e7976789");
        
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(multiValueMap, headers);
        
        Result<Map<String,String>> result = testRestTemplate.postForObject(this.serverPath+"/service/file/task/upload",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/file/task/upload---------end-----------  ");
    }
	
	@Test
    public void service_task_file_list() throws Exception {
		System.out.println("-----------------/service/file/filelist---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setSkey("34b0c7e1-3b9c-4d96-9413-3031d909b5e8");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        
        Result<Map<String,String>> result = testRestTemplate.postForObject(this.serverPath+"/service/file/filelist",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/file/filelist---------end-----------  ");
    }
	
	
	@Test
    public void service_delete_file() throws Exception {
		System.out.println("-----------------/service/file/deletefile---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        FileParameter para = new FileParameter();
        para.setFileId("3b43a2ac-7ca7-4d39-b968-87cb95708374");
        
        HttpEntity<FileParameter> entity = new HttpEntity<FileParameter>(para, headers);
        
        Result<Map<String,String>> result = testRestTemplate.postForObject(this.serverPath+"/service/file/deletefile",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/file/deletefile---------end-----------  ");
    }
	
	

	@Test
    public void service_file_download() throws Exception {
		System.out.println("-----------------/service/file/download---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        FileParameter para = new FileParameter();
        para.setFileId("d1cf786f-2de9-4e97-a3d0-941e2ea11c61");
        
        HttpEntity<FileParameter> entity = new HttpEntity<FileParameter>(para, headers);
        
        byte[] response = testRestTemplate.postForObject(this.serverPath+"/service/file/download",entity, byte[].class);
        if (response!=null) {
        	byte[] filedata = response;
            InputStream  inputStream = new ByteArrayInputStream(filedata);
            OutputStream os = new FileOutputStream(new File("d:/test.war"));
            readAndWrite(inputStream, os);
        }
        System.out.println("-----------------/service/file/download---------end-----------  ");
    }

	private void readAndWrite(final InputStream is, OutputStream os)
			throws IOException {
		byte[] data = new byte[2048];
		int read = 0;
		while ((read = is.read(data)) > 0) {
			os.write(data, 0, read);
		}
		os.flush();
	}
	
	
	@Test
    public void service_taskcomment_save() throws Exception {
		System.out.println("-----------------/service/user/taskcomment/save---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        CommentParameter para = new CommentParameter();
        para.setTaskId("7a62197d-5178-4f0b-910e-5f72a563c06a");
        para.setComment(UUID.randomUUID().toString());
        
        HttpEntity<CommentParameter> entity = new HttpEntity<CommentParameter>(para, headers);
        
        Result<Map<String,String>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/taskcomment/save",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/taskcomment/save---------end-----------  ");
    }
	
	
	@Test
    public void service_taskcomment_list() throws Exception {
		System.out.println("-----------------/service/user/taskcomment/list---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        
        RequestParameter para = new RequestParameter();
        para.setPage(0);
        para.setPageSize(10);
        para.setSortDirection("DESC");
        para.setSkey("7a62197d-5178-4f0b-910e-5f72a563c06a");
        
        HttpEntity<RequestParameter> entity = new HttpEntity<RequestParameter>(para, headers);
        Result<Map<String, Object>> result = testRestTemplate.postForObject(this.serverPath+"/service/user/taskcomment/list",entity,Result.class);
        Map<String, Object> rlist = result.getData();

        System.out.println(rlist);
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/user/taskcomment/list---------end-----------  ");
    }
	
}
