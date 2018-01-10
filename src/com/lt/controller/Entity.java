package com.lt.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lt.utils.ConfigUtil;
import com.lt.utils.JdbcUtils;
@Controller
public class Entity{

  
	Logger logger = Logger.getLogger("DipLogger");
	
	@RequestMapping("/Entity") 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void operate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		 
		JSONObject resultJO=new JSONObject();
		Class c = this.getClass();//获得当前类的Class对象
		
		String name = request.getParameter("method");//获取方法名
		String userName = (String) request.getAttribute("userName");//用户名
		logger.info("用户("+userName+")执行方法："+getClass().getName()+"-"+name);
        if(name == null || name.isEmpty()){
        	resultJO.put("status", "-1");
        	resultJO.put("msg", "没有传递method参数,请给出你想调用的方法");
        	logger.info("没有传递method参数,请给出你想调用的方法");
        }
        else{
            Method method = null;
            try {
                //获得Method对象
                method =  c.getMethod(name, HttpServletRequest.class,HttpServletResponse.class);
                try {
                    String mResult=(String) method.invoke(this, request,response);//反射调用方法
                    resultJO = JSONObject.fromObject(mResult);
                    //resultJO.put("status", "0");
                	//resultJO.put("msg", mResult);
                } catch (Exception e) {
                	resultJO.put("status", "-1");
                	resultJO.put("msg", "你调用的方法"+name+",内部发生了异常");
                	logger.info("你调用的方法"+name+",内部发生了异常");
                	e.printStackTrace();
                }
            } catch (Exception e) {
            	resultJO.put("status", "-1");
            	resultJO.put("msg", "没有找到"+name+"方法，请检查该方法是否存在");
            	logger.info("没有找到"+name+"方法，请检查该方法是否存在");
            	e.printStackTrace(); 
            }
        }
		out.print(resultJO.toString());
		out.flush();
		out.close();
	}

	/**
	 * 查询列表
	 * @param request(dbInfo,param)
	 * @param response
	 * @return
	 */
	public String query(HttpServletRequest request, HttpServletResponse response){
		//通过id查询数据源信息
		//String dbs_id = request.getParameter("dbs_id"); 
		//String dbInfo = JdbcUtils.getDbInfoByDbsid(dbs_id);//获取数据库信息
		String userId = (String) request.getAttribute("userId");//用户id
		String param = request.getParameter("param"); 
		return JdbcUtils.query(userId,ConfigUtil.getConfig(), param, "SYS_ENTITIES");
	}
	/**
	 * 删除
	 * @param request(dbInfo,param)
	 * @param response
	 * @return
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response){
		String result="";
		//通过id查询数据源信息
		//String dbs_id = request.getParameter("dbs_id");//获取数据库信息
		String ent_id = request.getParameter("ent_id");//实体id
		String dbInfo = JdbcUtils.getDbInfoByEid(ent_id);//获取数据库信息
		String userId = (String) request.getAttribute("userId");//用户id
		String tableName = request.getParameter("tableName"); 
		//删除本地表中记录
		String localTN="SYS_ENTITIES";
		JSONObject  paramJO = new JSONObject();
		paramJO.put("id", ent_id);
		JSONObject dropResult=JSONObject.fromObject(
				JdbcUtils.delete(userId,ConfigUtil.getConfig(), paramJO.toString(), localTN));
		if(dropResult.getString("status").equals("0")){
			//删除数据表
			result=JdbcUtils.deleteTable(dbInfo, tableName);
		}
		else{
			result=dropResult.toString();
		}
		return result;
	}
	/**
	 * 新建实体
	 * @param request(dbInfo,param)
	 * @param response
	 * @return
	 */
	public String create(HttpServletRequest request, HttpServletResponse response){
		String result="";
		//通过id查询数据源信息
		String userId = (String) request.getAttribute("userId");//用户id
		String dat_id = request.getParameter("dat_id"); 
		String dbInfo = JdbcUtils.getDbInfoByDatid(dat_id);//获取数据库信息
		String param = request.getParameter("param"); 
		String nm_t = request.getParameter("nm_t"); 
		//String tableName = request.getParameter("tableName"); 
		logger.info("数据库信息："+dbInfo);
		logger.info("数据库操作-新建实体");
		JSONObject createResult=JSONObject.fromObject(JdbcUtils.createTable(dbInfo, param, nm_t));
		if(createResult.getString("status").equals("0")){
			//同时在实体表中插入数据
 
			String mem_id = request.getParameter("mem_id"); 
			String status = request.getParameter("status"); 
			//String nm_t = request.getParameter("nm_t"); 
			String desc_t = request.getParameter("desc_t"); 
			String code_t = request.getParameter("code_t"); 
			
			JSONObject localParam=new JSONObject();
			localParam.put("mem_id", mem_id);
			localParam.put("status", status);
			localParam.put("nm_t", nm_t);
			localParam.put("desc_t", desc_t);
			localParam.put("code_t", code_t);
			localParam.put("dat_id", dat_id);
			String localTN="SYS_ENTITIES";
			JSONObject insertResult=JSONObject.fromObject(
					JdbcUtils.insert(userId,ConfigUtil.getConfig(), localParam.toString(), localTN));
			////////////////////////
			if(insertResult.getString("status").equals("0")){
				result=createResult.toString();
			}
			else{
				JdbcUtils.deleteTable(dbInfo, nm_t);
				result=insertResult.toString();
			}
		}
		else{
			result=createResult.toString();
		}

		
		return result;
	}
 

}
