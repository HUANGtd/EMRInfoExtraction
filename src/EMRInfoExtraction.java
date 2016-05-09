import basic.EMRTree;
import io.DictInputTXT;
import io.DataOutputTXT;

/**
 * Created by huang.tudou
 */
public class EMRInfoExtraction {
    public static void main(String args[]) {
        EMRInfoExtractionTask task1 = new EMRInfoExtractionTask("data/dictionary/txt/admission.txt", "data/emr/admission.xml", "data/result/r_admission.txt");
        task1.genEMRTree();
        task1.extractionInfo();
        task1.output2txt();
    }
}
