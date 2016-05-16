package basic.tree;

import basic.segment.EMRSegment;
import basic.segment.EMRSegmentOfExist;
import basic.segment.EMRSegmentOfFamilyHistory;
import basic.segment.EMRSegmentOfValue;

import java.util.*;

/**
 * Created by huang.tudou
 */
public class EMRLeafNode extends EMRNode {
    private final static int TYPE_OF_EXIST = 1;
    private final static int TYPE_OF_VALUE = 2;
    private final static int TYPE_OF_FAMILY_HISTORY = 3;

    private ArrayList<String> alias = null;
    private ArrayList<EMRSegment> segments = null;
    private int type = 1;
    private Boolean exist = true; //true: [存在]; false: [不存在].

    // 是否需要统计病情持续时间
    private Boolean isDurationNeeded = false; // 是否需要提取患病/症状持续时间【年/月/日/小时】
    // 家族史中需要统计亲属患病的情况
    protected Boolean isRelativesNeeded = false;
    // 家族史中可能需要关注其他关键词
    protected ArrayList<String> possibleOtherKeywords = null;
    // 需要统计“值“
    private ArrayList<String> possibleUnit = null;

    public EMRLeafNode (String name, int level) {
        super(name, level);
        super.setIsLeaf(true);
        this.addAlias(name);
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
                EMRSegment newSegment = null;

                if(this.type == TYPE_OF_EXIST) {
                    newSegment = new EMRSegmentOfExist(find, s, this.isDurationNeeded);
                    newSegment.parse();
                } else if(this.type == TYPE_OF_VALUE) {
                    newSegment = new EMRSegmentOfValue(find, s, this.isDurationNeeded, this.possibleUnit);
                    newSegment.parse();
                } else {
                    newSegment = new EMRSegmentOfFamilyHistory(find, s, this.isRelativesNeeded, this.possibleUnit);
                    newSegment.parse();
                    if(this.possibleOtherKeywords != null) {
                        newSegment.extractOtherKeywords(this.possibleOtherKeywords);
                    }
                }
                addSegment(newSegment);
            }
        }

        if(this.segments == null) {
            this.exist = false;
        }
    }

    public String containsAny(String s) {
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

        if(! this.alias.contains(alias)) {
            this.alias.add(alias);
        }
    }

    public void sortAlias() {
        Collections.sort(this.alias, new Comparator<String>() {
            @Override
            public int compare(String alias1, String alias2) {
                return  alias2.length() - alias1.length();
            }
        });
    }

    public ArrayList<String> getAlias() {
        return this.alias;
    }

    public Boolean getIsRelativesNeeded() {
        return this.isRelativesNeeded;
    }

    public void addSegment(EMRSegment seg) {
        if(this.segments == null) {
            this.segments = new ArrayList<EMRSegment>();
        }

        if(! this.segments.contains(seg)) {
            this.segments.add(seg);
        }
    }

    public ArrayList<EMRSegment> getSegments() {
        return this.segments;
    }

    public Boolean getExist() {
        return this.exist;
    }

    public void setIsRelativesNeeded(Boolean isRelativesNeeded) {
        this.isRelativesNeeded = isRelativesNeeded;
    }

    public void addPossibleOtherKeywords(String otherKeyword) {
        if(this.possibleOtherKeywords == null) {
            this.possibleOtherKeywords = new ArrayList<String>();
        }

        if(! this.possibleOtherKeywords.contains(otherKeyword)) {
            this.possibleOtherKeywords.add(otherKeyword);
        }
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

        if(! this.possibleUnit.contains(unit)) {
            this.possibleUnit.add(unit);
        }
    }

    public ArrayList<String> getPossibleUnit() {
        return this.possibleUnit;
    }
}
