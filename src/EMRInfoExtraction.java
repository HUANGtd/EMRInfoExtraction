import basic.EMRTree;
import io.DataInputTXT;
import io.DataOutputTXT;

/**
 * Created by huang.tudou
 */
public class EMRInfoExtraction {
    public static void main(String args[]) {
        EMRTree admission_tree = new EMRTree();
        DataInputTXT admission_dict = new DataInputTXT("data/admission.txt");

        // import dictionary and generate tree
        admission_dict.GenerateEMRInfoTree(admission_tree);

        // infomation extraction
        DataInputTXT data = new DataInputTXT("data/data/admission.xml");
        admission_tree.parseEMRData(data.EMRDataReader());
//        admission_tree.printTree();

        // out put
        DataOutputTXT dop = new DataOutputTXT("data/result/r_admission.txt");
        dop.OutPut2txt(admission_tree.toString());
    }
}
