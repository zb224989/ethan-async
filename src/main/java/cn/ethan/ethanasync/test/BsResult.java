package cn.ethan.ethanasync.test;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BsResult {
    private List<ITask> tasks = new ArrayList<>();

}
