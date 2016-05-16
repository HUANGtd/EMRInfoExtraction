package io;

import basic.tree.EMRLeafNode;
import basic.tree.EMRNode;
import basic.tree.EMRTree;

import java.io.*;

/**
 * Created by huang.tudou
 */
public class DictInputText {
    private final static int TYPE_OF_EXIST = 1;
    private final static int TYPE_OF_VALUE = 2;
    private final static int TYPE_OF_FAMILY_HISTORY = 3;
    private String fileName = null;

    public DictInputText(String fileName) {
        this.fileName = fileName;
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
                        throw new RuntimeException("！输入文件有误：未标识的一级节点（#）");
                    }
                    subFather = new EMRNode(a[1], father.getLevel()+1);
                    subFather.setFather(father);
                    father.addChild(subFather);
                } else if(a[0].equals("#")) {
                    subFather = null;
                    father = new EMRNode(a[1], root.getLevel()+1);
                    father.setFather(root);
                    root.addChild(father);
                } else if(!a[0].equals("")){
                    if(subFather != null) {
                        node = new EMRLeafNode(a[1], subFather.getLevel()+1);
                        node.setFather(subFather);
                        subFather.addChild(node);
                    } else if(father != null){
                        node = new EMRLeafNode(a[1], father.getLevel()+1);
                        node.setFather(father);
                        father.addChild(node);
                    } else {
                        throw new RuntimeException("！输入文件有误：未标识的一级节点（#）");
                    }

                    if(a[0].equals("+")) {
                        node.setIsDurationNeeded();
                    } else if(a[0].equals("=-")) {
                        node.setType(TYPE_OF_FAMILY_HISTORY);
                        node.setIsRelativesNeeded(true);
                    } else if(a[0].equals("=")) {
                        node.setType(TYPE_OF_FAMILY_HISTORY);
                    } else if(! a[0].equals("-")) {
                        throw new RuntimeException("！输入文件有误：存在未标识的关键词或者关键词标识有误");
                    }

                    if(a.length > 2) {
                        int i;
                        for(i = 2; i < a.length; i++) {
                            if(a[i].equals("")) {
                                continue;
                            } else if(a[i].startsWith("*")) {
                                node.addPossibleUnit(a[i].substring(1));
                            } else if(a[i].startsWith("#")) {
                                node.addPossibleOtherKeywords(a[i].substring(1));
                            } else {
                                node.addAlias(a[i]);
                            }
                        }
                        if(node.getType() != TYPE_OF_FAMILY_HISTORY) {
                            if(node.getPossibleUnit() != null) {
                                node.setType(TYPE_OF_VALUE);
                            }
                        }
                        node.sortAlias(); // 排序，以保证能够优先匹配更准确的关键词，"血糖"&"血糖2h"
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
