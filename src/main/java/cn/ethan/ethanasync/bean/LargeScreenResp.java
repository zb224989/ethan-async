package cn.ethan.ethanasync.bean;

import lombok.Data;

import java.util.HashMap;

/**
 * 大屏DTO
 */
@Data
public class LargeScreenResp extends HashMap<String, Object> {

    private static final long serialVersionUID = 9070446850527023057L;

    /**
     * 大屏-护理等级
     */
    private Object nursingLevel;

    private Object currentEmp;

    private Object portraitElderly;

    private Object healthxAnalysis;
}
