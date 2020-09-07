package com.itheima.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ActivitiGroupTest {

    ProcessEngine processEngine;
    TaskService taskService;
    String candidateUser = "lisan";
    static String  KEY = "holiday";

    @Before
    public void init() {
        processEngine = ProcessEngines.getDefaultProcessEngine();
        taskService = processEngine.getTaskService();
    }

    /**
     * 部署流程定义
     */
    @Test
    public void deployment(){

        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("pic/holidayGroup.bpmn")
                .addClasspathResource("pic/holidayGroup.png")
                .name("请假流程")
                .deploy();

        System.out.println(deploy);
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActivitiGroupTest.KEY);

        System.out.println(processInstance);

    }

    /**
     * 查询组任务
     */
    @Test
    public void queryGroupTask() {

        queryTaskByCandidateUser(candidateUser, null, false).forEach(task -> {
            System.out.println(task);
        });

    }

    /**
     * 用户拾取组任务
     */
    @Test
    public void claimTask() {

        queryTaskByCandidateUser(candidateUser, null, false).forEach(task -> {
            taskService.claim(task.getId(), candidateUser);
            System.out.println(candidateUser + "用户拾取了[" + task.getName() + "]任务");
        });
    }

    /**
     * 用户归还组任务
     */
    @Test
    public void unClaimTask(){
        queryTaskByCandidateUser(null, candidateUser, true).forEach(task -> {
            taskService.setAssignee(task.getId(),null);
            System.out.println(candidateUser + "用户丢弃了[" + task.getName() + "]任务");
        });
    }

    /**
     * 用户任务交接,委托
     */
    @Test
    public void toAssignee(){
        queryTaskByCandidateUser(null,candidateUser,true).forEach(task -> {
            taskService.setAssignee(task.getId(),"lisi");
            System.out.println(candidateUser+"用户将["+task.getName()+"]任务交接给了lisi用户");
        });
    }

    /**
     * 查询并处理自己的任务
     */
    @Test
    public void queryCompletTask() {

        queryTaskByCandidateUser(null, "lisi", true).forEach(task -> {
            taskService.complete(task.getId());
            System.out.println(candidateUser+"处理了["+task.getName()+"]任务");
        });
    }

    /**
     * 根据候选人查询任务
     *
     * @param candidateUser 候选人
     * @param assignee 指定人
     * @param isAssignee 是否是指定人
     * @return 任务列表
     */
    public List<Task> queryTaskByCandidateUser(String candidateUser, String assignee, Boolean isAssignee) {

        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(ActivitiGroupTest.KEY);

        if (isAssignee) {
            taskQuery.taskAssignee(assignee);
        } else {
            taskQuery.taskCandidateUser(candidateUser);
        }

        return taskQuery.list();

    }


}
