package cn.ethan.ethanasync.bean.base;


import lombok.Data;

@Data
public abstract class AbstractLargeScreenParam implements ILargeScreenParam {
    private String key;
    private Object value;

    public AbstractLargeScreenParam() {
    }

    public AbstractLargeScreenParam(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
