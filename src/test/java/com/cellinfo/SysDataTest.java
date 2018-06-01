package com.cellinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONValue;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.controller.entity.SpatialQueryParameter;
import com.cellinfo.entity.Result;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class SysDataTest {

	private RestTemplate testRestTemplate = new RestTemplate();
	
	private String serverPath1 = "http://47.94.88.135:8181/gammaa";
	
	private String serverPath = "http://127.0.0.1:8081";

	private String token = "gamma.tl.eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdHVzZXIxIiwic2NvcGUiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwibm9uX2V4cGlyZWQiOnRydWUsImV4cCI6MTUyNzkyOTY1NiwiZW5hYmxlZCI6dHJ1ZSwibm9uX2xvY2tlZCI6dHJ1ZSwiZ3JvdXAiOiIxMzkwMzY2Yi1mZWJkLTQ3NzYtYjRhOS05ZjhlMjhmMTgxYjcifQ.3mTFjwm6rct93kfglEp0jMEyz5y50KNYvofVdgAtrfjAzcKkH4LoVUQbAibI_N_uompRiqbkBpOWgmVQFdfuEw";
	
	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
	
	
	public Polygon createPolygonByWKT() throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        Polygon mpolygon = (Polygon) reader.read("POLYGON((110 35, 110 45, 118 45, 118 35, 110 35))");
        return mpolygon;
    }
	
	@Test
    public void service_data_query() throws Exception {
		System.out.println("-----------------/service/data/query---------start-----------  ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", token );
        SpatialQueryParameter qParam = new SpatialQueryParameter();
        qParam.setLayerId("77d31b6a-93ed-4e78-aefc-d3c47865321e");
        

        Polygon mpolygon = createPolygonByWKT();
        Map<String,Object> feaMap =new HashMap<String,Object>();
        feaMap.put("type", "Feature");
    	feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(mpolygon)));
    	
        qParam.setQueryRange(feaMap);

        HttpEntity<SpatialQueryParameter> entity = new HttpEntity<SpatialQueryParameter>(qParam, headers);
        Result<List<Map<String,String>>> result = testRestTemplate.postForObject(this.serverPath+"/service/data/query",entity,Result.class);
        System.out.println(result.getData());
        Assert.assertEquals(result.getMsg(),"成功");
        System.out.println("-----------------/service/data/query---------end-----------  ");
    }
	

}
