package cn.ethan.ethanasync.bean;

import lombok.Data;
import java.io.Serializable;

/**
 * 大屏接口请求参数类
 */
@Data
public class LargeScreenReq implements Serializable {

    private static final long serialVersionUID = -2404495504049594086L;

    private Long orgId;
}
