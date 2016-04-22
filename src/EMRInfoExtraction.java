import basic.EMRInfoTree;
import io.DataInput;
import io.DataOutput;

/**
 * Created by hhw on 4/21/16.
 */
public class EMRInfoExtraction {
    public static void main(String args[]) {
        EMRInfoTree tree = new EMRInfoTree();
        DataInput dict = new DataInput("data/category.txt");

        for(int i = 1; i < 10; i++) {
            // import dictionary and generate tree
            tree = new EMRInfoTree();
            dict.GenerateEMRInfoTree(tree);

            // infomation extraction
            DataInput data = new DataInput("data/data/data" + i + ".xml");
            tree.parseEMRData(data.EMRDataReader());

            // out put
            DataOutput dop = new DataOutput("data/result/result" + i + ".txt");
            dop.OutPut2txt(tree.toString());
        }
    }
}
