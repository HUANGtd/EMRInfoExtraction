package io;

import basic.tree.EMRLeafNode;
import basic.tree.EMRNode;
import basic.tree.EMRTree;

import java.io.*;

/**
 * Created by huang.tudou
 */
public class DictInputTXT {
    private String fileName = null;

    public DictInputTXT(String fileName) {
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
                        node.sortAlias(); // 排序，以保证能够优先匹配更准确的关键词，"血糖"&"血糖2h"

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