package com.cellinfo.controller;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.geotools.geojson.geom.GeometryJSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.entity.Result;
import com.cellinfo.utils.ResultUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

@RestController
@RequestMapping("/service/geomtry")
public class SpatialOpController {

	
	@GetMapping(value = "/getGeoData")
	public Result<Map<String, Object>> getGeoData() throws Exception {
		Map<String, Object> dMap = new HashMap<String, Object>();

//		fjson.writeFeature(p, writer);
//		String json = writer.toString();

		return ResultUtil.success(dMap);
	}
	
	public String geoToJson(Geometry geom){  
		GeometryJSON g = new GeometryJSON(15);
		StringWriter sw = new StringWriter();
	    try {
	        g.write(geom, sw);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    System.out.println(sw.toString());
        return sw.toString();  
	}
	
	public Geometry geoToGeometry(String geoJson){  
		GeometryJSON gjson = new GeometryJSON(15);
		Reader reader = new StringReader(geoJson);  
        try{  
            //Geometry geometry = gjson.read(reader);  
            return gjson.read(reader);
        }catch(IOException e){  
            e.printStackTrace();  
        }  
        return null;  
    } 
	
	public String geoToJson(String wkt){  
        String json = null;  
        try{  
            WKTReader reader = new WKTReader();  
            Geometry geometry = reader.read(wkt);  
            StringWriter writer = new StringWriter();  
            GeometryJSON g = new GeometryJSON();  
            g.write(geometry,writer);  
            json = writer.toString();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        return json;  
    } 
	public String jsonToWkt(String geoJson){  
        String wkt = null;  
        GeometryJSON gjson = new GeometryJSON();  
        Reader reader = new StringReader(geoJson);  
        try{  
            Geometry geometry = gjson.read(reader);  
            wkt = geometry.toText();  
        }catch(IOException e){  
            e.printStackTrace();  
        }  
        return wkt;  
    }  
}
