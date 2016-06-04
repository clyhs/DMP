package org.dmp.module.admin.reportmgr.service;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dmp.core.util.Tools;
import org.dmp.module.admin.reportmgr.dao.ReportImportDao;
import org.dmp.module.common.form.FormResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service("ReportImportService")
@Scope("prototype")
public class ReportImportService {
	@Resource(name = "ReportImportDao")
	private ReportImportDao rid;
	
	/**
	 * 导入excel表预览的内容到数据库
	 * @param tableName
	 * @param list
	 * @return
	 */
	public FormResponse doImportExcel(String tableName,List<Map<String,Object>> list){ 
		int mapSize = 0;
		FormResponse res = new FormResponse();
		if(list!=null&&list.size()>0){
			mapSize = list.get(0).size();
		}else{
			 res.setMsg("请先预览核对数据");
	    	 res.setSuccess(false);
	    	 return res;
		}
		int successCount = 0;
		List sqlList = new ArrayList();
		String sql = " SELECT COLUMN_NAME,DATA_TYPE,NULLABLE FROM USER_TAB_COLUMNS t WHERE t.TABLE_NAME = '" + tableName + "' ORDER BY COLUMN_ID";
		String sqlPK = " select col.column_name from user_constraints con, user_cons_columns col "
			   +" where con.constraint_name = col.constraint_name "
			   +" and con.constraint_type='P' and col.table_name = '"+tableName+"'";
		    List<DynaBean> tablePkList = rid.getTablePK(sqlPK);
		    List<DynaBean> tbPropertyList = rid.getTableProperty(sql);
			List colNameList = new ArrayList(); //列名
			List colTypeList = new ArrayList();  //字段类型
			List colAllowNullList = new ArrayList(); //字段是否为空
			List pkList = new ArrayList();
			Tools.pkDynaBeanToList(pkList,tablePkList);
			Tools.tbDynaBeanToList(colNameList,colTypeList,colAllowNullList,tbPropertyList,pkList);
			//处理多个主建和没有主键的问题
			 if(pkList.size()==1){
				 if(mapSize!=colNameList.size()){
					 res.setMsg("EXCEl表列数不符合要求");
			    	 res.setSuccess(false);
			    	 return res;
				} 
			 }else{
				 if(mapSize-1!=colNameList.size()){
					 res.setMsg("EXCEl表列数不符合要求");
			    	 res.setSuccess(false);
			    	 return res;
				}  
			 }
			String columnName = "";
			//拼接SQL语句中要插入的字段
			for(int i = 0;i<colNameList.size();i++){
				columnName += (String)colNameList.get(i)+",";
			}
			columnName = columnName.substring(0, columnName.length()-1);
			//遍历该行所有的列
			int rowSize = list.size();
		    String cellValue = ""; // EXCEL单元格的值
            String allowNull = "";
		    String colType = "";
		    int cellSize = colNameList.size();
			for(int i = 0;i<rowSize;i++){
				StringBuffer sbSQL = new StringBuffer();
				             sbSQL.append(" INSERT INTO ")
					              .append(tableName)
					              .append(" (")
					              .append(columnName)
					              .append(" ) VALUES (");
			    //遍历该行所有的列
			    for (int j = 0; j < cellSize; j++) {
					 allowNull = colAllowNullList.get(j).toString(); // 列是否允许为空
					 colType = colTypeList.get(j).toString();       // 列类型
					 //处理多个主建和没有主键的问题
					 if(pkList.size()==1){
					    cellValue = list.get(i).get("F" + (j)).toString();
					 }else{
						 cellValue = list.get(i).get("F" + (j+1)).toString(); 
					 }
				     if (ReportImportService.check(allowNull, colType, cellValue)){
                         String msg = "";
                         //处理多个主建和没有主键的问题
				    	 if(pkList.size()==1){
				    		 msg =  "数据导入失败,"+ReportImportService.prompt(colAllowNullList,colTypeList,1); 
				    	 }else{
				    		  msg = "数据导入失败,"+ReportImportService.prompt(colAllowNullList,colTypeList,2); 
				    	 }
				    	 res.setMsg(msg);
				    	 res.setSuccess(false);
				    	 return res;
				     }
				     cellValue = ReportImportService.getCellValue(cellValue, colType);
				     sbSQL.append(cellValue);
			    }
			    sqlList.add(sbSQL.toString().substring(0,sbSQL.length() - 1)+ ")");
			    sbSQL = null;
		    }
			for(int t = 0; t < sqlList.size(); t++){ 
				int isInsert = rid.doInsert(sqlList.get(t).toString());
				if(isInsert==1){
				  successCount++;
				}
			}	
			if(successCount>0){
			   res.setMsg("成功导入了"+successCount+"条数据");
		       res.setSuccess(true);
			}else{
			   res.setMsg("导入数据失败!");
			   res.setSuccess(false);	
			}
			return res;
	}
	
