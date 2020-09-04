package com.itheima.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;

/**
 * 流程定义的部署
 */
public class ActivitiDeployment {

    /**
     * 执行部署
     *  影响的表
     *      act_re_deployment 部署信息
     *      act_re_procdef    流程定义的一些信息
     *      act_ge_bytearray  流程定义的bpmn文件和png图片
     * @param args
     */
    public static void main(String[] args) {

        // 1:创建ProcessEngine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        // 2:获取RepositoryService服务
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        // 3:进行部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("pic/holiday.bpmn")
                .addClasspathResource("pic/holiday.png")
                .name("请假申请流程")
                .deploy();

        // 4:输出一些信息
        System.out.println("名称:" + deploy.getName());
        System.out.println("id:" + deploy.getId());

    }

}
