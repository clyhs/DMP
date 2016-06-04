package org.dmp.module.admin.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.common.dao.DynamicReportDao;
import org.dmp.module.admin.common.dao.ReportAttrDao;
import org.dmp.module.admin.common.dao.ReportDao;
import org.dmp.module.admin.common.dao.StatBaseDao;
import org.dmp.module.common.grid.DynamicGrid;
import org.dmp.module.common.grid.MetaData;
import org.dmp.module.common.grid.ReportFieldDto;
import org.dmp.module.common.grid.ReportHeaderDto;
import org.dmp.pojo.admin.reportmgr.Report;
import org.dmp.pojo.admin.reportmgr.ReportAttr;
import org.dmp.pojo.admin.reportmgr.ReportEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("StatBaseSevice")
@Scope("prototype")
public class StatBaseSevice {

	@Resource(name = "ReportDao")
	private ReportDao rd;

	@Resource(name = "ReportAttrDao")
	private ReportAttrDao rad;

	@Resource(name = "StatBaseDao")
	private StatBaseDao sbd;

	@Resource(name = "DynamicReportDao")
	private DynamicReportDao tbd;

	// 获取动态toobar的控件
	public List getToolbarData(String reportId, int treeId, int bgUserId) {
		List list = tbd.getToolbarData(reportId);
		List<ReportEvent> eventList = tbd.getEventData(reportId);
		// 判断是否有权限设置
		System.out.println(list.get(0));

		for (int i = 0; i < eventList.size(); i++) {
			list.add(eventList.get(i));
		}

		return list;
	}

	/**
	 * 通过指定的参数获取所有的数据
	 * 
	 * @param reportId
	 * @param paraMap
	 * @return
	 */
	public List<DynaBean> getDataList(String reportId,
			HashMap<String, Object> paraMap) {
		Report report = rd.getReport(reportId);
		Object value = paraMap.get("flag");
		if (value != null) {
			paraMap.remove("flag");
		}
		return sbd.getDataList(report.getSql(), paraMap);
	}

	/**
	 * 获取导出Excel的头部信息
	 * 
	 * @param reportId
	 * @return
	 */
	public List<Object> getReportAttr(String reportId) {
		List<ReportAttr> reportAttrList = rad.getReportAttr(reportId);
		List header = null;
		if (reportAttrList != null && reportAttrList.size() > 0) {
			int size = reportAttrList.size();
			header = new ArrayList(size);
			for (int i = 0; i < size; i++) {
				ReportAttr attribute = reportAttrList.get(i);
				Object name = attribute.getName();
				header.add(name);
			}
		}
		return header;
	}

	/**
	 * 通过指定的参数获取图表所需数据
	 * 
	 * @param reportId
	 * @param paraMap
	 * @return List<DynaBean>
	 */
	public List<DynaBean> getChartDataList(String reportId,
			HashMap<String, Object> paraMap) {
		Report report = rd.getReport(reportId);
		Object value = paraMap.get("flag");
		Object lineStore = paraMap.get("lineStore");
		Object chartStore = paraMap.get("chartStore");
		if (value != null) {
			paraMap.remove("flag");
		}
		if (lineStore != null) {
			return sbd
					.getDataList(report.getChartSql().split("@@")[0], paraMap);
		}
		if (chartStore != null) {
			return sbd
					.getDataList(report.getChartSql().split("@@")[1], paraMap);
		}
		return sbd.getDataList(report.getChartSql(), paraMap);
	}

	public List<DynaBean> getDataList(String reportId,
			HashMap<String, Object> paraMap, int start, int offset) {
		Report report = rd.getReport(reportId);
		return sbd.getDataList(report.getSql(), paraMap, start, offset);
	}

	public Long getDataTotal(String reportId, HashMap<String, Object> paraMap) {
		Report report = rd.getReport(reportId);
		List<DynaBean> list = sbd.getDataList(report.getTotalSql(), paraMap);
		if (null != list && list.size() > 0) {
			DynaBean db = list.get(0);
			return Long.parseLong(db.get("total").toString());
		}
		return 0L;
	}

	public DynamicGrid initNullGrid(String reportId) {
		DynamicGrid dynamicGrid = new DynamicGrid();
		MetaData metaData = new MetaData();

		List<ReportAttr> list = rad.getReportAttr(reportId);
		List<ReportFieldDto> listField = new ArrayList<ReportFieldDto>();
		List<ReportHeaderDto> listHeader = new ArrayList<ReportHeaderDto>();

		String dataindex = "";
		if (null != list && list.size() > 0) {

			for (int i = 0; i < list.size(); i++) {

				ReportAttr ra = list.get(i);
				dataindex = ra.getDataIndex().trim();
				// 构造Field
				ReportFieldDto rfd = new ReportFieldDto();
				rfd.setName(dataindex);
				rfd.setType(rfd.getType());
				listField.add(rfd);

				// 构造header
				ReportHeaderDto rhd = new ReportHeaderDto();
				rhd.setHeader(ra.getName().trim());
				rhd.setDataIndex(dataindex);
				rhd.setWidth(ra.getWidth());
				if (ra.getType().equals("right")) {
					rhd.setAlign("right");
				}
				if (ra.getType().equals("left")) {
					rhd.setAlign("left");
				}
				rhd.setSortable(Boolean.parseBoolean(ra.getSortable()));
				listHeader.add(rhd);
			}
		}
		metaData.setFields(listField);
		dynamicGrid.setMetaData(metaData);
		dynamicGrid.setColumns(listHeader);

		return dynamicGrid;
	}

