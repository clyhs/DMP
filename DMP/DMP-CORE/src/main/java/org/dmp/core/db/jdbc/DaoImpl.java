package org.dmp.core.db.jdbc;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.dmp.core.db.util.Field;
import org.dmp.core.db.util.FieldVo;
import org.dmp.core.log.Log;
import org.dmp.core.log.LogFactory;
import org.dmp.core.util.LogTags;
import org.dmp.core.util.Tools;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * 数据库管理类
 * 
 * @Author 伍锐凡
 * @Date 2012-3-17
 * @Version 1.0
 * @Remark
 */
public class DaoImpl extends JdbcDaoSupport implements IDao<DynaBean> {
	// private static final Logger m_oLog = Logger.getLogger(DaoImpl.class);
	private static final Log m_oLog = LogFactory.Instance(DaoImpl.class);

	public List<DynaBean> select(String sSQL) {
		return select(sSQL, null);
	}

	public List<DynaBean> select(String sSQL, int nStart) {
		return select(sSQL, null, nStart, m_nOffset);
	}

	public List<DynaBean> select(String sSQL, int nStart, int nOffset) {
		return select(sSQL, null, nStart, nOffset);
	}

	public List<DynaBean> select(String sSQL, Field oField, int nStart) {
		return select(sSQL, oField, nStart, m_nOffset);
	}

	public List<DynaBean> select(String sSQL, Field oField, int nStart,
			int nOffset) {
		int nEnd = nStart + nOffset;
		StringBuffer sSQLBuffer = new StringBuffer();
		sSQLBuffer.append("SELECT              \r\n")
				.append("    *               \r\n")
				.append("FROM                \r\n")
				.append("    ( SELECT        \r\n")
				.append("        A.*         \r\n")
				.append("        ,rownum rownum_ \r\n")
				.append("    FROM            \r\n")
				.append("        (" + sSQL + ") A        \r\n")
				.append("    WHERE           \r\n")
				.append("        rownum <= " + String.valueOf(nEnd) + " \r\n")
				.append("    )  B              \r\n")
				.append("WHERE               \r\n")
				.append("    rownum_ > " + String.valueOf(nStart) + " \r\n");

		return select(sSQLBuffer.toString(), oField);
	}

	public List<DynaBean> select(String sSQL, Field oField, int nStart,
			int nOffset, String type) {
		int nEnd = nStart + nOffset;
		StringBuffer sSQLBuffer = new StringBuffer();
		if (type.toLowerCase().equalsIgnoreCase("mysql")) {
			sSQLBuffer.append(sSQL).append(" limit " + nStart + ", ")
					.append("" + nOffset + "");

		}
		if (type.toLowerCase().equalsIgnoreCase("oracle")) {
			sSQLBuffer.append("SELECT              \r\n")
					.append("    *               \r\n")
					.append("FROM                \r\n")
					.append("    ( SELECT        \r\n")
					.append("        A.*         \r\n")
					.append("        ,rownum rownum_ \r\n")
					.append("    FROM            \r\n")
					.append("        (" + sSQL + ") A        \r\n")
					.append("    WHERE           \r\n")
					.append("        rownum <= " + String.valueOf(nEnd)
							+ " \r\n")
					.append("    )  B              \r\n")
					.append("WHERE               \r\n")
					.append("    rownum_ > " + String.valueOf(nStart) + " \r\n");
		}

		return select(sSQLBuffer.toString(), oField);
	}

	public List<DynaBean> select(String sSQL, Field oField) {
		long nStartTime = System.currentTimeMillis();
		String sCallStack = beginLog(new Throwable().getStackTrace());
		String sError = "";
		List<DynaBean> aDataSource = null;

		try {
			aDataSource = getJdbcTemplate().query(sSQL,
					new DBPreparedStatementSetter(oField), new DBRowMapper());
		} catch (DataAccessException oDataAccessException) {
			sError = oDataAccessException.toString();
		} finally {
			// System.err.println(sError +",current time: "+ nStartTime+
			// ", sCallStack:" + sCallStack);
			// endLog(nStartTime, sCallStack, sSQL, oField, sError);
		}

		return aDataSource;
	}

	public int insert(String sSQL) {
		return insert(sSQL, new Field());
	}

	public int insert(String sSQL, Field oField) {
		return exec(sSQL, oField);
	}

	public int insert(String sSQL, List<Field> aField) {
		return exec(sSQL, aField);
	}

	public int update(String sSQL) {
		return update(sSQL, new Field());
	}

	public int update(String sSQL, Field oField) {
		return exec(sSQL, oField);
	}

	public int update(String sSQL, List<Field> aField) {
		return exec(sSQL, aField);
	}

