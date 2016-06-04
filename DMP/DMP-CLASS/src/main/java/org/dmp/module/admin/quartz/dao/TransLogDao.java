package org.dmp.module.admin.quartz.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.JBaseDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("TransLogDao")
@Scope("prototype")
public class TransLogDao extends JBaseDao{
	
	/**
	 * 获取transformation的指定转换日志
	 * @param transName
	 * @return
	 */
	public List<DynaBean> getTransList(String transName, int start, int offset){
		String OracleSql = "select ID_BATCH , ERRORS , ENDDATE, LINES_INPUT, LINES_OUTPUT," +
                     " LINES_READ, LINES_REJECTED, LINES_UPDATED, LINES_WRITTEN," +
                     " STARTDATE, LOGDATE, STATUS, TRANSNAME, (CASE WHEN ERRORS = 0 THEN  EMPTY_CLOB()"+
                     " ELSE LOG_FIELD END) LOG_FIELD "+
                 " from TL_TRANSFORMATION  where TRANSNAME  like ? "+
	             " order by LOGDATE desc";
		String mysqlSql = "select ID_BATCH , ERRORS , ENDDATE, LINES_INPUT, LINES_OUTPUT," +
                " LINES_READ, LINES_REJECTED, LINES_UPDATED, LINES_WRITTEN," +
                " STARTDATE, LOGDATE, STATUS, TRANSNAME, (CASE WHEN ERRORS = 0 THEN  null "+
                " ELSE LOG_FIELD END) LOG_FIELD "+
            " from TL_TRANSFORMATION  where TRANSNAME  like ? "+
            " order by LOGDATE desc";
		if(start==0 && offset ==0){
			return m_oDB.select(mysqlSql,new Field().addStr("%"+transName+"%"));
		}

		return m_oDB.select(mysqlSql,new Field().addStr("%"+transName+"%"),start, offset,"mysql");
	}
	
	/**
	 * 获取job的指定转换日志
	 * @param jobName
	 * @return
	 */
	public List<DynaBean> getStepList(String jobName,int start, int offset){
		String sql = "select TRANSNAME,STEPNAME,ID_BATCH,LINES_READ,LINES_WRITTEN,LINES_INPUT,LINES_OUTPUT,ERRORS,LOG_DATE " +
				" from TL_STEP where TRANSNAME  like ? "+
	            " order by LOG_DATE desc";
		if(start==0 && offset ==0){
			return m_oDB.select(sql,new Field().addStr("%"+jobName+"%"));
		}

		return m_oDB.select(sql,new Field().addStr("%"+jobName+"%"), start, offset,"mysql");
	}

}
