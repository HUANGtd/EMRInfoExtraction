package basic.segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hhw on 5/10/16.
 */

public class EMRSegmentOfFamilyHistory extends EMRSegmentOfValue {
    // 家族史中需要统计亲属患病的情况
    private Boolean isRelativesNeeded = false;
    private HashMap<String, String> relativesMapAge = null;
    // 家族史中可能需要关注其他关键词
    private ArrayList<String> otherKeywords = null;

    public EMRSegmentOfFamilyHistory(String keyword, String context, Boolean isRelativesNeeded, ArrayList<String> possibleUnit) {
        super(keyword, context, false, possibleUnit);
        this.isRelativesNeeded = isRelativesNeeded;
    }

    @Override
    public void parse() {
        this.extractRelatives();
    }

    public void extractRelatives() {
        String[] family = {"母亲", "父亲", "妈妈", "舅舅", "姑姑", "爸爸", "哥哥", "弟弟", "姐姐", "妹妹", "儿子", "女儿"};
        String valueIn = null;

        if(this.context.contains("患有" + this.keyword)) {
            valueIn = this.context.substring(0, this.context.indexOf("患有" + this.keyword));
        } else if(this.context.contains("得有" + this.keyword)) {
            valueIn = this.context.substring(0, this.context.indexOf("得有" + this.keyword));
        } else if(this.context.contains("患上" + this.keyword)) {
            valueIn = this.context.substring(0, this.context.indexOf("患上" + this.keyword));
        } else if(this.context.contains("有" + this.keyword)) {
            valueIn = this.context.substring(0, this.context.indexOf("有" + this.keyword));
        } else if(this.context.contains("诊断" + this.keyword)) {
            valueIn = this.context.substring(0, this.context.indexOf("诊断" + this.keyword));
        } else if(this.context.contains("死于" + this.keyword)) {
            valueIn = this.context.substring(0, this.context.indexOf("死于" + this.keyword));
        } else {
            return;
        }

        for(String s : family) {
            if(valueIn.contains(s)) {
                String content = this.context.substring(this.context.indexOf(s) + s.length(), this.context.indexOf(this.keyword));
                this.addRelative(s, this.getAge(content));
            }
        }
        if(this.relativesMapAge == null) {
            this.value = this.getAge(this.context);
        }
    }

    public String getAge(String content) {
        String age = "N/A";

        this.addPossibleUnit("来岁");
        this.addPossibleUnit("余岁");
        for(String u : super.possibleUnit) {
            String lowerCaseOfUnit = u.toLowerCase();
            String lowerCaseOfCutFromKeyword = content.toLowerCase();

            if(lowerCaseOfCutFromKeyword.contains(lowerCaseOfUnit)) {
                this.unit = "岁";
                if(lowerCaseOfUnit.contains("^")) {
                    lowerCaseOfUnit = lowerCaseOfUnit.substring(0, lowerCaseOfUnit.indexOf("^"));
                } else if(lowerCaseOfUnit.contains("*")) {
                    lowerCaseOfUnit = lowerCaseOfUnit.substring(0, lowerCaseOfUnit.indexOf("*"));
                }

                Pattern pattern = Pattern.compile("(([1-9]+[0-9]*(\\.[0-9]+)?)|(0\\.[0-9]+))((-|/)(([1-9]+[0-9]*(\\.[0-9]+)?)|(0\\.[0-9]+)))?" + lowerCaseOfUnit);
                Matcher matcher = pattern.matcher(lowerCaseOfCutFromKeyword);

                if(matcher.find()) {
                    age = matcher.group(0).substring(0, matcher.group(0).indexOf(lowerCaseOfUnit));
                }
            }
        }

        return age;
    }

    @Override
    public void extractOtherKeywords(ArrayList<String> otherPossibleKeywords) {
        for(String key : otherPossibleKeywords) {
            if(this.context.contains(key)) {
                addOtherKeywords(key);
            }
        }
    }

    /******** util ********/
    public void addRelative(String relative, String age) {
        if(this.relativesMapAge == null) {
            this.relativesMapAge = new HashMap<String, String>();
        }

        this.relativesMapAge.put(relative, age);
    }

    public HashMap<String, String> getRelatives() {
        return this.relativesMapAge;
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

    public void addPossibleUnit(String unit) {
        if(super.possibleUnit == null) {
            super.possibleUnit = new ArrayList<String>();
        }

        if(! super.possibleUnit.contains(unit)) {
            super.possibleUnit.add(unit);
        }
    }
}
