package basic;

import java.util.*;

/**
 * Created by huang.tudou
 */
public class EMRNode {
    protected String name;
    protected int level = -1;
    protected String content = null;
    protected EMRNode father = null;
    protected Boolean isLeaf = false;
    private ArrayList<EMRNode> children = null;

    public EMRNode(String name, int level) {
        this.name = name;
        this.level = level;
    }

    /******** basic util ********/
    public String getName() {
        return this.name;
    }

    public void setFather(EMRNode father) {
        this.father = father;
    }

    public EMRNode getFather() {
        return this.father;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Boolean getIsLeaf() {
        return this.isLeaf;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void addChild(EMRNode child) {
        if(this.children == null) {
            this.children = new ArrayList<EMRNode>();
        }

        this.children.add(child);
    }

    public ArrayList<EMRNode> getChildren() {
        return this.children;
    }

    public int getLevel() {
        return this.level;
    }

}
