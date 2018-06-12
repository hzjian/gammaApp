package com.cellinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONValue;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.cellinfo.controller.entity.SpatialQueryParameter;
import com.cellinfo.entity.Result;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class SysDataTest {

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	
	private String serverPath1 = "http://47.94.88.135:8181/gammaa";
	
	private String serverPath = "http://127.0.0.1:8081";

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdHVzZXIxIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwibm9uX2V4cGlyZWQiOnRydWUsImV4cCI6MTUyODk2MTg5OCwiZW5hYmxlZCI6dHJ1ZSwibm9uX2xvY2tlZCI6dHJ1ZSwiZ3JvdXAiOiIxMzkwMzY2Yi1mZWJkLTQ3NzYtYjRhOS05ZjhlMjhmMTgxYjcifQ.W-bRRIMGVGDM_pJ2mxnJlsrA3NtBojUSOrDh57FCjVLbZSae2Xim-AlGkRqHTHrwr4QsUi_on_v7kwsgF4cxeg";
	
	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
	
	
	public Polygon createPolygonByWKT() throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        //Polygon mpolygon = (Polygon) reader.read("POLYGON((110 35, 110 45, 118 45, 118 35, 110 35))");
        Polygon mpolygon = (Polygon) reader.read("POLYGON((116.378 39.859, 116.378 39.974, 116.453 39.974, 116.453 39.859, 116.378 39.859))");
        //Polygon mpolygon = (Polygon) reader.read("POLYGON((116.402 39.926, 116.402 39.947, 116.419 39.947, 116.419 39.926, 116.402 39.926))");
        //Polygon mpolygon = (Polygon) reader.read("POLYGON((116.402 39.94, 116.402 39.947, 116.411 39.947, 116.411 39.94, 116.402 39.94))");
        return mpolygon;
    }
	
	@Test
    public void service_data_query() throws Exception {
		System.out.println("-----------------/service/data/query---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        SpatialQueryParameter qParam = new SpatialQueryParameter();
        //qParam.setLayerId("77d31b6a-93ed-4e78-aefc-d3c47865321e");
        //qParam.setLayerId("d3425a96-c83c-4751-b734-0f4caa7f5b64");
        qParam.setLayerId("34b0c7e1-3b9c-4d96-9413-3031d909b5e8");

        Polygon mpolygon = createPolygonByWKT();
        Map<String,Object> feaMap =new HashMap<String,Object>();
        feaMap.put("type", "Feature");
    	feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(mpolygon)));
    	
        qParam.setQueryRange(feaMap);
        long startTime = System.currentTimeMillis();
        HttpEntity<SpatialQueryParameter> entity = new HttpEntity<SpatialQueryParameter>(qParam, headers);
        Result<List<Map<String,String>>> result = testRestTemplate.postForObject(this.serverPath+"/service/data/query",entity,Result.class);
        //System.out.println(result.getData());
        System.out.println("rownum =="+result.getData().size());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("service/data/query wastelTime== "+(System.currentTimeMillis()-startTime));
        System.out.println("-----------------/service/data/query---------end-----------  ");
    }
	
	
	@Test
    public void service_data_query_count() throws Exception {
		System.out.println("-----------------/service/data/query/count---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        SpatialQueryParameter qParam = new SpatialQueryParameter();
        //qParam.setLayerId("77d31b6a-93ed-4e78-aefc-d3c47865321e");
        //qParam.setLayerId("d3425a96-c83c-4751-b734-0f4caa7f5b64");
        qParam.setLayerId("34b0c7e1-3b9c-4d96-9413-3031d909b5e8");

        Polygon mpolygon = createPolygonByWKT();
        Map<String,Object> feaMap =new HashMap<String,Object>();
        feaMap.put("type", "Feature");
    	feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(mpolygon)));
    	
        qParam.setQueryRange(feaMap);
        long startTime = System.currentTimeMillis();
        HttpEntity<SpatialQueryParameter> entity = new HttpEntity<SpatialQueryParameter>(qParam, headers);
        Result<Long> result = testRestTemplate.postForObject(this.serverPath+"/service/data/query/count",entity,Result.class);
        //System.out.println(result.getData());
        System.out.println("rownum =="+result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("service/data/query/count wastelTime== "+(System.currentTimeMillis()-startTime));
        System.out.println("-----------------/service/data/query/count---------end-----------  ");
    }
	
	

}
