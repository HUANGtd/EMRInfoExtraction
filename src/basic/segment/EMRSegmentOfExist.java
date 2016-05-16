package basic.segment;

/**
 * Created by hhw on 5/5/16.
 */
public class EMRSegmentOfExist extends EMRSegment {
    private Boolean yesOrNo = true;
    private Boolean isNegative = false;
    private String[] negativeWord = {"否认", "不存在", "未发现", "无", "不伴", "未"};

    public EMRSegmentOfExist(String keyword, String context, Boolean isDurationNeeded) {
        super(keyword, context, isDurationNeeded);
    }

    public void parse() {
        super.extractDuration();

        for(String s : this.negativeWord) {
            if(super.context.startsWith(s)) {
                this.isNegative = true;
                this.yesOrNo = false;
                return;
            }
        }
    }

    /******** util ********/
    public void setIsNegative(Boolean isNegative) {
        this.isNegative = isNegative;
    }

    public Boolean getIsNegative() {
        return this.isNegative;
    }

    public Boolean getYesOrNo() {
        return this.yesOrNo;
    }
}
