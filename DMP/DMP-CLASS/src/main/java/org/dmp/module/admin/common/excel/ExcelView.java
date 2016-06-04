package org.dmp.module.admin.common.excel;

import java.util.Map;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


/**
 * 导出EXCEL视图
 * 
 * @Author 伍锐凡
 * @Date 2012-3-30
 * @Version 1.0
 * @Remark
 */
public class ExcelView extends AbstractExportZip{

	@Override
	protected void buildExcelDocument(Map<String, Object> oModel,
                                        HSSFWorkbook oWorkbook,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
		ExportingExcel excel = null;
		for (Map.Entry<String, Object> oEntry : oModel.entrySet()) {
			if (oEntry.getValue() instanceof ExportingExcel){
                excel = (ExportingExcel)oEntry.getValue();
				break;
			}
		}
		   /* header("Cache-Control: must-revalidate, post-check=0, pre-check=0");
            header("Content-Type: application/octet-stream");
            header('Content-type: application/ms-excel');str.getBytes("iso8859-1"), "gb2312"
            header("Content-Disposition:attachment;filename=".$this->input->post('filename').".xls;"); */
		if(excel != null){
			response.setCharacterEncoding("UTF-8");
			String encodedFileName = new String(excel.getFileName().getBytes("GBK"), "ISO8859-1");
//			response.setHeader("Content-type","application/zip;charset=UTF-8");
//	        response.setHeader("Content-Disposition", "attachment;filename=\""+encodedFileName+".zip\"");
			response.setHeader("Content-type","application/msexcel;charset=UTF-8");
	        response.setHeader("Content-Disposition", "attachment;filename=\""+encodedFileName+".xls\"");
	        excel.setWorkbook(oWorkbook);
            excel.generateData(true);		
    		ServletOutputStream out = response.getOutputStream();
    		oWorkbook.write(out);
//    		ZipOutputStream zip = new ZipOutputStream(out);
//    		zip.setEncoding("GBK");
//    		ZipEntry entry = new ZipEntry(excel.getFileName()+".xls");
//    		zip.putNextEntry(entry);
//    		oWorkbook.write(zip);
    		out.flush();
//    		zip.close();
		}
    }
}
