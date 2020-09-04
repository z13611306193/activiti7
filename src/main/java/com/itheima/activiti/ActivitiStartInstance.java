package com.itheima.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * 启动流程实例:前提是完成流程部署
 */
public class ActivitiStartInstance {

    /**
     * 影响的表
     *  act_hi_actinst      已完成的活动信息
     *  act_hi_identitylink 参与者信息
     *  act_hi_procinst     流程实例
     *  act_hi_taskinst     任务实例
     *  act_ru_execution    执行表
     *  act_ru_identitylink 参与者信息
     *  act_ru_task         任务
     * @param args
     */
    public static void main(String[] args) {

        // 1:得到ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        // 2:获取RuntimeService服务
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        // 3:根据KEY启动流程实例
        ProcessInstance holiday = runtimeService.startProcessInstanceByKey("holiday");

        // 4:输出信息
        System.out.println("流程定义ID:" + holiday.getProcessDefinitionId());
        System.out.println("流程实例ID:" + holiday.getId());
        System.out.println("当前活跃ID:" + holiday.getActivityId());

    }

}
