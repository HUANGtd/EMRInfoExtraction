package basic;

import basic.tree.EMRTree;
import io.*;

import java.io.File;

/**
 * Created by huang.tudou on 5/9/16.
 */
public class EMRInfoExtractionTask {
    private String name = null;
    private EMRTree emrTree = null;
    private DictInputText dictInputTXT = null;
    private EMRInput emrInput = null;
    private DataOutputText dataOutput2TXT = null;
    private DataOutputText dataOutput2md = null;

    public EMRInfoExtractionTask(String name, String folderName, String originName) {
        this.name = name;
        this.emrTree = new EMRTree();
        this.dictInputTXT = new DictInputText("data" + File.separator + "intermediate" + File.separator + "dictionary"+ File.separator + name + ".txt");
        this.emrInput = new EMRInput("data" + File.separator + "input"+ File.separator + "emr" + File.separator + folderName + File.separator + originName + ".xml");
        this.dataOutput2TXT = new DataOutputText("data"+ File.separator + "output"+ File.separator + folderName + File.separator + "txt"+ File.separator + folderName + "_" + originName + ".txt");
        this.dataOutput2md = new DataOutputText("data" + File.separator + "output"+ File.separator + folderName + File.separator + "md"+ File.separator + folderName + "_" + originName + ".md");
    }

    // import dictionary and generate tree
    public void genEMRTree() {
        this.dictInputTXT.GenerateEMRInfoTree(this.emrTree);
    }

    // infomation extraction
    public void extractionInfo() {
        this.emrTree.parseEMRData(this.emrInput.EMRDataReader());
    }

    // out put
    public void output2txt() {
        this.dataOutput2TXT.OutPut2txt(this.emrTree.toString());
    }

    public void output2md() {
        this.dataOutput2md.OutPut2txt(this.emrTree.toStringMd());
    }

    /******** io.util ********/
    public String genName(String s) {
        String name = null;
        int startIndex = s.lastIndexOf(File.separator);
        int endIndex = s.lastIndexOf(".");

        if((startIndex != -1) && (endIndex != -1)) {
            name = s.substring(startIndex + 1, endIndex);
        }

        return name;
    }

    public String getName() {
        return this.name;
    }

    public EMRTree getEmrTree() {
        return this.emrTree;
    }

    public void print() {
        this.emrTree.printTree();
    }
}
