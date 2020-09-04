package com.itheima.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * 查询当期用户任务的列表
 */
public class ActivitiTaskQuery {

    static final String ONE = "zhangsan";

    static final String TWO = "lisi";

    static final String THREE = "wangwu";

    static final String KEY = "holiday";

    /**
     * @param args
     */
    public static void main(String[] args) {

        // 1:得到ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        // 2:获取TaskService服务
        TaskService taskService = defaultProcessEngine.getTaskService();

        // 3:根据流程定义的Key,负责人assignee来实现当前用户的任务列表的查询
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(ActivitiTaskQuery.KEY).taskAssignee(ActivitiTaskQuery.ONE).list();

        // 4:任务列表的展示
        list.forEach(task -> {
            System.out.println("流程实例ID:" + task.getProcessDefinitionId());
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务负责人:" + task.getAssignee());
            System.out.println("任务名称:" + task.getName());
        });

    }

}
