package basic.segment;

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

        String cutFormKeyword = this.context.substring(this.context.indexOf(this.keyword) + this.keyword.length());

        for(String u : this.possibleUnit) {
            String lowerCaseOfUnit = u.toLowerCase();
            String lowerCaseOfCutFormKeyword = cutFormKeyword.toLowerCase();

            if(lowerCaseOfCutFormKeyword.contains(lowerCaseOfUnit)) {
                this.unit = u;
                if(lowerCaseOfUnit.contains("^")) {
                    lowerCaseOfUnit = lowerCaseOfUnit.substring(0, lowerCaseOfUnit.indexOf("^"));
                } else if(lowerCaseOfUnit.contains("*")) {
                    lowerCaseOfUnit = lowerCaseOfUnit.substring(0, lowerCaseOfUnit.indexOf("*"));
                }
                Pattern pattern = Pattern.compile("(([1-9]+[0-9]*(\\.[0-9]+)?)|(0\\.[0-9]+))((-|/)(([1-9]+[0-9]*(\\.[0-9]+)?)|(0\\.[0-9]+)))?" + lowerCaseOfUnit);
                Matcher matcher = pattern.matcher(lowerCaseOfCutFormKeyword);

                if(matcher.find()) {
                    this.value = matcher.group(0).substring(0, matcher.group(0).indexOf(lowerCaseOfUnit));
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
