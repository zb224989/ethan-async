package cn.ethan.ethanasync.bean.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 大屏模块枚举
 */
@Getter
public enum LargeScreenKeyEnum {

    NURSING_LEVEL("nursingLevel", "大屏-护理等级模块"),
    CURRENT_EMP("currentEmp", "大屏-在职员工模块"),
    PORTRAIT_ELDERLY("portraitElderly", "大屏-长者画像模块"),
    HEALTHX_ANALYSIS("healthxAnalysis", "大屏-健康分析模块"),
    ;

    /**
     * 编码
     */
    private final String code;

    /**
     * 信息
     */
    @Setter
    private String remark;

    /**
     * 构造方法
     *
     * @param code    编码
     * @param message 信息
     */
    LargeScreenKeyEnum(String code, String message) {
        this.code = code;
        this.remark = message;
    }

}