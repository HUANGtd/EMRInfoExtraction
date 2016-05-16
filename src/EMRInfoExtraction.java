import basic.EMRInfoExtractionTask;
import io.DictInputExcel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by huang.tudou
 */
public class EMRInfoExtraction {
    public static void main(String args[]) {
        DictInputExcel dictInputExcel = new DictInputExcel("data/input/2型糖尿病.xlsx");
        dictInputExcel.parseXLSXFile();

        File inputFolder = new File("data/input/emr");
        if(!inputFolder.exists())
            inputFolder.mkdirs();
        File[] patientFolders = inputFolder.listFiles();
        for(File patientFolder : patientFolders) {
            if(patientFolder.isDirectory()) {
                int index = patientFolder.getPath().lastIndexOf("/");
                if(index == -1) {
                    continue;
                }
                String folderName = patientFolder.getPath().substring(index + 1);
                EMRInfoExtractionTask task;
                ArrayList<String> taskName = dictInputExcel.getTaskName();
                File[] patientFiles = patientFolder.listFiles();
                for(File patientFile : patientFiles) {
                    int sIndex = patientFile.getPath().lastIndexOf("/");
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
                            task.output2txt();
                            task.output2md();
                        }
                    }
                }
            }
        }
    }
}
