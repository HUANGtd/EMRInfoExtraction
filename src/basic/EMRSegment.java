package basic;

import java.util.ArrayList;
import java.util.regex.*;

/**
 * Created by huang.tudou
 */
public class EMRSegment {
    protected String keyword = null;
    protected String context = null;
    protected Boolean isDurationNeeded = false; // 是否需要提取患病/症状持续时间【年/月/日/小时】
    protected String duration = null;

    // 家族史中需要统计亲属患病的情况
    protected Boolean isRelativesNeeded = false;
    protected ArrayList<String> relatives = null;

    // 家族史中可能需要关注其他关键词
    protected ArrayList<String> otherKeywords = null;

    public EMRSegment(String keyword, String context, Boolean isDurationNeeded, Boolean isRelativesNeeded) {
        this.keyword = keyword;
        this.context = context;
        this.isDurationNeeded = isDurationNeeded;
        this.isRelativesNeeded = isRelativesNeeded;
    }

    public Boolean parse() {
        return true;
    }

    public void extractDuration() {
        if(this.isDurationNeeded == false) {
            return;
        }

        Pattern pattern1 = Pattern.compile("[1-9]+[0-9]*(个月|月|年|年前|年余|小时)");
        Matcher matcher1 = pattern1.matcher(this.context);

        if(matcher1.find()) {
            this.duration = matcher1.group(0);
        } else {
            Pattern pattern2 = Pattern.compile("[1-2][0-9]{3}年");
            Matcher matcher2 = pattern1.matcher(this.context);

            if(matcher2.find()) {
                this.duration = String.valueOf(2016 - Integer.parseInt(matcher2.group(0).substring(0, matcher2.group(0).indexOf("年"))));
            }
        }
    }

    public void extractRelatives() {
        if(this.isRelativesNeeded == false) {
            return;
        }

        String[] family = {"母亲", "父亲", "妈妈", "舅舅", "姑姑", "爸爸", "哥哥", "弟弟", "姐姐", "妹妹", "儿子", "女儿"};
        String valueIn = null;

        if(this.context.contains("患有" + this.keyword)) {
            valueIn = this.context.substring(0, this.context.indexOf("患有" + this.keyword));
        } else if(this.context.contains("得有" + this.keyword)) {
            valueIn = this.context.substring(0, this.context.indexOf("得有" + this.keyword));
        } else if(this.context.contains("有" + this.keyword)) {
            valueIn = this.context.substring(0, this.context.indexOf("有" + this.keyword));
        } else {
            return;
        }

        for(String s : family) {
            if(valueIn.contains(s)) {
                this.addRelative(s);
            }
        }
    }

    public void extractOtherKeywords(ArrayList<String> otherPossibleKeywords) {
        for(String key : otherPossibleKeywords) {
            if(this.context.contains(key)) {
                addOtherKeywords(key);
            }
        }
    }

    /******** util ********/
    public String getDuration() {
        return this.duration;
    }

    public void setIsDurationNeeded() {
        this.isDurationNeeded = true;
    }

    public Boolean getIsDurationNeeded() {
        return this.isDurationNeeded;
    }

    public String getContext() {
        return this.context;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void addRelative(String relative) {
        if(this.relatives == null) {
            this.relatives = new ArrayList<String>();
        }

        this.relatives.add(relative);
    }

    public ArrayList<String> getRelatives() {
        return this.relatives;
    }

    public void addOtherKeywords(String otherKeyword) {
        if(this.otherKeywords == null) {
            this.otherKeywords = new ArrayList<String>();
        }

        this.otherKeywords.add(otherKeyword);
    }

    public ArrayList<String> getOtherKeywords() {
        return this.otherKeywords;
    }

    public String getValue() {
        return null;
    }

    public String getUnit() {
        return null;
    }

}
