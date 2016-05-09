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
    private DictInputTXT dictInputTXT = null;
    private EMRInput emrInput = null;
    private DataOutputTXT dataOutput2TXT = null;

    public EMRInfoExtractionTask(String name, String folderName) {
        this.name = name;
        this.emrTree = new EMRTree();
        this.dictInputTXT = new DictInputTXT("data/intermediate/dictionary/" + name + ".txt");
        this.emrInput = new EMRInput("data/input/emr/" + folderName + "/" + name + ".xml");
        this.dataOutput2TXT = new DataOutputTXT("data/intermediate/result/" + folderName + "/" + folderName + "_" + name + ".txt");
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

    /******** util ********/
    public String genName(String s) {
        String name = null;
        int startIndex = s.lastIndexOf("/");
        int endIndex = s.lastIndexOf(".");

        if((startIndex != -1) && (endIndex != -1)) {
            name = s.substring(startIndex + 1, endIndex);
        }

        return name;
    }

    public String getName() {
        return this.name;
    }

    public void print() {
        this.emrTree.printTree();
    }
}
