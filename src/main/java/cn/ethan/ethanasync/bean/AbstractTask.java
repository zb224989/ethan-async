package cn.ethan.ethanasync.bean;


import lombok.Data;

@Data
public abstract class AbstractTask implements ITask {
    private String key;
    private Object value;
}
