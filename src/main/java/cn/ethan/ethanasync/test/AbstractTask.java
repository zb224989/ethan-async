package cn.ethan.ethanasync.test;


import lombok.Data;

@Data
public abstract class AbstractTask implements ITask {
    private String key;
    private Object value;
    @Override
    public String getKey() {
        return key;
    }
}
