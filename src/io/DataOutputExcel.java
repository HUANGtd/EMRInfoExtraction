package io;

import java.io.*;
import java.util.*;

import basic.segment.EMRSegment;
import basic.tree.EMRLeafNode;
import basic.tree.EMRNode;
import basic.tree.EMRTree;
import org.apache.poi.xssf.usermodel.*;
import util.FileUlitity;

/**
 * Created by huang.tudou on 5/18/16.
 */
public class DataOutputExcel {
    String fileName = null;
    String filePath = null;
    ArrayList<String> sheetName = null;
    HashMap<String, EMRTree> forest = null;

    public DataOutputExcel(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void OutPut2xlsx() {
        if(this.sheetName == null) {
            return;
        }
        try {
            XSSFWorkbook wb = new XSSFWorkbook();

            for(String sheet : this.sheetName) {
                this.WriteTree2Xlsx(wb, sheet, this.forest.get(sheet));
            }

            FileUlitity.makeFileDirectory(this.filePath);

            FileOutputStream ExcelFileToExport = new FileOutputStream(this.filePath);
            wb.write(ExcelFileToExport);
            ExcelFileToExport.flush();
            ExcelFileToExport.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteTree2Xlsx(XSSFWorkbook wb, String sheetName, EMRTree tree) {
        XSSFSheet sheet = wb.createSheet(sheetName);
        int rowNum = 0;

        rowNum = this.WriteNode2Xlsx(tree.getRoot(), rowNum, sheet);
    }

    private int WriteNode2Xlsx(EMRNode node, int rowNum, XSSFSheet sheet) {
        if(node.getIsLeaf() == true) {
            return WriteLeaf2Xlsx((EMRLeafNode)node, rowNum, sheet);
        }

        if(node.getLevel() == 0) {
            for(EMRNode child : node.getChildren()) {
                rowNum = WriteNode2Xlsx(child, rowNum, sheet);
            }
            return rowNum;
        }

        // if that area dose not exists in the data
        if(node.getContent() == null) {
            return rowNum;
        }

        if(node.getLevel() == 1) {
            XSSFRow row = sheet.createRow(rowNum++);
            XSSFCell name = row.createCell(0);

            name.setCellValue(node.getName());
            row = sheet.createRow(rowNum++);
            XSSFCell content = row.createCell(1);
            content.setCellValue("“" + node.getContent() + "”");
        } else if(node.getLevel() == 2) {
            XSSFRow row = sheet.createRow(rowNum++);
            XSSFCell name = row.createCell(0);

            name.setCellValue(node.getName());
        }

        for(EMRNode child : node.getChildren()) {
            rowNum = WriteNode2Xlsx(child, rowNum, sheet);
        }

        return ++rowNum;
    }

    private int WriteLeaf2Xlsx(EMRLeafNode node, int rowNum, XSSFSheet sheet) {
        if(node.getExist() == false) {
            return rowNum;
        }

        XSSFRow row = sheet.createRow(rowNum++);

        XSSFCell type = row.createCell(0);
        XSSFCell name = row.createCell(1);


        type.setCellValue(node.getType());
        name.setCellValue(node.getName());

        ArrayList<EMRSegment> segments = node.getSegments();

        for(EMRSegment s : segments) {
            row = sheet.createRow(rowNum++);
            int columnNum = 1;

            // 是/否
            if(s.getYesOrNo() != null) {
                XSSFCell yesOrNo = row.createCell(columnNum++);
                if(s.getYesOrNo() == true) {
                    yesOrNo.setCellValue("是");
                } else {
                    yesOrNo.setCellValue("否");
                }
            }

            // 如果需要考虑+原文中存在患病时长
            if(node.getIsDurationNeeded() == true) {
                if(s.getDuration() != null) {
                    XSSFCell duration = row.createCell(columnNum++);
                    duration.setCellValue(s.getDuration());
                }
            }

            // 如果需要考虑+原文中存在亲属患病
            if(node.getIsRelativesNeeded() == true) {
                if(s.getRelatives() != null) {
                    for (String key : s.getRelatives().keySet()) {
                        XSSFCell relative = row.createCell(columnNum++);
                        relative.setCellValue("[" + key + "]" + (s.getRelatives().get(key) == "N/A" ? "" : "[" + s.getRelatives().get(key) + "][岁]"));
                    }
                }
            }

            // 如果需要考虑+原文中存在其他关键词
            if(s.getOtherKeywords() != null) {
                for(String keyword : s.getOtherKeywords()) {
                    XSSFCell otherKey = row.createCell(columnNum++);
                    otherKey.setCellValue(keyword);
                }
            }

            // 如果提出到了值
            if(s.getValue() != null) {
                XSSFCell value = row.createCell(columnNum++);
                if(s.getValue().equals("N/A")) {
                    value.setCellValue("[" + s.getValue() + "]");
                } else {
                    XSSFCell unit = row.createCell(columnNum++);

                    value.setCellValue(s.getValue());
                    unit.setCellValue(s.getUnit());
                }
            }

            XSSFCell context = row.createCell(columnNum++);
            context.setCellValue("“" + s.getContext() + "”");
        }

        return rowNum;
    }

    /********* util *********/
    public void addTree(String sheetName, EMRTree tree) {
        if(this.sheetName == null) {
            this.sheetName = new ArrayList<String>();
            this.forest = new HashMap<String, EMRTree>();
        }

        this.sheetName.add(sheetName);
        this.forest.put(sheetName, tree);
    }
}
