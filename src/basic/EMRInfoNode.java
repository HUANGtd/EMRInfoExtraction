package basic;

import java.util.*;

/**
 * Created by hhw on 4/21/16.
 */
public class EMRInfoNode {
    private String name;
    private int level = -1;
    private String content = null;
    private EMRInfoNode father = null;
    private Boolean isLeaf = false;
    private String value = null;
    private ArrayList<EMRInfoNode> children = null;
    // for leaf nodes
    private ArrayList<String> alias = null;
    private ArrayList<String> segments = null;

    public EMRInfoNode(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public EMRInfoNode(String name, ArrayList<String> alias, int level) {
        this.name = name;
        this.alias = alias;
        this.level = level;
    }

    /******** for leaf nodes ********/
    public void setSegments() {
        String copy = this.content.replaceAll("，", "---");
        copy = copy.replaceAll("。", "---");
        copy = copy.replaceAll("\n", "---");
        String sentence[] = copy.split("---");

        for(String s : sentence) {
            String find = containsAny(s);
            if(find != null) {
                s = s.replace(find, "[" + find + "]");
                addSegment(s);
            }
        }
    }

    public String containsAny(String s) {
        if(s.contains(this.name))
            return this.name;

        if(this.alias != null) {
            for(String a : this.alias) {
                if(s.contains(a))
                    return a;
            }
        }

        return null;
    }

    /******** util ********/
    public String getName() {
        return this.name;
    }

    public void addAlias(String alias) {
        if(this.alias == null) {
            this.alias = new ArrayList<String>();
        }

        this.alias.add(alias);
    }

    public ArrayList<String> getAlias() {
        return this.alias;
    }

    public void setFather(EMRInfoNode father) {
        this.father = father;
    }

    public EMRInfoNode getFather() {
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

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void addChild(EMRInfoNode child) {
        if(this.children == null) {
            this.children = new ArrayList<EMRInfoNode>();
        }

        this.children.add(child);
    }

    public ArrayList<EMRInfoNode> getChildren() {
        return this.children;
    }

    public int getLevel() {
        return this.level;
    }

    public void addSegment(String seg) {
        if(this.segments == null) {
            this.segments = new ArrayList<String>();
        }

        this.segments.add(seg);
    }

    public ArrayList<String> getSegments() {
        return this.segments;
    }
}
