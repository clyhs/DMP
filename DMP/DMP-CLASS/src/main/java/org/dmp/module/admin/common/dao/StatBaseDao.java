package org.dmp.module.admin.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.db.util.Field;
import org.dmp.core.util.StrUtil;
import org.dmp.module.common.db.JBaseDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("StatBaseDao")
@Scope("prototype")
public class StatBaseDao extends JBaseDao{

	private String[] keys;
	
	/**
	 * 获取分页报表数据
	 * @param sql
	 * @param paraMap
	 * @param start
	 * @param offset
	 * @return
	 */
	public List<DynaBean> getDataList(String paramSql,HashMap<String, Object> paraMap,int start, int offset){
		String sql = preprocAreaArray(paramSql, paraMap);
		sql = this.preprocStatement(sql);
		Field field = getField(paraMap);
		System.out.println(sql);
		return m_oDB.select(sql, field,start,offset,"mysql");
		
	}
	
	/**
	 * 获取报表数据
	 * @param sql
	 * @param paraMap
	 * @return
	 */
	public List<DynaBean> getDataList(String paramSql,HashMap<String, Object> paraMap){
		String sql = preprocAreaArray(paramSql, paraMap);
		sql = this.preprocStatement(sql);
		Field field = getField(paraMap);
		return m_oDB.select(sql, field);
	}

	/**
	 * 构造field
	 * @param paraMap
	 * @return
	 */
    private Field getField(HashMap paraMap){
        Field field = new Field();
        String[] s = this.keys;
        for(int i=0;i<keys.length;i++){
        	field.addStr(paraMap.get(keys[i]).toString());
        }
        return field;
    }

	/**
	 * 获取数据汇总
	 * @param sql
	 * @param paraMap
	 * @return
	 */
	public List<DynaBean> getDataTotal(String paramSql,HashMap<String, Object> paraMap){
		String sql = preprocAreaArray(paramSql, paraMap);
		sql = this.preprocStatement(sql);
        Field field = getField(paraMap);
		return m_oDB.select(sql, field);
	}
	
	/**
	 * 构造sql
	 * @param sql
	 * @param paraMap
	 * @return
	 */
	private String preprocStatement(String paramSql){
		Pattern patParam = Pattern.compile("(:[a-zA-Z_0-9\\$]*)");
	    Pattern patQuote = Pattern.compile("('[^']*')");
	    Pattern patMiDate = Pattern.compile("^([mM][iI])$");
	    Pattern patSsDate = Pattern.compile("^([sS][sS])$");

	    List quoteRanges = new ArrayList();
	    Matcher matcher = patQuote.matcher(paramSql);
	    
	    class MyRange{
   		   int start;
           int length;
           String text;
	    }
	    while(matcher.find()){
	    	MyRange myr = new MyRange();
	    	myr.start = matcher.start();
	    	myr.text = matcher.group();
	    	myr.length = myr.text.length();
	    	quoteRanges.add(myr);
	    }
	    matcher = patParam.matcher(paramSql);
	    List names = new ArrayList();
	    while (matcher.find()) {
	      String key = matcher.group().substring(1);
	      if (!quoteRanges.isEmpty()) {
	        int pos = matcher.start();
	        Iterator it = quoteRanges.iterator();
	        while (it.hasNext()) {
	        	MyRange r = (MyRange)it.next();
	          if ((pos >= r.start) && (pos < r.start + r.length)) {
	            break;
	          }

	        }
	      }
	      if (key.length() > 0 && (!patMiDate.matcher(key).find() && !patSsDate.matcher(key).find())) {
	    	paramSql = paramSql.replaceFirst(":" + key.replaceAll("\\Q$\\E", "\\\\\\$"), "?");
	        names.add(key);
	      }
	    }
	    this.keys = (String[]) names.toArray(new String[0]);
	    return paramSql;
	}
	
