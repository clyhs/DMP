package org.dmp.module.admin.common.dao;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.JBaseDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("ReportBaseDao")
@Scope("prototype")
public class ReportBaseDao  extends JBaseDao{

	public List<DynaBean> getDataList(String sql,LinkedHashMap<String, Object> paraMap,int start, int offset){
		if (null!=paraMap ){
            Field field = getField(paraMap);
			return m_oDB.select(sql, field,start,offset,"mysql");
		}
		return m_oDB.select(sql,start,offset);
		
	}
	
	public List<DynaBean> getDataList(String sql,LinkedHashMap<String, Object> paraMap){
		if (null!=paraMap ){
            Field field = getField(paraMap);
			return m_oDB.select(sql, field);
		}
		return m_oDB.select(sql);
	}

    private Field getField(Map paraMap){
        Field field = new Field();
        Iterator it = paraMap.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            field.addStr((String)entry.getValue());
        }
        return field;
    }

	
	public List<DynaBean> getDataTotal(String sql,LinkedHashMap<String, Object> paraMap){
		if (null!=paraMap ){
            Field field = getField(paraMap);
			return m_oDB.select(sql, field);
		}
		return m_oDB.select(sql);
	}
}
