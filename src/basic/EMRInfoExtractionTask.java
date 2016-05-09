package basic;

import basic.tree.EMRTree;
import io.*;

/**
 * Created by huang.tudou on 5/9/16.
 */
public class EMRInfoExtractionTask {
    private String name = null;
    private EMRTree emrTree = null;
    private DictInputTXT dictInputTXT = null;
    private EMRInput emrInput = null;
    private DataOutputTXT dataOutput2TXT = null;

    public EMRInfoExtractionTask(String dictInputTXT, String emrInput, String output) {
        this.emrTree = new EMRTree();
        this.dictInputTXT = new DictInputTXT(dictInputTXT);
        this.emrInput = new EMRInput(emrInput);
        this.dataOutput2TXT = new DataOutputTXT(output);
        this.name = genName(dictInputTXT);
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
            name = s.substring(startIndex+1, endIndex);
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