	//有省份地市查询条件的sql参数特殊处理
	private String preprocAreaArray(String sql, HashMap<String, Object> paraMap){
		//对传过来的省份/地市进行特殊处理
		if(paraMap.containsKey("areaMultiSelect") && paraMap.get("areaMultiSelect").equals("proArea")){
			
			List<String> provinceArray = new ArrayList<String>();
			List<String> areaArray = new ArrayList<String>();
			if(paraMap.get("province").equals("%")){
				provinceArray.add("isAll");
			}else{
				provinceArray.add(paraMap.get("province").toString());
			}
			if(paraMap.get("area").equals("%")){
				areaArray.add("isAll");
			}else{
				areaArray.add(paraMap.get("area").toString());			
			}
			  
		    
			String proArrayStr = "";   //省份sql拼接语句
			String areaArrayStr = "";  //地市sql拼接语句
			if(!provinceArray.get(0).equals("null")){
				if(provinceArray.get(0).equals("isAll")){
					proArrayStr += " LIKE '%' ";

				}else {
					proArrayStr += " IN (" + StrUtil.joinToString(provinceArray.get(0)) + ") ";
				}
			}else{
				proArrayStr += " LIKE '' ";
			}
			if(!areaArray.get(0).equals("null")){
				if(areaArray.get(0).equals("isAll")){
					areaArrayStr += " LIKE '%' ";

				}else{
					areaArrayStr += " IN (" + StrUtil.joinToString(areaArray.get(0)) + ") ";
				}
			}else{
				areaArrayStr += " LIKE '' ";
			}
			sql = sql.replaceAll(":province", proArrayStr);
			sql = sql.replaceAll(":area", areaArrayStr);
		}
		//对传过来的可以多选的结算账期进行特殊处理
		if(paraMap.containsKey("ymMultiSelect") && paraMap.get("ymMultiSelect").equals("ymMulti")){
			String ymStr = paraMap.get("settleTime").toString();
			String ymStrId = paraMap.get("settleTimeId").toString();
			sql = sql.replaceAll(":settleTimeId", StrUtil.joinToString(ymStrId));
			sql = sql.replaceAll(":settleTime", StrUtil.joinToString(ymStr));
//			paraMap.put("settleTimeId", joinToString(ymStrId));
//			paraMap.put("settleTime", joinToString(ymStr));
            paraMap.put("maxSettleTime",ymStrId.split(",")[0]);
		}
		//对传过来的可以多选的cp名称进行特殊处理
		if(paraMap.containsKey("cpMultiSelect") && paraMap.get("cpMultiSelect").equals("cpMulti")){
			String cpIdSqlStr = "";  //用于拼接的sql语句
			String cpSqlStr = "";
			String cpIdStr = paraMap.get("cpNameId").toString();
			String cpStr = paraMap.get("cpName").toString();
			if(cpStr.equals("%")){
				cpSqlStr = " LIKE '%' ";
				cpIdSqlStr = " LIKE '%' ";
			}else{
				cpSqlStr = " IN (" + StrUtil.joinToString(cpStr) + ") ";
				cpIdSqlStr = " IN (" + StrUtil.joinToString(cpIdStr) + ") ";
//				paraMap.put("singleCpName", cpStr.split(",")[0]);
			}
			sql = sql.replaceAll(":cpNameId", cpIdSqlStr);
			sql = sql.replaceAll(":cpName", cpSqlStr);
		}
		
	//对传过来的可以多选的长号码longNumber进行特殊处理
	if(paraMap.containsKey("longNumMultiSelect") && paraMap.get("longNumMultiSelect").equals("longNumMulti")){
		String longNumIdSqlStr = "";  //用于拼接的sql语句
		String longNumSqlStr = "";
		String longNumIdStr = paraMap.get("longNumberId").toString();
		String longNumStr = paraMap.get("longNumber").toString();
		if(longNumStr.equals("%")){
			longNumSqlStr = " LIKE '%' ";
			longNumIdSqlStr = " LIKE '%' ";
		}else{
			longNumSqlStr = " IN (" + StrUtil.joinToString(longNumStr) + ") ";
			longNumIdSqlStr = " IN (" + StrUtil.joinToString(longNumIdStr) + ") ";
//			paraMap.put("singleCpName", cpStr.split(",")[0]);
		}
		sql = sql.replaceAll(":longNumberId", longNumIdSqlStr);
		sql = sql.replaceAll(":longNumber", longNumSqlStr);
	}
		return sql;
    }
}
