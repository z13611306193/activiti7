package com.itheima.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

/**
 * 提交任务
 *      影响的表:
 *          act_hi_actinst
 *          act_hi_taskinst
 *          act_hi_identitylink
 *          act_ru_task
 *          act_ru_identitylink
 */
public class ActivitiCompletTask {

    public static void main(String[] args) {

        // 1:得到ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        // 2:获取TaskService服务
        TaskService taskService = defaultProcessEngine.getTaskService();

        // 2.1:综合查询提交
//        Task task = taskService.createTaskQuery().processDefinitionKey(ActivitiTaskQuery.KEY).taskAssignee(ActivitiTaskQuery.ONE).singleResult();

        // 3:根据之前查询出来的任务ID 提交任务
        taskService.complete("17505"); //张三提交
//        taskService.complete("5002"); //李四提交

//        taskService.complete(task.getId());

        System.out.println("任务ID:2505");
    }

}
