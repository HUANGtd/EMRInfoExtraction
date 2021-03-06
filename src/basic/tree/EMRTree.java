package basic.tree;

import basic.segment.EMRSegment;

import java.util.ArrayList;

/**
 * Created by huang.tudou
 */
public class EMRTree {
    private EMRNode root;
    private String name = null;
    private ArrayList<EMRLeafNode> leaves = null;

    public EMRTree() {
        this.root = new EMRNode("ROOT", 0);
    }

    public EMRTree(EMRNode root) {
        this.root = root;
    }

    /******** parse data ********/
    public void parseEMRData(String data) {
        root.setContent(data);

        for(EMRNode node : root.getChildren()) {
            node.setContent(getContent(node.getName(), data));
            setOffspringContent(node);
        }

        for(EMRLeafNode leaf : this.leaves) {
            if(leaf.getContent() != null) {
                leaf.setSegments();
            }
        }
    }

    public void setOffspringContent(EMRNode node) {
        if(node.getIsLeaf()) {
            node.setContent(node.getFather().getContent());
            return;
        }

        if(node.getFather() != null && node.getFather() != this.root) { // not root or 1-lv node
            node.setContent(node.getFather().getContent());
        }

        for(EMRNode child : node.getChildren()) {
            setOffspringContent(child);
        }
    }

    public String getContent(String name, String data) {
        String content = null;
        int startIndex = data.indexOf("<Name>" + name + "</Name><InnerValue>");
        int endIndex = data.indexOf("</InnerValue><BackgroundText>" + name + "</BackgroundText>");

        if(startIndex < 0 || endIndex < 0) {
//            return this.root.getContent();
            return null;
        }
        content = data.substring(startIndex, endIndex);
        String mark = "<InnerValue>";
        content = content.substring(content.indexOf(mark) + mark.length());
        content = content.replaceAll(" ", ""); // remove blanks
//        content = content.replaceAll("\\s*", "");

        return content;
    }

    /******** print to .txt ********/
    public void printTree() {
        System.out.print(toString());
    }

    @Override
    public String toString() {
        StringBuffer tree2String = new StringBuffer();
        printNode(this.root, tree2String);

        return tree2String.toString();
    }

    public void printNode(EMRNode node, StringBuffer tree2String) {
        if(node.getIsLeaf() == true) {
            printLeaf((EMRLeafNode)node, tree2String);
            return;
        }

        if(node == this.root) {
            for(EMRNode child : node.getChildren()) {
                printNode(child, tree2String);
            }
            return;
        }

        for(int i = 0; i < node.getLevel()-1; i++) {
            tree2String.append("    ");
        }

        // if that area dose not exists in the data
        if(node.getContent() == null) {
            tree2String.append(node.getName() + ": 0\n");
            return;
        }

        tree2String.append(node.getName() + "\n");
        if(node.getLevel() == 1) {
            tree2String.append("-----------------------------------------\n");
            tree2String.append("[" + node.getContent() + "]\n");
            tree2String.append("-----------------------------------------\n");
        }

        for(EMRNode child : node.getChildren()) {
            printNode(child, tree2String);
        }
        tree2String.append("\n");
    }

    public void printLeaf(EMRLeafNode node, StringBuffer tree2String) {
        if(node.getExist() == false) {
            return;
        }
        for(int i = 0; i < node.getLevel()-1; i++) {
            tree2String.append("    ");
        }

        tree2String.append("*[" + node.getType() + "][" + node.getAlias().get(0));
        if(node.getAlias().size() > 1) {
            for(int i = 1; i < node.getAlias().size(); i++) {
                tree2String.append("/" + node.getAlias().get(i));
            }
        }
        tree2String.append("]\n");

        ArrayList<EMRSegment> segments = node.getSegments();
        if(node.getExist() == null) {
            tree2String.append(": 未找到");
        } else {
            for(EMRSegment s : segments) {
                for(int i = 0; i < node.getLevel(); i++) {
                    tree2String.append("    ");
                }
                tree2String.append("+-------------------------\n");
                for(int i = 0; i < node.getLevel(); i++) {
                    tree2String.append("    ");
                }
                tree2String.append("| ");

                // 是/否
                if(s.getYesOrNo() != null) {
                    if(s.getYesOrNo() == true) {
                        tree2String.append("[是]");
                    } else {
                        tree2String.append("[否]");
                    }
                    tree2String.append(" | ");
                }

                // 如果需要考虑+原文中存在患病时长
                if(node.getIsDurationNeeded() == true) {
                    if(s.getDuration() != null) {
                        tree2String.append("[" + s.getDuration() + "] | ");
                    }
                }

                // 如果需要考虑+原文中存在亲属患病
                if(node.getIsRelativesNeeded() == true) {
                    if (s.getRelatives() != null) {
                        for (String key : s.getRelatives().keySet()) {
                            tree2String.append("[" + key + "]" + (s.getRelatives().get(key) == "N/A" ? "" : "[" + s.getRelatives().get(key) + "][岁]"));
                        }
                        tree2String.append(" | ");
                    }
                }

                // 如果需要考虑+原文中存在其他关键词
                if(s.getOtherKeywords() != null) {
                    for(String keyword : s.getOtherKeywords()) {
                        tree2String.append("[" + keyword + "]");
                    }
                    tree2String.append(" | ");
                }

                // 如果提出到了值
                tree2String.append("[" + s.getKeyword() + "]");
                if(s.getValue() != null) {
                    if(s.getValue().equals("N/A")) {
                        tree2String.append("[" + s.getValue() + "]");
                    } else {
                        tree2String.append("[" + s.getValue() + "]" + "[" + s.getUnit() + "]");
                    }
                }

                tree2String.append(" | “" + s.getContext() + "”\n");
                for(int i = 0; i < node.getLevel(); i++) {
                    tree2String.append("    ");
                }
                tree2String.append("+-------------------------\n");
            }
        }

        tree2String.append("\n");
    }

