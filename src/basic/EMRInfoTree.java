package basic;

import java.util.ArrayList;

/**
 * Created by hhw on 4/21/16.
 */
public class EMRInfoTree {
    private EMRInfoNode root;
    private ArrayList<EMRInfoNode> leaves = null;

    public EMRInfoTree() {
        this.root = new EMRInfoNode("ROOT", 0);
    }

    public EMRInfoTree(EMRInfoNode root) {
        this.root = root;
    }

    /******** parse data ********/
    public void parseEMRData(String data) {
        root.setContent(data);

        for(EMRInfoNode node : root.getChildren()) {
            node.setContent(getContent(node.getName(), data));
            setOffspringContent(node);
        }

        for(EMRInfoNode leaf : this.leaves) {
            if(leaf.getContent() != null)
                leaf.setSegments();
        }
    }

    public void setOffspringContent(EMRInfoNode node) {
        if(node.getIsLeaf()) {
            node.setContent(node.getFather().getContent());
            return;
        }

        if(node.getFather() != null && node.getFather() != this.root) { // not root or 1-lv node
            node.setContent(node.getFather().getContent());
        }

        for(EMRInfoNode child : node.getChildren()) {
            setOffspringContent(child);
        }
    }

    public String getContent(String name, String data) {
        String content = null;
        int startIndex = data.indexOf("<Name>" + name + "</Name><InnerValue>");
        int endIndex = data.indexOf("</InnerValue><BackgroundText>" + name + "</BackgroundText>");

        if(startIndex < 0 || endIndex < 0) {
            return null;
        }
        content = data.substring(startIndex, endIndex);
        String mark = "<InnerValue>";
        content = content.substring(content.indexOf(mark) + mark.length());
        content = content.replaceAll(" ", ""); // remove blanks
//        content = content.replaceAll("\\s*", "");

        return content;
    }

    /******** print ********/
    public void printTree() {
        System.out.print(toString());
    }

    public String toString() {
        StringBuffer tree2String = new StringBuffer();
        printNode(this.root, tree2String);

        return tree2String.toString();
    }

    public void printNode(EMRInfoNode node, StringBuffer tree2String) {
        if(node.getIsLeaf() == true) {
            printLeaf(node, tree2String);
            return;
        }

        if(node == this.root) {
            for(EMRInfoNode child : node.getChildren()) {
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

        for(EMRInfoNode child : node.getChildren()) {
            printNode(child, tree2String);
        }
        tree2String.append("\n");
    }

    public void printLeaf(EMRInfoNode node, StringBuffer tree2String) {
        for(int i = 0; i < node.getLevel()-1; i++) {
            tree2String.append("    ");
        }
        tree2String.append("*[" + node.getName());
        if(node.getAlias() != null) {
            for(String s : node.getAlias()) {
                tree2String.append("/" + s);
            }
        }
        tree2String.append("]");

        ArrayList<String> segments = node.getSegments();
        if(segments == null) {
            tree2String.append(": 0");
        } else {
            tree2String.append(": " + segments.size() + "\n");
            for(String s : segments) {
                for(int i = 0; i < node.getLevel(); i++) {
                    tree2String.append("    ");
                }
                tree2String.append("+-------------------------\n");
                for(int i = 0; i < node.getLevel(); i++) {
                    tree2String.append("    ");
                }
                tree2String.append("| “" + s + "”\n");
                for(int i = 0; i < node.getLevel(); i++) {
                    tree2String.append("    ");
                }
                tree2String.append("+-------------------------\n");
            }
        }

        tree2String.append("\n");
    }

    /******** util ********/
    public EMRInfoNode getRoot() {
        return this.root;
    }

    public void addLeaf(EMRInfoNode node) {
        if(this.leaves == null) {
            this.leaves = new ArrayList<EMRInfoNode>();
        }

        this.leaves.add(node);
    }

    public ArrayList<EMRInfoNode> getLeaves() {
        return this.leaves;
    }
}