	public int delete(String sSQL) {
		return delete(sSQL, new Field());
	}

	public int delete(String sSQL, Field oField) {
		return exec(sSQL, oField);
	}

	public int delete(String sSQL, List<Field> aField) {
		return exec(sSQL, aField);
	}

	/**
	 * 执行SQL
	 * 
	 * @param sSQL
	 * @param oField
	 * @return int
	 */
	private int exec(String sSQL, Field oField) {
		long nStartTime = System.currentTimeMillis();
		int nRetTotal = -1;
		String sCallStack = beginLog(new Throwable().getStackTrace());
		String sError = null;

		try {
			nRetTotal = getJdbcTemplate().update(sSQL,
					new DBPreparedStatementSetter(oField));
		} catch (DataAccessException oDataAccessException) {
			sError = oDataAccessException.toString();
		} finally {
			endLog(nStartTime, sCallStack, sSQL, oField, sError);
		}

		return nRetTotal;
	}

	private int exec(String sSQL, List<Field> aField) {
		long nStartTime = System.currentTimeMillis();
		int nRetTotal = -1;
		String sCallStack = beginLog(new Throwable().getStackTrace());
		String sError = null;
		try {
			int[] aTotal = getJdbcTemplate().batchUpdate(sSQL,
					new DBBatchPreparedStatementSetter(aField));
			nRetTotal = 0;
			for (int i = 0; i < aTotal.length; i++)
				nRetTotal += aTotal[i];
		} catch (DataAccessException oDataAccessException) {
			sError = oDataAccessException.toString();
		} finally {
			endLog(nStartTime, sCallStack, sSQL, null, sError);
		}

		return nRetTotal;
	}

	/**
	 * 开始日志
	 * 
	 * @param aStackTrace
	 * @return String
	 */
	private String beginLog(StackTraceElement[] aStackTrace) {
		StringBuffer sCallStack = new StringBuffer();
		for (int i = 0, nLen = aStackTrace.length; i < nLen; i++) {
			sCallStack.append(aStackTrace[i].toString() + "\r\n");
			if (i > 1)
				break;
		}

		return sCallStack.toString();
	}

	/**
	 * 结束日志
	 * 
	 * @param nStartTime
	 * @param sCallStack
	 * @param sSQL
	 * @param oField
	 * @param sError
	 */
	private void endLog(long nStartTime, String sCallStack, String sSQL,
			Field oField, String sError) {
		long nEndTime = System.currentTimeMillis();

		if (oField != null) {
			Pattern oPattern = Pattern.compile("(?imu)\\?");
			String[] aSQLTpl = oPattern.split(sSQL);
			StringBuffer sSQLBuffer = new StringBuffer();
			List<FieldVo> aParam = oField.getFields();
			int nIndex = 0;
			for (FieldVo oFieldVo : aParam) {
				if (oFieldVo == null)
					continue;
				sSQLBuffer.append(aSQLTpl[nIndex].toString());
				switch (oFieldVo.m_nType) {
				case Types.INTEGER:
				case Types.FLOAT:
				case Types.DOUBLE:
					sSQLBuffer.append(oFieldVo.m_oValue.toString());
					break;
				default:
					sSQLBuffer.append("'" + oFieldVo.m_oValue.toString() + "'");
					break;
				}
				nIndex++;
			}
			for (int i = nIndex, nLen = aSQLTpl.length; i < nLen; i++) {
				sSQLBuffer.append(aSQLTpl[i]);
			}
			if (nIndex > 0) {
				sSQL = sSQL.toString();
			}
		}
		StringBuffer sLog = new StringBuffer();
		sLog.append("执行时间:" + Tools.getNow()).append("\r\n");
		sLog.append("执行栈:" + sCallStack).append("\r\n");
		sLog.append("执行SQL：" + sSQL).append("\r\n");
		sLog.append("耗时:" + (nEndTime - nStartTime)).append("ms\r\n");
		if (sError != null) {
			sLog.append("错误:" + sError).append("\r\n");
			m_oLog.append(LogTags.ERROR, sError).error();
		}
		m_oLog.append(LogTags.DEBUG, sLog.toString()).debug();
	}
}

/**
 * 
 * 作者：伍锐凡 创建：2011-2-28 版本：Version 1.0 描述：变量预置
 */
class DBPreparedStatementSetter implements PreparedStatementSetter {
	private Field m_oField = null;

	public DBPreparedStatementSetter(Field oField) {
		m_oField = oField;
	}

	public void setValues(PreparedStatement oPreparedStatement)
			throws SQLException {
		if (m_oField == null || m_oField.getFields().size() == 0)
			return;
		DBUtil.setValues(oPreparedStatement, m_oField);
	}
}

