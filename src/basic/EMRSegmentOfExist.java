package basic;

/**
 * Created by hhw on 5/5/16.
 */
public class EMRSegmentOfExist extends EMRSegment{
    private Boolean isNegative = false;
    private String[] negativeWord = {"否认", "不存在", "未发现", "无"};

    public EMRSegmentOfExist(String keyword, String context, Boolean isDurationNeeded, Boolean isRelativesNeeded) {
        super(keyword, context, isDurationNeeded, isRelativesNeeded);
    }

    public Boolean parse() {
        super.extractDuration();
        super.extractRelatives();

        for(String s : this.negativeWord) {
            if(super.context.startsWith(s)) {
                this.isNegative = true;
                return false;
            }
        }

        return true;
    }

    /******** util ********/
    public void setIsNegative(Boolean isNegative) {
        this.isNegative = isNegative;
    }

    public Boolean getIsNegative() {
        return this.isNegative;
    }
}