    /******** print to .md ********/
    public String toStringMd() {
        StringBuffer tree2String = new StringBuffer();
        printNode2md(this.root, tree2String);

        return tree2String.toString();
    }

    public void printNode2md(EMRNode node, StringBuffer tree2String) {
        if(node.getIsLeaf() == true) {
            printLeaf2md((EMRLeafNode) node, tree2String);
            return;
        }

        if(node == this.root) {
            for(EMRNode child : node.getChildren()) {
                printNode2md(child, tree2String);
            }
            return;
        }

        // if that area dose not exists in the data
        if(node.getContent() == null) {
            tree2String.append(node.getName() + ": 0\n");
            return;
        }

        if(node.getLevel() == 1) {
            tree2String.append("## " + node.getName() + "\n");
            tree2String.append("> " + node.getContent() + "\n\n");
        } else {
            tree2String.append("### " + node.getName() + "\n");
        }

        for(EMRNode child : node.getChildren()) {
            printNode2md(child, tree2String);
        }
        tree2String.append("\n");
    }

    public void printLeaf2md(EMRLeafNode node, StringBuffer tree2String) {
        if(node.getExist() == false) {
            return;
        }
        tree2String.append("    ");

        tree2String.append("[类型" + node.getType() + "][" + node.getAlias().get(0));
        if(node.getAlias().size() > 1) {
            for(int i = 1; i < node.getAlias().size(); i++) {
                tree2String.append("/" + node.getAlias().get(i));
            }
        }
        tree2String.append("]\n");

        ArrayList<EMRSegment> segments = node.getSegments();
        if(node.getExist() == null) {
            tree2String.append(": 未找到");
        } else {
            for(EMRSegment s : segments) {
                tree2String.append("    ");
                tree2String.append("+-------------------------\n");
                tree2String.append("    ");
                tree2String.append("| ");

                // 是/否
                if(s.getYesOrNo() != null) {
                    if(s.getYesOrNo() == true) {
                        tree2String.append("[是]");
                    } else {
                        tree2String.append("[否]");
                    }
                    tree2String.append(" | ");
                }

                // 如果需要考虑+原文中存在亲属患病
                if(node.getIsRelativesNeeded() == true) {
                    if (s.getRelatives() != null) {
                        for (String key : s.getRelatives().keySet()) {
                            tree2String.append("[" + key + "]" + (s.getRelatives().get(key) == "N/A" ? "" : "[" + s.getRelatives().get(key) + "][岁]"));
                        }
                        tree2String.append(" | ");
                    }
                }

                // 如果需要考虑+原文中存在其他关键词
                if(s.getOtherKeywords() != null) {
                    for(String keyword : s.getOtherKeywords()) {
                        tree2String.append("[" + keyword + "]");
                    }
                    tree2String.append(" | ");
                }

                tree2String.append("[" + s.getKeyword() + "]");
                // 如果提出到了值
                if(s.getValue() != null) {
                    if(s.getValue().equals("N/A")) {
                        tree2String.append("[" + s.getValue() + "] |");
                    } else {
                        tree2String.append("[" + s.getValue() + "]" + "[" + s.getUnit() + "] |");
                    }
                } else {
                    tree2String.append(" | ");
                }

                // 如果需要考虑+原文中存在患病时长
                if(node.getIsDurationNeeded() == true) {
                    if(s.getDuration() != null) {
                        tree2String.append("[" + s.getDuration() + "] | ");
                    }
                }

                tree2String.append("\n    | “" + s.getContext() + "” |\n");
            }
            tree2String.append("    ");
            tree2String.append("+-------------------------\n");
        }

        tree2String.append("\n");
    }

    /******** util ********/
    public EMRNode getRoot() {
        return this.root;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addLeaf(EMRLeafNode node) {
        if(this.leaves == null) {
            this.leaves = new ArrayList<EMRLeafNode>();
        }

        this.leaves.add(node);
    }

    public ArrayList<EMRLeafNode> getLeaves() {
        return this.leaves;
    }
}