/**
 * 
 * 作者：伍锐凡 创建：2011-2-28 版本：Version 1.0 描述：批变量预置
 */
class DBBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
	private List<Field> m_aField = new ArrayList<Field>();

	public DBBatchPreparedStatementSetter(List<Field> aField) {
		m_aField.addAll(aField);
	}

	public int getBatchSize() {
		return m_aField.size();
	}

	public void setValues(PreparedStatement oPreparedStatement, int i)
			throws SQLException {
		DBUtil.setValues(oPreparedStatement, m_aField.get(i));
	}
}

/**
 * 
 * 作者：伍锐凡 创建：2011-2-28 版本：Version 1.0 描述：数据库辅助类
 */
class DBUtil {
	public static void setValues(PreparedStatement oPreparedStatement,
			Field oField) throws SQLException {
		List<FieldVo> aParam = oField.getFields();
		int nIndex = 0;
		for (FieldVo oFieldVo : aParam) {
			nIndex++;
			if (oFieldVo.m_oValue == null) {
				oPreparedStatement.setNull(nIndex, oFieldVo.m_nType);
				continue;
			}
			switch (oFieldVo.m_nType) {
			case Types.INTEGER:
				oPreparedStatement.setInt(nIndex,
						Integer.parseInt(oFieldVo.m_oValue.toString()));
				break;
			case Types.FLOAT:
				oPreparedStatement.setFloat(nIndex,
						Float.parseFloat(oFieldVo.m_oValue.toString()));
				break;
			case Types.DOUBLE:
				oPreparedStatement.setDouble(nIndex,
						Double.parseDouble(oFieldVo.m_oValue.toString()));
				break;
			case Types.TIMESTAMP:
				oPreparedStatement.setTimestamp(nIndex,
						Timestamp.valueOf(oFieldVo.m_oValue.toString()));
				break;
			case Types.DATE:
				oPreparedStatement.setDate(nIndex,
						java.sql.Date.valueOf(oFieldVo.m_oValue.toString()));
				break;
			case Types.TIME:
				oPreparedStatement.setTime(nIndex,
						java.sql.Time.valueOf(oFieldVo.m_oValue.toString()));
				break;
			case Types.CLOB:
				oPreparedStatement.setClob(nIndex,
						new javax.sql.rowset.serial.SerialClob(
								oFieldVo.m_oValue.toString().toCharArray()));
				break;
			default:
				oPreparedStatement.setString(nIndex,
						oFieldVo.m_oValue.toString());
				break;
			}
		}
	}
}

/**
 * 
 * 作者：伍锐凡 创建：2011-2-28 版本：Version 1.0 描述：查询结果转换
 */
class DBRowMapper implements RowMapper<DynaBean> {
	public DynaBean mapRow(ResultSet oResultSet, int nRow) throws SQLException {
		ResultSetMetaData oResultSetMetaData = oResultSet.getMetaData();
		DynaBean oDynaBean = new LazyDynaBean();
		Date oDate = null;

		for (int i = 1, nLen = oResultSetMetaData.getColumnCount(); i <= nLen; i++) {
			String sLabel = oResultSetMetaData.getColumnLabel(i);
			switch (oResultSetMetaData.getColumnType(i)) {
			case Types.INTEGER:
				oDynaBean.set(sLabel, oResultSet.getInt(i));
				break;
			case Types.FLOAT:
				oDynaBean.set(sLabel, oResultSet.getFloat(i));
				break;
			case Types.DOUBLE:
				oDynaBean.set(sLabel, oResultSet.getDouble(i));
				break;
			case Types.TIMESTAMP:
				oDate = oResultSet.getTimestamp(i);
				oDynaBean.set(
						sLabel,
						oDate != null ? Tools.getDate(oDate,
								"yyyy-MM-dd HH:mm:ss") : "");
				break;
			case Types.DATE:
				oDate = oResultSet.getDate(i);
				oDynaBean
						.set(sLabel,
								oDate != null ? Tools.getDate(oDate,
										"yyyy-MM-dd") : "");
				break;
			case Types.TIME:
				Time oTime = oResultSet.getTime(i);
				oDynaBean.set(sLabel,
						oTime != null ? Tools.getDate(oTime, "HH:mm:ss") : "");
				break;
			case Types.CLOB:
				Clob oClob = oResultSet.getClob(i);
				oDynaBean.set(
						sLabel,
						oClob == null ? "" : oClob.getSubString(1,
								(int) oClob.length()));
				break;
			default:
				oDynaBean.set(sLabel, Tools.toStr(oResultSet.getString(i), ""));
				break;
			}
		}

		return oDynaBean;
	}
}
