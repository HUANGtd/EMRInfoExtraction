import basic.EMRInfoExtractionTask;
import io.DataOutputExcel;
import io.DictInputExcel;
import io.util.FileUlitity;
import io.util.FileWriter;
import io.util.LogWriter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by huang.tudou
 */
public class EMRInfoExtraction {
    public static void main(String args[]) {
        // delete exsisting files
        FileUlitity.DeleteFolder("data" + File.separator + "output");

        LogWriter log = new LogWriter("data" + File.separator + "log.md");
        DictInputExcel dictInputExcel = new DictInputExcel("data" + File.separator + "input" + File.separator + "2型糖尿病.xlsx");
        dictInputExcel.parseXLSXFile2txt();
        log.WriteLog("## 2型糖尿病病例分析日志\n");
        log.WriteLog("- 文件`" + dictInputExcel.getInputFileName() + "`读取完毕。\n");

        File inputFolder = new File("data"+ File.separator + "input"+ File.separator + "emr");
        if(!inputFolder.exists())
            inputFolder.mkdirs();
        File[] patientFolders = inputFolder.listFiles();
        for(File patientFolder : patientFolders) {
            if(patientFolder.isDirectory()) {
                int index = patientFolder.getPath().lastIndexOf(File.separator);
                if(index == -1) {
                    continue;
                }

                String folderName = patientFolder.getPath().substring(index + 1);
                EMRInfoExtractionTask task;
                ArrayList<String> taskName = dictInputExcel.getTaskName();
                DataOutputExcel out2xlsx = new DataOutputExcel(folderName, "data" + File.separator + "output"+ File.separator + folderName + File.separator  + folderName + ".xlsx");
                log.WriteLog("- 开始解析文件夹`" + folderName + "`。\n");

                File[] patientFiles = patientFolder.listFiles();
                for(File patientFile : patientFiles) {
                    int sIndex = patientFile.getPath().lastIndexOf(File.separator);
                    int eIndex = patientFile.getPath().lastIndexOf(".");
                    if(sIndex == -1 || eIndex == -1) {
                        continue;
                    }
                    String originName = patientFile.getPath().substring(sIndex + 1, eIndex);
                    for(String name : taskName) {
                        if(originName.contains(name)) {
                            task = new EMRInfoExtractionTask(name, folderName, originName);
                            task.genEMRTree();
                            task.extractionInfo();

                            out2xlsx.addTree(originName, task.getEmrTree());
                            out2xlsx.OutPut2xlsx();

                            task.output2txt();
                            task.output2md();
                            log.WriteLog("    - 文件`" + folderName + "`/`" + patientFile.getPath().substring(sIndex + 1) + "`解析完毕。\n");
                        }
                    }
                }
                log.WriteLog("  - 文件夹`" + folderName + "`解析完毕。\n");
            }
        }

        log.WriteLog("- 所有文件解析完毕，删除临时文件...\n");
        FileUlitity.DeleteFolder("data"+ File.separator + "intermediate");
        log.WriteLog("- 删除临时文件完毕，解析结束。");
    }
}
