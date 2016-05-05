package basic;

import java.util.*;
import java.util.regex.*;

/**
 * Created by hhw on 5/5/16.
 */
public class EMRSegmentOfValue extends EMRSegment{
    private String value = null;
    private ArrayList<String> possibleUnit = null;
    private String unit = null;

    public EMRSegmentOfValue(String keyword, String context, Boolean isDurationNeeded, Boolean isRelativesNeeded,  ArrayList<String> possibleUnit) {
        super(keyword, context, isDurationNeeded, isRelativesNeeded);
        this.possibleUnit = possibleUnit;
    }

    public Boolean parse() {
        super.extractDuration();
        super.extractRelatives();

        String cutFormKeyword = super.context.substring(super.context.indexOf(super.keyword) + super.keyword.length());

        for(String u : this.possibleUnit) {
            if(cutFormKeyword.contains(u)) {
                this.unit = u;
                if(u.contains("^")) {
                    u = u.substring(0, u.indexOf("^"));
                } else if(u.contains("*")) {
                    u = u.substring(0, u.indexOf("*"));
                }
                Pattern pattern = Pattern.compile("(([1-9]+[0-9]*(\\.[0-9]+)?)|(0\\.[0-9]+))((-|/)(([1-9]+[0-9]*(\\.[0-9]+)?)|(0\\.[0-9]+)))?" + u);
                Matcher matcher = pattern.matcher(cutFormKeyword);
                if(matcher.find()) {
                    this.value = matcher.group(0).substring(0, matcher.group(0).indexOf(u));
                }

                return true;
            }
        }

        // 若没有找到对应单位，视为没找到
        return false;
    }

    /******** util ********/
    public String getValue() {
        return this.value;
    }

    public String getUnit() {
        return this.unit;
    }
}
