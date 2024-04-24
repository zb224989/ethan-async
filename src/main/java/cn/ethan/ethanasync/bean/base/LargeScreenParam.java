package cn.ethan.ethanasync.bean.base;

import lombok.Data;

@Data
public class LargeScreenParam extends AbstractLargeScreenParam {
    public LargeScreenParam() {
    }

    public LargeScreenParam(String key, Object value) {
        super(key, value);
    }
}
