package org.dmp.module.admin.common.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.common.dao.ReportAttrDao;
import org.dmp.module.admin.common.dao.ReportBaseDao;
import org.dmp.module.admin.common.dao.ReportDao;
import org.dmp.module.common.grid.DynamicGrid;
import org.dmp.module.common.grid.MetaData;
import org.dmp.module.common.grid.ReportFieldDto;
import org.dmp.module.common.grid.ReportHeaderDto;
import org.dmp.pojo.admin.reportmgr.Report;
import org.dmp.pojo.admin.reportmgr.ReportAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;



@Service("ReportBaseService")
@Scope("prototype")
public class ReportBaseService {

	@Resource(name="ReportDao")
	private ReportDao rd;
	
	@Resource(name="ReportAttrDao")
	private ReportAttrDao rad;
	
	@Resource(name="ReportBaseDao")
	private ReportBaseDao rbd;

    /**
     * 通过指定的参数获取所有的数据
     * @param reportId
     * @param paraMap
     * @return
     */
	public List<DynaBean>  getDataList(String reportId,LinkedHashMap<String, Object> paraMap){
		Report report = rd.getReport(reportId);
        Object value =  paraMap.get("flag");
        if(value == null || value.toString().length() == 0){
            paraMap.remove("flag");
        }
		return rbd.getDataList(report.getSql(), paraMap);
	}
	

    /**
     * 获取导出Excel的头部信息
     * @param reportId
     * @return
     */
    public List<Object>  getReportAttr(String reportId){
        List<ReportAttr> reportAttrList = rad.getReportAttr(reportId);
        List header = null;
        if(reportAttrList != null && reportAttrList.size() > 0){
            int size = reportAttrList.size();
            header = new ArrayList(size);
            for(int i = 0; i < size; i++){
                ReportAttr attribute = reportAttrList.get(i);
                Object name = attribute.getName();
                header.add(name);
            }
        }
        return header;
    }
    
    /**
     * 通过指定的参数获取图表所需数据
     * @param reportId
     * @param paraMap
     * @return List<DynaBean>
     */
	public List<DynaBean>  getChartDataList(String reportId,LinkedHashMap<String, Object> paraMap){
		Report report = rd.getReport(reportId);
		Object value =  paraMap.get("flag");
        if(value == null || value.toString().length() == 0){
            paraMap.remove("flag");
        }
		return rbd.getDataList(report.getChartSql(), paraMap);
	}
	
	public List<DynaBean>  getDataList(String reportId,LinkedHashMap<String, Object> paraMap,int start, int offset){
		Report report = rd.getReport(reportId);
		return rbd.getDataList(report.getSql(), paraMap,start,offset);
	}
	
	public Long getDataTotal(String reportId,LinkedHashMap<String, Object> paraMap){
		Report report = rd.getReport(reportId);
		List<DynaBean> list = rbd.getDataList(report.getTotalSql(), paraMap);
		if(null!=list && list.size()>0){
			DynaBean db = list.get(0);
			return Long.parseLong(db.get("TOTAL").toString());
		}
		return 0L;
	}
	
	public DynamicGrid initNullGrid(String reportId){
		DynamicGrid dynamicGrid  = new DynamicGrid();
		MetaData metaData = new MetaData();
		
		List<ReportAttr> list = rad.getReportAttr(reportId);
		List<ReportFieldDto> listField = new ArrayList<ReportFieldDto>();
		List<ReportHeaderDto> listHeader = new ArrayList<ReportHeaderDto>();
		
		String dataindex = "";
		if(null!=list && list.size()>0){
			
			for(int i=0;i<list.size();i++){
				
				ReportAttr ra = list.get(i);
				dataindex = ra.getDataIndex().trim();
				//构造Field
				ReportFieldDto rfd = new ReportFieldDto();
				rfd.setName(dataindex);
				rfd.setType(rfd.getType());
				listField.add(rfd);
				
				//构造header
				ReportHeaderDto rhd = new ReportHeaderDto();
				rhd.setHeader(ra.getName().trim());
				rhd.setDataIndex(dataindex);
				rhd.setWidth(ra.getWidth());
				if(ra.getType().equals("right")){
					rhd.setAlign("right");
				}
				if(ra.getType().equals("left")){
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
	
	public DynamicGrid getDynamicGrid(String reportId,LinkedHashMap<String, Object> paraMap,int start, int offset){
		DynamicGrid dGrid =  initNullGrid(reportId);
		String flag = paraMap.get("flag").toString();
		if(!"".equals(flag)){
			paraMap.remove("flag");
			dGrid.setRecords(Tools.dynaBeanToMap(this.getDataList(reportId, paraMap, start, offset)));
			dGrid.setTotal(this.getDataTotal(reportId, paraMap));
		}
		return dGrid;
	}
	
    /**
     * 通过指定的参数获取合计的数据
     * @param reportId
     * @param paraMap
     * @return
     */
	public List<DynaBean> getTotalList(String reportId,LinkedHashMap<String, Object> paraMap,String sql){
		Report report = rd.getReport(reportId);
        Object value =  paraMap.get("flag");
        if(value != null || value.toString().length() != 0){
            paraMap.remove("flag");
        }
        List<DynaBean> list = rbd.getDataList(sql, paraMap);
        return list;		
	}

    /**
     * Gets the specified summer sql statement by the report id .
     * @param reportId
     * @return
     */
    public String getSummerSQL(String reportId){
           return  rd.getReport(reportId).getSumSql();
    }
}
