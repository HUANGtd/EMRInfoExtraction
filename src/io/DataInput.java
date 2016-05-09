package io;

import basic.EMRInfoNode;
import basic.EMRInfoTree;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * Created by hhw on 4/21/16.
 */
public class DataInput {
    String fileName = null;

    public DataInput(String fileName) {
        this.fileName = fileName;
    }

    public String EMRDataReader() {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer strBuffer = new StringBuffer();

        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(read);
            String line = null;

            while((line = reader.readLine()) != null) {
                strBuffer.append(line + "\n");
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {}
            }
        }

        return strBuffer.toString();
    }

    public EMRInfoTree GenerateEMRInfoTree(EMRInfoTree tree) {
        File file = new File(fileName);
        BufferedReader reader = null;
        EMRInfoNode root = tree.getRoot();

        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(read);
            String line = null;
            EMRInfoNode father = null; // for #...
            EMRInfoNode subFather = null; // for ##...
            EMRInfoNode node = null; // for leaf node

            while((line = reader.readLine()) != null) {
                if(line.equals(""))
                    continue;

                String a[] = line.split(" ");

                if(a[0].startsWith("##")) {
                    if(father == null) {
                        System.out.println("Error: 缺少一级节点");
                    }
                    subFather = new EMRInfoNode(a[0].substring(2), father.getLevel()+1);
                    subFather.setFather(father);
                    father.addChild(subFather);
                } else if(a[0].startsWith("#")) {
                    subFather = null;
                    father = new EMRInfoNode(a[0].substring(1), root.getLevel()+1);
                    father.setFather(root);
                    root.addChild(father);
                } else {
                    if(subFather != null) {
                        node = new EMRInfoNode(a[0], subFather.getLevel()+1);
                        node.setFather(subFather);
                        subFather.addChild(node);
                    } else {
                        node = new EMRInfoNode(a[0], father.getLevel()+1);
                        node.setFather(father);
                        father.addChild(node);
                    }
                    node.setIsLeaf(true);
                    tree.addLeaf(node);

                    if(a.length > 1) {
                        for(int i = 1; i < a.length; i++) {
                            node.addAlias(a[i]);
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {}
            }
        }

        return tree;
    }
}
