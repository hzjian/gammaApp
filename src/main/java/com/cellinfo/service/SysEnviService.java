package com.cellinfo.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.repository.TlGammaUserRepository;

@Service
public class SysEnviService {
	
	@Autowired
	private TlGammaUserRepository tlGammaUserRepository;
	
	private Iterable<TlGammaUser> userList;	

	private RestTemplate restTemplate = new RestTemplate();

	private Map<String, String> userRangeMap = new HashMap<String, String>();

	private Map<String, String> userConvexHullMap = new HashMap<String, String>();

	private Map<String, String> userExtentMap = new HashMap<String, String>();

	private static final Logger logger = Logger.getLogger(SysEnviService.class.getName());
	
	public void loadInitData() {
		
	}

	public TlGammaUser getUserById(String userId)
	{
		for (TlGammaUser user: this.userList)
		{
			if(user.getUserName().equalsIgnoreCase(userId))
				return user;
		}
		return null;
	}
	
	private String getPlgText(String plgSql) {
		int c1 = 0;
		int c2 = 0;
		int c3 = 0;
		int strLen = plgSql.length();
		String cStr = "";
		String tmpStr = plgSql.substring(11, strLen - 2);
		String[] xyArray = tmpStr.split(",");
		String plgText = "{\"geometryType\":\"esriGeometryPolygon\",\"geometries\":[{\"rings\" :[[";
		for (int i = 0; i < xyArray.length - 1; i++) {

			cStr = xyArray[i].trim();
			c1 = cStr.indexOf('.', 0);
			c2 = cStr.indexOf(' ');
			c3 = cStr.indexOf('.', c2);
			plgText += "[" + cStr.substring(0, c1) + ',' + cStr.substring(c2 + 1, c3) + "],";
		}
		cStr = xyArray[xyArray.length - 1].trim();

		c1 = cStr.indexOf('.', 0);
		c2 = cStr.indexOf(' ');
		c3 = cStr.indexOf('.', c2);
		plgText += "[" + cStr.substring(0, c1) + ',' + cStr.substring(c2 + 1, c3) + "]]],"
				+ "\"spatialReference\" : {\"wkid\" : 3857}}]}";

		return plgText;
	}

	private String getConvexHullPlg(String sPath, String plgStr) {

		try {
			MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
			param.add("f", "json");
			param.add("sr", "3857");
			param.add("geometries", plgStr);
			String tmpStr = restTemplate.postForObject(sPath + "/Utilities/Geometry/GeometryServer/convexHull?", param,
					String.class);
			return tmpStr;
			// int strLen = tmpStr.length();
			// System.out.println(tmpStr);
			// return tmpStr.substring(49, strLen - 1);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private String getGeneralizePlg(String sPath, String plgStr) {
		try {
			MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();

			param.add("f", "json");
			param.add("sr", "3857");
			param.add("maxDeviation", 200);
			param.add("geometries", plgStr);
			String tmpStr = restTemplate.postForObject(sPath + "/Utilities/Geometry/GeometryServer/generalize?", param,
					String.class);
			return tmpStr;
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private String getPlgExtent(String plgSql) {

		double minx = 0, miny = 0, maxx = 0, maxy = 0;
		int strLen = plgSql.length();
		String cStr = "";
		String tmpStr = plgSql.substring(11, strLen - 2);
		String[] xyArray = tmpStr.split(",");

		cStr = xyArray[0].trim();
		int cIdx = cStr.indexOf(' ');

		double tmpX = 0, tmpY;

		try {
			tmpX = Double.parseDouble(cStr.substring(0, cIdx));
			minx = tmpX;
			maxx = tmpX;
			tmpY = Double.parseDouble(cStr.substring(cIdx + 1));
			miny = tmpY;
			maxy = tmpY;

		} catch (Exception e) {
			// TODO: handle exception
		}
		for (int i = 0; i < xyArray.length - 1; i++) {

			cStr = xyArray[i].trim();
			cIdx = cStr.indexOf(' ');
			try {
				tmpX = Double.parseDouble(cStr.substring(0, cIdx));
				if (minx > tmpX)
					minx = tmpX;
				if (maxx < tmpX)
					maxx = tmpX;
				tmpY = Double.parseDouble(cStr.substring(cIdx + 1));
				if (miny > tmpY)
					miny = tmpY;
				if (maxy < tmpY)
					maxy = tmpY;

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return "{\"xmin\":" + minx + ",\"ymin\":" + miny + ",\"xmax\":" + maxx + ",\"ymax\":" + maxy
				+ ",\"spatialReference\":{\"wkid\":3857,\"latestWkid\":3857}}";
	}

	/**
	 * @return the userRangeMap
	 */
	public Map<String, String> getUserRangeMap() {
		return userRangeMap;
	}

	/**
	 * @param userRangeMap the userRangeMap to set
	 */
	public void setUserRangeMap(Map<String, String> userRangeMap) {
		this.userRangeMap = userRangeMap;
	}

	/**
	 * @return the userConvexHullMap
	 */
	public Map<String, String> getUserConvexHullMap() {
		return userConvexHullMap;
	}

	/**
	 * @param userConvexHullMap the userConvexHullMap to set
	 */
	public void setUserConvexHullMap(Map<String, String> userConvexHullMap) {
		this.userConvexHullMap = userConvexHullMap;
	}

	/**
	 * @return the userExtentMap
	 */
	public Map<String, String> getUserExtentMap() {
		return userExtentMap;
	}

	/**
	 * @param userExtentMap the userExtentMap to set
	 */
	public void setUserExtentMap(Map<String, String> userExtentMap) {
		this.userExtentMap = userExtentMap;
	}

	
	
	
}
