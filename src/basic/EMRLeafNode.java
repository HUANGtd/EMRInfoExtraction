package basic;

import javax.swing.text.Segment;
import java.util.*;

/**
 * Created by huang.tudou
 */
public class EMRLeafNode extends EMRNode {
    private ArrayList<String> alias = null;
    private ArrayList<EMRSegment> segments = null;
    private int type = 1;
    private Boolean exist = true; //true: [存在]; false: [不存在].
    private Boolean yesOrNo = null;
    // type[1] leaf node
    private Boolean isDurationNeeded = false; // 是否需要提取患病/症状持续时间【年/月/日/小时】
    // 家族史中需要统计亲属患病的情况
    protected Boolean isRelativesNeeded = false;
    // 家族史中可能需要关注其他关键词
    protected ArrayList<String> possibleOtherKeywords = null;
    // type[2] leaf node
    private ArrayList<String> possibleUnit = null;

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
        copy = copy.replaceAll("；", "---");
        copy = copy.replaceAll("\n", "---");
        String sentence[] = copy.split("---");

        for(String s : sentence) {
            String find = containsAny(s);
            if(find != null) {
//                EMRSegment newSegment = (this.type == 1)? new EMRSegmentOfExist(find, s, this.isDurationNeeded): new EMRSegmentOfValue(find, s, this.isDurationNeeded, this.possibleUnit);
                EMRSegment newSegment = null;
                if(this.type == 1) {
                    newSegment = new EMRSegmentOfExist(find, s, this.isDurationNeeded, this.isRelativesNeeded);
                    this.yesOrNo = newSegment.parse();
                } else {
                    newSegment = new EMRSegmentOfValue(find, s, this.isDurationNeeded, this.isRelativesNeeded, this.possibleUnit);
                    newSegment.parse();
//                    if(!newSegment.parse()) {
//                        addSegment(newSegment);
//                    }
                }
                if(this.possibleOtherKeywords != null) {
                    newSegment.extractOtherKeywords(this.possibleOtherKeywords);
                }
                addSegment(newSegment);
            }
        }

        if(this.segments == null) {
            this.exist = false;
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

    public void addSegment(EMRSegment seg) {
        if(this.segments == null) {
            this.segments = new ArrayList<EMRSegment>();
        }

        this.segments.add(seg);
    }

    public ArrayList<EMRSegment> getSegments() {
        return this.segments;
    }

    public Boolean getExist() {
        return this.exist;
    }

    public Boolean getYesOrNo() {
        return this.yesOrNo;
    }

    public void setIsRelativesNeeded(Boolean isRelativesNeeded) {
        this.isRelativesNeeded = isRelativesNeeded;
    }

    public void addPossibleOtherKeywords(String otherKeyword) {
        if(this.possibleOtherKeywords == null) {
            this.possibleOtherKeywords = new ArrayList<String>();
        }

        this.possibleOtherKeywords.add(otherKeyword);
    }

    // type[1] util
    public void setIsDurationNeeded() {
        this.isDurationNeeded = true;
    }

    public Boolean getIsDurationNeeded() {
        return this.isDurationNeeded;
    }

    // type[2] util
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
