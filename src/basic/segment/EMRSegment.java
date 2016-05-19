package basic.segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

/**
 * Created by huang.tudou
 */
public abstract class EMRSegment {
    protected String keyword = null;
    protected String context = null;
    protected Boolean isDurationNeeded = false; // 是否需要提取患病/症状持续时间【年/月/日/小时】
    protected String duration = null;

    public EMRSegment(String keyword, String context, Boolean isDurationNeeded) {
        this.keyword = keyword;
        this.context = context;
        this.isDurationNeeded = isDurationNeeded;
    }

    public abstract void parse();

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

    /******** io.util ********/
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

    public void addRelative() {}

    public HashMap<String, String> getRelatives() {
        return null;
    }

    public void addOtherKeywords(String otherKeyword) {}

    public ArrayList<String> getOtherKeywords() {
        return null;
    }

    public String getValue() {
        return null;
    }

    public String getUnit() {
        return null;
    }

    public void extractOtherKeywords(ArrayList<String> otherPossibleKeywords) {}

    public Boolean getYesOrNo() {
        return null;
    }
}
