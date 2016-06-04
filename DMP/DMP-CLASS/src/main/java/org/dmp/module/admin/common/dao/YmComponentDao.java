package org.dmp.module.admin.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.JBaseDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("YmComponentDao")
@Scope("prototype")
public class YmComponentDao extends JBaseDao{

	/**
	 * 可单选或多选的年月控件
	 * oracle
	 * @return
	 */
	public List<DynaBean> getYMComboBoxList(int ymRange){
		int rowNumSize = ymRange-(ymRange*2);
		String sql = "SELECT TO_CHAR(ADD_MONTHS(ADD_MONTHS(sysdate, ?),ROWNUM),'yyyymm') ymId "
                + "FROM DUAL CONNECT BY ROWNUM <= ? ORDER BY ymId DESC";
		List<DynaBean> list = m_oDB.select(sql,new Field().addInt(rowNumSize).addInt(ymRange));
		return list;
		
	}
	
	//select date_format(date_sub(curdate(), interval 1 month),'%Y%m')
	
	public List<DynaBean> getYMComboBoxList(int ymRange,String type){
		int rowNumSize = ymRange-(ymRange*2);
		List<DynaBean> list = new ArrayList<DynaBean>(); 
		if(type.toLowerCase().equalsIgnoreCase("mysql")){
			String sql ="select date_format(date_sub(curdate(), interval ? month),'%Y%m') ymId ";
			for(int i=0;i<ymRange;i++){
				List l= m_oDB.select(sql,new Field().addInt(i));
				DynaBean dbean = (DynaBean) l.get(0);
				System.out.println(dbean.get("ymId"));
				list.add(dbean);
			}
		}else{
			String sql = "SELECT TO_CHAR(ADD_MONTHS(ADD_MONTHS(sysdate, ?),ROWNUM),'yyyymm') ymId "
	                + "FROM DUAL CONNECT BY ROWNUM <= ? ORDER BY ymId DESC";
			list = m_oDB.select(sql,new Field().addInt(rowNumSize).addInt(ymRange));
		}
		
		
		 
		return list;
		
	}
}
