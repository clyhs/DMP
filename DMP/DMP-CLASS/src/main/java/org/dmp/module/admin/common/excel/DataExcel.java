package org.dmp.module.admin.common.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DataExcel
{
	private HSSFWorkbook m_oWorkbook = null;
	
	private String m_sFileName = "";
	private Map<String, List<DynaBean>> aSheet = new HashMap<String, List<DynaBean>>();
	
	public void setFileName(String sFileName)
	{
		m_sFileName = sFileName;
	}
	
	public String getFileName()
	{
		return m_sFileName;
	}
	
	public void setWorkbook(HSSFWorkbook oWorkbook)
	{
		m_oWorkbook = oWorkbook;
	}
	
	public HSSFWorkbook getWorkbook()
	{
		return m_oWorkbook;
	}
	
	public void addSheet(String sSheetName, List<DynaBean> aData)
	{
		aSheet.put(sSheetName, aData);
	}
	
	public void delSheet(String sSheetName)
	{
		aSheet.remove(sSheetName);
	}
	
	public void delAllSheet()
	{
		aSheet.clear();
	}
	
	public HSSFWorkbook toExcel()
	{
		if(m_oWorkbook == null) return m_oWorkbook;
		
		for(String sKey : aSheet.keySet()) 
		{
			HSSFSheet oSheet = m_oWorkbook.createSheet(sKey);
			HSSFRow oRow = null;
			//HSSFCellStyle oStyle;
			// oStyle.setFillBackgroundColor(HSSFColor.AQUA.index);
			// oStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);
			HSSFCell oCell;
			// oCell.setCellValue("X");
			// oCell.setCellStyle(oStyle);
			// Orange "foreground",
			// foreground being the fill foreground not the font color.
			// oStyle = oWorkbook.createCellStyle();
			// oStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
			// oStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			int nRow = 0;

			List<DynaBean> aData = aSheet.get(sKey);
			if(aData != null && aData.size() > 0)
			{
				for (DynaBean oData : aData)
				{
					oRow = oSheet.createRow(nRow++);
					DynaProperty[] aProperty = oData.getDynaClass().getDynaProperties();
					if(aProperty != null && aProperty.length > 0)
					{
						for(int nColumn = 0, nLen = aProperty.length; nColumn < nLen; nColumn ++)
						{
							oCell = oRow.createCell(nColumn);
							oCell.setCellValue(oData.get(aProperty[nColumn].getName()).toString());
						}
					}
				}
			}
		}
		
		return m_oWorkbook;
	}

}
