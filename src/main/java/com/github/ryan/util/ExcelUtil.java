package com.github.ryan.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: ExcelUtil
 * @date May 07,2018
 */
public class ExcelUtil {

    // 读取指定excel文件的行与单元格内容
    public static void readXLSXFile() throws IOException {
        String fileName = "";

        InputStream excelFileToRead = new FileInputStream(fileName);

        XSSFWorkbook wb = new XSSFWorkbook(excelFileToRead);
        XSSFSheet sheet = wb.getSheetAt(0);

        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            XSSFRow row = (XSSFRow) rows.next();
            // 过滤首行
            if (row.getRowNum() == 0) {
                continue;
            }

            StringBuilder sql = new StringBuilder();
            sql.append("update t_vehicle_inspect_bill set next_inspection_date = '");

            String vin = row.getCell(2).getStringCellValue();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateCellValue = row.getCell(4).getDateCellValue();
            String dateValue = df.format(dateCellValue);
            sql.append(dateValue)
                    .append("' where vehicle_id = ( select id from t_vehicle_base where vin = '")
                    .append(vin).append("' );");

            System.out.println(sql.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        readXLSXFile();
    }
}
