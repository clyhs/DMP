package org.dmp.module.admin.common.excel;

import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.hssf.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author John Liu(LiuJigang)
 */
public class ExportingExcel {

    private static final Logger logger = Logger.getLogger(ExportingExcel.class);
    private static HSSFWorkbook workbook;
    public static final int MAX_ROW_PER_SHEET = 65536;

    private String fileName;
    private String dateFormat = "yyyy-MM-dd";

    private HSSFCellStyle defaultStyle ;
    private int sheetMark = 0;

    private List<List> excelData;

    public ExportingExcel(){}

    public void setWorkbook(HSSFWorkbook workbook){
        this.workbook = workbook;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setExcelData(List<List> excelData) {
        this.excelData = excelData;
    }

    /**
     * Generates the excel sheet data by HSSFWorkbook object workbook
     * @param isFirst
     * @return
     */
    public void generateData(boolean isFirst){
        if(excelData == null || excelData.size() == 0){
            return ;
        }
        int totalSize = excelData.size();
        int maxSizePerSheet = isFirst ? (MAX_ROW_PER_SHEET ): MAX_ROW_PER_SHEET;
        int remainder = totalSize % maxSizePerSheet;
        int sheetNum = remainder == 0 ? totalSize / maxSizePerSheet: totalSize / maxSizePerSheet + 1;

        try{
            for(int k = 0 ; k < sheetNum ; k++){
                HSSFSheet sheet = workbook.createSheet();
                sheet.setColumnWidth(0, 3000);
                sheet.setVerticallyCenter(true);
                int sheetSize =  k * MAX_ROW_PER_SHEET;
                logger.info("sheetNumber: "+ sheetMark*MAX_ROW_PER_SHEET);

                String excelName = sheetMark == 0? fileName : (fileName+ "-"+ (sheetMark));
                workbook.setSheetName((sheetMark), excelName);      //need change it according to the sheet number.
                sheetMark++;
                int rows = (sheetNum - 1 == k ?  ( sheetSize + remainder):( sheetSize+ MAX_ROW_PER_SHEET));
                /*create the rows and cells */
//                rows = totalSize == maxSizePerSheet?  totalSize: rows;
                logger.info(""+sheetNum+", rows number: "+ rows);
                for(int i = sheetSize; i < rows ; i++){
                    int index = i % maxSizePerSheet;
                    HSSFRow row = sheet.createRow(index);
                    int columns = excelData.get(0).size();
//                    logger.info("row size: "+ i + ", column" + columns);
                    try{
                        generateCell(excelData, row, columns);
                    } catch (Exception e){
                        logger.error("generate cells with an error", e);
                    }
                    excelData.remove(0);
                }
            }
        } catch (Exception e){
            logger.error("generate excel with an error", e);
        }
    }

    /**
     */
    public void generateCell(List<List> data, HSSFRow row,
                             int columns){
        for(int j = 0; j < columns ; j++){
            HSSFCell cell = row.createCell(j);
            setCellValue(cell, data.get(0).get(j)) ;
        }
    }
    /**
     * Builds the excel cell with the specified cell value.
     * @param cell   cell object
     * @param value  specified value object
     */

    public void setCellValue(HSSFCell cell, Object value) {
        if (value == null) {
            cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
        }else if (value instanceof String) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString((String) value));
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(getDefaultStyle());
        } else if (value instanceof BigDecimal){
            cell.setCellValue(((BigDecimal)value).doubleValue());
        } else if(value instanceof BigInteger){
            cell.setCellValue(((BigInteger)value).doubleValue());
        }  else if(value instanceof Long){
            cell.setCellValue(((Long)value).longValue());
        } else{
            logger.info("can't set CellValue with " + value.getClass() + " class type");
        }
    }

    public HSSFCellStyle getDefaultStyle() {
        if(defaultStyle==null){
            defaultStyle = workbook.createCellStyle();
            defaultStyle.setWrapText(true);
            defaultStyle.setDataFormat(workbook.createDataFormat().getFormat(dateFormat));
        }
        return defaultStyle;
    }

    public void setDateFormat(String format){
        dateFormat = format ;
    }
}
