package basic;

import java.util.*;

/**
 * Created by huang.tudou
 */
public class EMRLeafNode extends EMRNode {
    private ArrayList<String> alias = null;
    private ArrayList<String> segments = null;
    private int type = 1;
    private Boolean exist = null; // null: [无此项]; true: [存在]; false: [不存在].
    // type[1] leaf node
    private Boolean isNegative = false;
    private Boolean isDurationNeeded = false; // 是否需要提取患病/症状持续时间【年/月/日/小时】
    private String duration = null;
    // type[2] leaf node
    private String value = null;
    private ArrayList<String> possibleUnit = null;
    private String unit = null;

    public EMRLeafNode (String name, int level) {
        super(name, level);
        super.setIsLeaf(true);
    }

    public EMRLeafNode (String name, int level, int type, Boolean isDurationNeeded) {
        super(name, level);
        super.setIsLeaf(true);
        this.type = type;
        this.isDurationNeeded = isDurationNeeded;
    }

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
    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
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

    public void addSegment(String seg) {
        if(this.segments == null) {
            this.segments = new ArrayList<String>();
        }

        this.segments.add(seg);
    }

    public ArrayList<String> getSegments() {
        return this.segments;
    }

    public Boolean getExist() {
        return this.exist;
    }

    // type[1] util
    public String getDuration() {
        return this.duration;
    }

    public void setIsDurationNeeded() {
        this.isDurationNeeded = true;
    }

    public Boolean getIsDurationNeeded() {
        return this.isDurationNeeded;
    }

    // type[2] util
    public String getValue() {
        return this.value;
    }

    public String getUnit() {
        return this.unit;
    }

    public void addPossibleUnit(String unit) {
        if(this.possibleUnit == null) {
            this.possibleUnit = new ArrayList<String>();
        }

        this.possibleUnit.add(unit);
    }

    public ArrayList<String> getPossibleUnit() {
        return this.possibleUnit;
    }
}
