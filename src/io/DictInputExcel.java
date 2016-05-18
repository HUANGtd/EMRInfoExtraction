package io;

import java.io.*;
import java.util.*;

import org.apache.poi.xssf.usermodel.*;
import util.FileUlitity;

/**
 * Created by huang.tudou on 5/9/16.
 */
public class DictInputExcel {
    private String inputFileName = null;
    private ArrayList<String> taskName = null;

    public DictInputExcel(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void parseXLSXFile2txt() {
        try {
            InputStream ExcelFileToRead = new FileInputStream(this.inputFileName);
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

            for(int i = 0; i < wb.getNumberOfSheets(); i++) {
                // input as .xlsx
                XSSFSheet sheet = wb.getSheetAt(i);
                XSSFRow row;
                XSSFCell cell;
                Iterator rows = sheet.rowIterator();

                String sheetName = sheet.getSheetName();

                // output to as .txt
                String outputfilePath = "data" + File.separator + "intermediate"+ File.separator + "dictionary"+ File.separator + sheetName + ".txt";
                File file = new File(outputfilePath);
                FileUlitity.makeFileDirectory(outputfilePath);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

                while(rows.hasNext()) {
                    row = (XSSFRow)rows.next();
                    Iterator cells = row.cellIterator();
                    while(cells.hasNext()) {
                        cell = (XSSFCell)cells.next();

                        if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                            String strCell = cell.getStringCellValue();
                            strCell = strCell.replaceAll(" ", "");
                            if(!strCell.equals("")) {
                                writer.write(strCell + " ");
                            }
                        }
                    }
                    writer.write("\n");
                }

                this.addTaskName(sheetName);
                writer.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /******** util ********/
    public void addTaskName(String name) {
        if(this.taskName == null) {
            this.taskName = new ArrayList<String>();
        }

        this.taskName.add(name);
    }

    public ArrayList<String> getTaskName() {
        return this.taskName;
    }
}
