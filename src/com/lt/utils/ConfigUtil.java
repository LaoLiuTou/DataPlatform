package com.lt.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;


public class ConfigUtil {
	
	public static String configCon=null;
    
    public static String getConfig(){
    	if(configCon==null){
    		configCon=readProperties();
    	}
    	else{
    		 
    	}
    	return configCon;
    }
 
    
	public static String readProperties(){
		Logger logger = Logger.getLogger("DipLogger");
		String resultStr="";
		try {
			Properties props = new Properties();  
	      	props.load(ConfigUtil.class.getClassLoader().getResourceAsStream("dbpool.properties"));  
	      	Map<String, String> hashMap = new HashMap<String, String>();    
	      	hashMap.put("dbType", props.getProperty("dbType").trim());
	      	hashMap.put("dbUser", props.getProperty("dbUser").trim());
	      	hashMap.put("dbPassword", props.getProperty("dbPassword").trim());
	      	hashMap.put("dbHost", props.getProperty("dbHost").trim());
	      	hashMap.put("dbPort", props.getProperty("dbPort").trim());
	      	hashMap.put("dbName", props.getProperty("dbName").trim());
	      	ObjectMapper objectMapper = new ObjectMapper();    
			resultStr=objectMapper.writeValueAsString(hashMap); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("本地数据源配置错误！");
			e.printStackTrace();
		}
		return resultStr;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
