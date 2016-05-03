import basic.EMRTree;
import io.DataInput;
import io.DataOutput;

/**
 * Created by huang.tudou
 */
public class EMRInfoExtraction {
    public static void main(String args[]) {
        EMRTree admission_tree = new EMRTree();
        DataInput admission_dict = new DataInput("data/admission.txt");

        // import dictionary and generate tree
        admission_dict.GenerateEMRInfoTree(admission_tree);

        // infomation extraction
        DataInput data = new DataInput("data/data/admission.xml");
        admission_tree.parseEMRData(data.EMRDataReader());
        admission_tree.printTree();

        // out put
//        DataOutput dop = new DataOutput("data/result/r_admission.txt");
//        dop.OutPut2txt(admission_tree.toString());
    }
}