	//哪列不能为空的提示
	public static String prompt(List colAllowNullList,List colTypeList,int type){
		String str = "第";
		String notNull = "";
		String typeNull = "";
		int allowSize = colAllowNullList.size();
		if(type == 1){
			for(int c=1;c<allowSize;c++){
				notNull = colAllowNullList.get(c).toString();
				typeNull = colTypeList.get(c).toString();
				if(notNull.equals("N")||typeNull.equals("NUMBER")||typeNull.equals("DATE")||typeNull.equals("TIMESTAMP(6)")){
					str+=(c)+"、";
				}
			}	
		}else{
			for(int c=0;c<allowSize;c++){
				notNull = colAllowNullList.get(c).toString();
				typeNull = colTypeList.get(c).toString();
				if(notNull.equals("N")||typeNull.equals("NUMBER")||typeNull.equals("DATE")||typeNull.equals("TIMESTAMP(6)")){
					str+=(c+1)+"、";
				}
			}	
		}
		return str.substring(0,str.length()-1)+"列数据不允许为空";
	}
	
	
	public static Boolean check(String allowNull,String colType,String cellValue){
		//判断EXCEL表为空但数据库不允许为空的
		if(allowNull.equals("N")&&(cellValue==null || cellValue.equals(""))){
			return true;
		}
		//数字类型不允许为空的
		if(colType.equals("NUMBER")&&(cellValue==null || cellValue.equals(""))){
			return true;
		}
		//日期类型不允许为空的
		if(colType.equals("DATE")&&(cellValue==null || cellValue.equals(""))){
			return true;
		}
			
		//日期类型不允许为空的
		if(colType.equals("TIMESTAMP(6)")&&(cellValue==null || cellValue.equals(""))){
			return true;
		}
		return false;
	}
	
	/**
	 * 删除excel表预览的内容
	 * @param list
	 * @param ids
	 * @return
	 */
	public List<Map<String,Object>> delExcel(List<Map<String,Object>> list,String ids){
		String[] id = ids.split(",");
		String mapVlaue = "";
		for (int j = id.length; j > 0; j--) {
			String sId = id[j-1];
			int rowSize = list.size();
			for (int i = rowSize; i > 0; i--) {
				mapVlaue = list.get(i-1).get("F0").toString();
				if (mapVlaue.equals(sId)) {
					list.remove(i-1);
					break;
				}
			}
		}
		return list;
	}
	
	/**
	 * 修改excel表预览的内容
	 * @param list
	 * @param id
	 * @param field
	 * @param changeValue
	 * @return
	 */
	public List<Map<String,Object>> editExcel(List<Map<String,Object>> list,String id,String field,String changeValue){
	    String mapVlaue = "";
	    int rowSize = list.size();
	    for(int i=0;i<rowSize;i++){
	    	mapVlaue = list.get(i).get("F0").toString();
	    	if(mapVlaue.equals(id)){
	    		list.get(i).remove(field);
	    		list.get(i).put(field,changeValue);
	    		break;
	    	}
	    }
		return list;
	}
	
