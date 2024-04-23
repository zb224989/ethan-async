package cn.ethan.ethanasync.test2;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BsResult {
    private List<ITask> tasks = new ArrayList<>();

}