	public DynamicGrid getDynamicGrid(String reportId,
			HashMap<String, Object> paraMap, int start, int offset) {
		DynamicGrid dGrid = initNullGrid(reportId);
		String flag = (String) paraMap.get("flag");
		if (flag != null) {
			paraMap.remove("flag");
			dGrid.setRecords(Tools.dynaBeanToMap(this.getDataList(reportId,
					paraMap, start, offset)));
			dGrid.setTotal(this.getDataTotal(reportId, paraMap));
		}
		return dGrid;
	}

	/**
	 * 通过指定的参数获取合计的数据
	 * 
	 * @param reportId
	 * @param paraMap
	 * @return
	 */
	public List<DynaBean> getTotalList(HashMap<String, Object> paraMap,
			String sql) {
		Object value = paraMap.get("flag");
		if (value != null) {
			paraMap.remove("flag");
		}
		List<DynaBean> list = sbd.getDataTotal(sql, paraMap);
		return list;
	}

	/**
	 * Gets the specified summer sql statement by the report id .
	 * 
	 * @param reportId
	 * @return
	 */
	public String getSumSQL(String reportId) {
		return rd.getReport(reportId).getSumSql();
	}

	/******************* 重写报表获取数据方法（区分全国） shu.y **************************/
	/**
	 * 重写获取DynamicGrid(区分全国)
	 */
	public DynamicGrid getDynamicGridForReport(String reportId,
			HashMap<String, Object> paraMap, int start, int offset) {
		DynamicGrid dGrid = initNullGrid(reportId);
		Object flag = paraMap.get("flag");
		if (flag != null) {
			paraMap.remove("flag");
			dGrid.setRecords(Tools.dynaBeanToMap(this.getDataListForReport(
					reportId, paraMap, start, offset)));
			dGrid.setTotal(this.getDataTotalForReport(reportId, paraMap));
		}
		return dGrid;
	}

	/**
	 * 重写获取数据(区分全国)
	 */
	public List<DynaBean> getDataListForReport(String reportId,
			HashMap<String, Object> paraMap, int start, int offset) {
		Report report = rd.getReport(reportId);
		String sql = "";
		if (paraMap.get("provinceId").toString().equals("3")) {
			paraMap.put("province", "%");
			sql = report.getSql().split("@@")[1];
		} else {
			sql = report.getSql().split("@@")[0];
		}

		return sbd.getDataList(sql, paraMap, start, offset);
	}

	/**
	 * 重写通过指定的参数获取所有的数据(区分全国)
	 * 
	 * @param reportId
	 * @param paraMap
	 * @return
	 */
	public List<DynaBean> getDataListForReport(String reportId,
			HashMap<String, Object> paraMap) {
		Report report = rd.getReport(reportId);
		Object value = paraMap.get("flag");
		if (value == null) {
			paraMap.remove("flag");
		}
		String sql = "";
		if (paraMap.get("provinceId").toString().equals("3")) {
			paraMap.put("province", "%");
			sql = report.getSql().split("@@")[1];
		} else {
			sql = report.getSql().split("@@")[0];
		}
		return sbd.getDataList(sql, paraMap);
	}

	/**
	 * 重写获取总数(区分全国)
	 */
	public Long getDataTotalForReport(String reportId,
			HashMap<String, Object> paraMap) {
		Report report = rd.getReport(reportId);
		String totalSql = "";
		if (paraMap.get("provinceId").toString().equals("3")) {
			paraMap.put("province", "%");
			totalSql = report.getTotalSql().split("@@")[1];
		} else {
			totalSql = report.getTotalSql().split("@@")[0];
		}

		List<DynaBean> list = sbd.getDataList(totalSql, paraMap);
		if (null != list && list.size() > 0) {
			DynaBean db = list.get(0);
			return Long.parseLong(db.get("TOTAL").toString());
		}
		return 0L;
	}

	/**
	 * 重写图表数据(区分全国)
	 */
	public List<DynaBean> getChartDataListForReport(String reportId,
			HashMap<String, Object> paraMap) {
		Report report = rd.getReport(reportId);
		Object value = paraMap.get("flag");
		Object lineStore = paraMap.get("lineStore");
		Object chartStore = paraMap.get("chartStore");
		if (value != null) {
			paraMap.remove("flag");
		}
		if (paraMap.get("provinceId").toString().equals("3")) {
			paraMap.put("province", "%");
		}
		if (lineStore != null) {
			return sbd
					.getDataList(report.getChartSql().split("@@")[0], paraMap);
		}
		if (chartStore != null) {
			return sbd
					.getDataList(report.getChartSql().split("@@")[1], paraMap);
		}
		return sbd.getDataList(report.getChartSql(), paraMap);
	}

	/**
	 * 通过指定的参数获取合计的数据
	 * 
	 * @param reportId
	 * @param paraMap
	 * @return
	 */
	public List<DynaBean> getTotalListForReport(
			HashMap<String, Object> paraMap, String sql) {
		Object value = paraMap.get("flag");
		if (value != null) {
			paraMap.remove("flag");
		}
		if (paraMap.get("provinceId").toString().equals("3")) {
			paraMap.put("province", "%");
		}
		List<DynaBean> list = sbd.getDataList(sql, paraMap);
		return list;
	}
}