	/**
	 * excel表的内容转化成List
	 * @param file
	 * @return
	 */
	public List<Map<String,Object>> excelToMap(MultipartFile file){
		HSSFRow row = null; //表单行数
		HSSFSheet sheet = null;  //表单对像
		InputStream  inputStream = null;
		HSSFWorkbook  workBook = null;
		List<Map<String, Object>> rowList = new LinkedList<Map<String, Object>>();
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			inputStream = file.getInputStream();
			workBook = new HSSFWorkbook(inputStream);
			sheet = workBook.getSheetAt(0);
			int rowSize = sheet.getPhysicalNumberOfRows();
			//根据EXCEL表表头来确定列数，因为每列都包含很多null没用的单元格
			int cellSize = sheet.getRow(0).getPhysicalNumberOfCells();
			for(int j=0;j<cellSize;j++){
				HSSFCell cell  = sheet.getRow(0).getCell(j);
				if(ReportImportService.getCellValue(cell).equals("")){
					cellSize--;
				}
			}
			//把EXCEL单元格的值放到List
			for(int i = 1;i<rowSize;i++){
				String nowTime = sf.format(new Date());
				row = sheet.getRow(i);
				if(row==null){
					continue;
				}
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("F0",nowTime+i);
				int cellNullSize = 0;
				for(int j=0;j<cellSize;j++){
					String cellValue = ReportImportService.getCellValue(row.getCell(j)); //EXCEL单元格的值	
					if(cellValue.equals("")){
						cellNullSize++;
					}
					map.put("F"+(j+1), cellValue);
				}
				//处理在excel表中删除一行但读取的时候还存在全部为空格行的情况.
				if(cellNullSize==cellSize){
					continue;
				}
				rowList.add(map);
			}
		} catch (IOException e) {}
		return rowList;
	}
	
	/**
	 * 表名称下拉框
	 * @return
	 */
	public List<DynaBean> getTableNameBean(){
		String sSQL = " select table_name as tableName from user_tables where table_name LIKE 'TR%' ORDER BY table_name ";
		return rid.getTableNameBean(sSQL);
	}
	
	/**
	 * 根据excel表的第一列获取动态表头
	 * @param file
	 * @return
	 */
	public String getTableHeader(MultipartFile file){
		HSSFSheet sheet = null;  //表单对像
		InputStream  inputStream = null;
		HSSFWorkbook  workBook = null;
		String sJson = "";
		try {
			inputStream = file.getInputStream();
			workBook = new HSSFWorkbook(inputStream);
			sheet = workBook.getSheetAt(0);
			int cellSize = sheet.getRow(0).getPhysicalNumberOfCells();
			StringBuffer sbColumn = new StringBuffer();
			sbColumn.append("[");
			for(int j=0;j<cellSize;j++){
				HSSFCell cell  = sheet.getRow(0).getCell(j);
				String cellvalue = ReportImportService.getCellValue(cell);
				if(!cellvalue.equals("")){
					sbColumn.append("{header:")
					.append("'")
			        .append(cellvalue)
			        .append("'")
			        .append(",dataIndex:")
			        .append("'")
			        .append("F"+(j+1))
			        .append("'")
			        .append(",editor:")
			        .append("new Ext.form.TextField()")
			        .append(",width:110")
			        .append("},");
				}
			}
			sJson = sbColumn.toString().substring(0,sbColumn.toString().length()-1) +"]";
		} catch (IOException e) {}
		return sJson;
	}
	
	/**
	 * excel表模版下载
	 * @param request
	 * @param response
	 * @param fileName
	 * @return
	 */
	public String getExcelModel(HttpServletRequest request,HttpServletResponse response,String fileName){
		OutputStream os =null;
		BufferedOutputStream bos =null;
		BufferedInputStream bis = null;
		InputStream is  = null;
		String msg = "";
		try {
			String filePath = request.getSession().getServletContext().getRealPath("/");
			filePath=filePath+"rw"+System.getProperty("file.separator")+"excelmodel"+System.getProperty("file.separator")+fileName;
			File file = new File(filePath);
			if(!file.exists()){
				msg = "excel模版不存在!";
				return msg;
			}
			is = new FileInputStream(filePath);
			bis = new BufferedInputStream(is);
			os = response.getOutputStream();
			bos = new BufferedOutputStream(os);
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","attachment;filename="+fileName);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while((bytesRead = bis.read(buffer,0,8192))!=-1){
				bos.write(buffer,0,bytesRead);
			}
			
		} catch (IOException e) {
			
		}finally{
			try {
				if(bos != null){
				  bos.flush();	
				}
				if(bis != null){
				   bis.close();
				}
				if(os != null){
				  os.close(); 
				}
				if(bos != null){
				  bos.close();
				}
			} catch (IOException e) {}
		}
		return msg;
	}
	
	/**
	 * 判断从Excel文件中解析出来数据的格式  然后转成字符串
	 * @param cell
	 * @return
	 */
	private static String getCellValue(HSSFCell cell){
		String cellValue = null;
		//当EXCEL表的单元格为null时返回字符串,不然会报错
		if(cell==null){
		  return "";	
		}
		switch(cell.getCellType()){
		 case HSSFCell.CELL_TYPE_STRING://字符串 
			  cellValue = cell.getRichStringCellValue().getString().trim();
			  break;
		 case HSSFCell.CELL_TYPE_NUMERIC://数字
			  cellValue = (long)cell.getNumericCellValue()+"";
			  break;
		 case HSSFCell.CELL_TYPE_BLANK:
			  cellValue = "";
			  break;
		 case HSSFCell.CELL_TYPE_FORMULA:
			  cellValue = String.valueOf(cell.getCellFormula());
			  break;
		 case HSSFCell.CELL_TYPE_BOOLEAN:
			  cellValue = String.valueOf(cell.getBooleanCellValue());
			  break;
		 case HSSFCell.CELL_TYPE_ERROR:
			  cellValue = String.valueOf(cell.getErrorCellValue());
			  break;
	     default:
			  break;
		} 
		return cellValue;
	}
	
	
	/**
	 * 获取单元格的值,如果是数值类型为空的时候就返回0，字符串的就加上单引号
	 * @param cellValue
	 * @param type
	 * @return
	 */
	private static String getCellValue(String cellValue,String type){
	   String value = null;
	   if(type.equals("NUMBER")){
			value = cellValue;
	   }else if(type.equals("VARCHAR2")){
		  value = "'"+cellValue+"'";
	   }else if(type.equals("DATE")){
		  value = "TO_DATE('"+cellValue+"','YYYY-MM-DD HH24:MI:SS')";
	   }else if(type.equals("CHAR")){
		  value = "'"+cellValue+"'";
	   }else if(type.equals("TIMESTAMP(6)")){
		   value = "TO_DATE('"+cellValue+"','YYYY-MM-DD HH24:MI:SS')";
	   }else{
		   value = " ";
	   }
		return value+",";
	}
}
