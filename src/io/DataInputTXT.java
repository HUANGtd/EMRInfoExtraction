package io;

import basic.EMRLeafNode;
import basic.EMRNode;
import basic.EMRTree;

import java.io.*;

/**
 * Created by huang.tudou
 */
public class DataInputTXT {
    String fileName = null;

    public DataInputTXT(String fileName) {
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

    public EMRTree GenerateEMRInfoTree(EMRTree tree) {
        File file = new File(fileName);
        BufferedReader reader = null;
        EMRNode root = tree.getRoot();

        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(read);
            String line = null;
            EMRNode father = null; // for #...
            EMRNode subFather = null; // for ##...
            EMRLeafNode node = null; // for leaf node

            while((line = reader.readLine()) != null) {
                if(line.equals(""))
                    continue;

                String a[] = line.split(" ");

                if(a[0].equals("##")) {
                    if(father == null) {
                        System.out.println("Error: 缺少一级节点");
                    }
                    subFather = new EMRNode(a[1], father.getLevel()+1);
                    subFather.setFather(father);
                    father.addChild(subFather);
                } else if(a[0].equals("#")) {
                    subFather = null;
                    father = new EMRNode(a[1], root.getLevel()+1);
                    father.setFather(root);
                    root.addChild(father);
                } else {
                    if(subFather != null) {
                        node = new EMRLeafNode(a[1], subFather.getLevel()+1);
                        node.setFather(subFather);
                        subFather.addChild(node);
                    } else {
                        node = new EMRLeafNode(a[1], father.getLevel()+1);
                        node.setFather(father);
                        father.addChild(node);
                    }

                    if(a[0].equals("+")) {
                        node.setIsDurationNeeded();
                    } else if(a[0].equals("--")) {
                        node.setIsRelativesNeeded(true);
                    }

                    if(a.length > 2) {
                        int i;
                        for(i = 2; i < a.length; i++) {
                            if(a[i].equals("")) {
                                continue;
                            }
                            if(a[i].startsWith("*")) {
                                node.setType(2);
                                break;
                            }
                            node.addAlias(a[i]);
                        }

                        for(; i < a.length; i++) {
                            if(a[i].equals("")) {
                                continue;
                            }
                            if(a[i].startsWith("#")) {
                                node.setType(2);
                                break;
                            }
                            node.addPossibleUnit(a[i].substring(1));
                        }

                        for(; i < a.length; i++) {
                            if(a[i].equals("")) {
                                continue;
                            }
                            node.addPossibleOtherKeywords(a[i].substring(1));
                        }
                    }

                    tree.addLeaf(node);
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
