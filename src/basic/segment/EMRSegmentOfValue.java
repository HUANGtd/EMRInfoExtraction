package basic.segment;

import java.util.*;
import java.util.regex.*;

/**
 * Created by hhw on 5/5/16.
 */
public class EMRSegmentOfValue extends EMRSegment {
    protected String value = "N/A";
    protected ArrayList<String> possibleUnit = null;
    protected String unit = "N/A";

    public EMRSegmentOfValue(String keyword, String context, Boolean isDurationNeeded, ArrayList<String> possibleUnit) {
        super(keyword, context, isDurationNeeded);
        this.possibleUnit = possibleUnit;
        this.sortUnits();
    }

    public void parse() {
        super.extractDuration();

        String cutFromKeyword = this.context.substring(this.context.indexOf(this.keyword) + this.keyword.length());

        for(String u : this.possibleUnit) {
            String lowerCaseOfUnit = u.toLowerCase();
            String lowerCaseOfCutFromKeyword = cutFromKeyword.toLowerCase();

            if(lowerCaseOfCutFromKeyword.contains(lowerCaseOfUnit)) {
                this.unit = u;
                if(lowerCaseOfUnit.contains("^")) {
                    lowerCaseOfUnit = lowerCaseOfUnit.substring(0, lowerCaseOfUnit.indexOf("^"));
                } else if(lowerCaseOfUnit.contains("*")) {
                    lowerCaseOfUnit = lowerCaseOfUnit.substring(0, lowerCaseOfUnit.indexOf("*"));
                }

                Pattern pattern = Pattern.compile("(([1-9]+[0-9]*(\\.[0-9]+)?)|(0\\.[0-9]+))((-|/)(([1-9]+[0-9]*(\\.[0-9]+)?)|(0\\.[0-9]+)))?" + lowerCaseOfUnit);
                Matcher matcher = pattern.matcher(lowerCaseOfCutFromKeyword);

                if(matcher.find()) {
                    this.value = matcher.group(0).substring(0, matcher.group(0).indexOf(lowerCaseOfUnit));
                }
            }
        }
    }

    /******** util ********/
    public void sortUnits() {
        Collections.sort(this.possibleUnit, new Comparator<String>() {
            @Override
            public int compare(String unit1, String unit2)
            {
                return  unit1.compareTo(unit2);
            }
        });
    }

    public String getValue() {
        return this.value;
    }

    public String getUnit() {
        return this.unit;
    }
}
