package com.kdsj.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RequestMapping("/activiti/")
@RestController
public class ActivitiController {
    private static Logger logger = LoggerFactory.getLogger(ActivitiController.class);
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RuntimeService runtimeService;

    /**
     * 根据Model部署流程
     */
    @PostMapping(value = "deploy/{modelId}")
    public void deploy(@PathVariable("modelId") String modelId) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionResourceName(processName).list();
            for (ProcessDefinition definition : list) {
                //删除原有流程定义,正在使用的流程定义无法删除
                repositoryService.deleteDeployment(definition.getDeploymentId());
            }
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
            System.out.println("流程部署id----" + deployment.getId());
        } catch (Exception e) {
            logger.error("根据模型部署流程失败：modelId={}", modelId, e);
        }
    }
    /**
     * 启动流程
     */
    @RequestMapping("start/{modelId}")
    public void startProcessInstanceByKey(@PathVariable("modelId") String modelId) {
//        Model modelData = repositoryService.getModel(modelId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(modelId);
        //获取流程实例的相关信息
        String processDefinitionId = processInstance.getProcessDefinitionId();
        System.out.println("流程定义的id = " + processDefinitionId);
        String deploymentId = processInstance.getDeploymentId();
        System.out.println("流程部署的id = " + deploymentId);
        String id = processInstance.getId();
        System.out.println("流程实例的id = " + id);
        String activityId = processInstance.getActivityId();
        System.out.println("当前活动的id = " + activityId);
    }

    /***
     * 执行下一步
     */
    @RequestMapping("complete")
    public void complete() {
        /**获取任务id**/
        try{
            List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("process").list();
            for (Task task : taskList) {
                taskService.complete(task.getId());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /***
     * 任务查询
     */
    @RequestMapping("query")
    public void query() {
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("process").list();
        //遍历任务列表
        for (Task task : taskList) {
            String processDefinitionId = task.getProcessDefinitionId();
            System.out.println("流程定义id = " + processDefinitionId);
            String processInstanceId = task.getProcessInstanceId();
            System.out.println("流程实例id = " + processInstanceId);
            String assignee1 = task.getAssignee();
            System.out.println("任务负责人 = " + assignee1);
            String id = task.getId();
            System.out.println("任务id = " + id);
            String name = task.getName();
            System.out.println("任务名称 = " + name);
        }
    }

    /**
     * 获取下一节点
     */
    @RequestMapping("getNextStep")
    public void getNextStep() {
        List<Task> tasks = taskService.createTaskQuery().processDefinitionKey("process").list();
        for(Task task : tasks) {
            ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(task.getProcessDefinitionId());
            List<ActivityImpl> activitiList = def.getActivities();

            String excId = task.getExecutionId();
            ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(excId).singleResult();
            String activitiId = execution.getActivityId();
            for(ActivityImpl activityImpl:activitiList){
                String id = activityImpl.getId();
                if(activitiId.equals(id)){
                    System.out.println("当前任务："+activityImpl.getProperty("name"));//输出某个节点的某种属性
                    List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();//获取从某个节点出来的所有线路
                    for(PvmTransition tr:outTransitions){
                        PvmActivity ac = tr.getDestination();//获取线路的终点节点
                        System.out.println("下一步任务任务："+ac.getProperty("name"));
                    }
                    break;
                }
            }
        }
    }
    /**
     * 新建模型
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("create")
    public ModelAndView newModel() throws UnsupportedEncodingException {
//    	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//    	RepositoryService repositoryService = processEngine.getRepositoryService();
//    	ObjectMapper objectMapper = new ObjectMapper();
        //初始化一个空模型
        Model model = repositoryService.newModel();

        //设置一些默认信息，可以用参数接收
        int revision = 1;
        String key = "process";
        String name = "process";
        String description = "新建流程模型";

        //ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);


        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);

        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());

        repositoryService.saveModel(model);

        String id = model.getId();

        repositoryService.addModelEditorSource(id, editorNode.toString().getBytes(StandardCharsets.UTF_8));
        return new ModelAndView("redirect:/modeler.html?modelId=" + id);
    }

    /**
     * 模型编辑
     *
     * @param modelId
     * @return
     */
    @GetMapping("update/{modelId}")
    public ModelAndView updateModel(@PathVariable String modelId) {
        System.out.println("用户点击模型编辑");
        return new ModelAndView("redirect:/modeler.html?modelId=" + modelId);
    }


    /**
     * 导出指定模型xml 文件
     *
     * @param modelId
     * @param response
     */
    @GetMapping(value = "export/{modelId}")
    public void export(@PathVariable String modelId, HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(in, outputStream);
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn.xml";
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            logger.error("导出model的xml文件失败：{}", e.getMessage(), e);
        }
    }

    @GetMapping("import")
    public ModelAndView importXml(HttpServletResponse response) {

        try {
            File file = new File("C:\\Users\\IEDS-PC\\Downloads\\process.bpmn.xml");

            InputStream fileInputStream = new FileInputStream(file);
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream("请假流程" + ".bpmn", fileInputStream)
                    .deploy();
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            String modelId = changeProcessToModel(processDefinition);
            return new ModelAndView("redirect:/modeler.html?modelId=" + modelId);
        } catch (Exception e) {
            logger.error("模型基于xml导入异常:{}", e.getMessage(), e);
        }
        return new ModelAndView("redirect:/modelError.html");
    }

    /**
     * 流程转化为可编辑模型
     *
     * @param processDefinition
     */

    public String changeProcessToModel(ProcessDefinition processDefinition) {

        Model modelData = repositoryService.newModel();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 初始化Model
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());
        modelData.setName(processDefinition.getName());
        modelData.setKey(processDefinition.getKey());

        // 保存模型
        repositoryService.saveModel(modelData);
        String deploymentId = processDefinition.getDeploymentId();
        String processDefineResourceName = null;
        // 通过deploymentId取得某个部署的资源的名称
        List<String> resourceNames = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        if (resourceNames != null && resourceNames.size() > 0) {
            for (String temp : resourceNames) {
                if (temp.indexOf(".bpmn") > 0) {
                    processDefineResourceName = temp;
                }
            }
        }
        InputStream bpmnStream = processEngine.getRepositoryService().getResourceAsStream(deploymentId, processDefineResourceName);
        createModelByInputStream(bpmnStream, modelData.getId());
        return modelData.getId();
    }

    public void createModelByInputStream(InputStream bpmnStream, String ModelID) {
        XMLInputFactory xif;
        InputStreamReader in = null;
        XMLStreamReader xtr = null;
        try {
            xif = XMLInputFactory.newFactory();
            in = new InputStreamReader(bpmnStream, StandardCharsets.UTF_8);
            xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = (new BpmnXMLConverter()).convertToBpmnModel(xtr);
            ObjectNode modelNode = new BpmnJsonConverter().convertToJson(bpmnModel);
            repositoryService.addModelEditorSource(ModelID, modelNode.toString().getBytes(StandardCharsets.UTF_8));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            if (xtr != null) {
                try {
                    xtr.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bpmnStream != null) {
                try {
                    bpmnStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("png/{taskId}")
    public void currentProcessInstanceImage(@PathVariable("taskId") String taskId, HttpServletResponse response) throws IOException {
        int index;
        InputStream inputStream = this.currentProcessInstanceImage(taskId);
        OutputStream out = response.getOutputStream();
        response.setContentType("image/png");
        byte[] bytes = new byte[1024];
        while ((index = inputStream.read(bytes)) != -1) {
            out.write(bytes, 0, index);
            out.flush();
        }
        out.close();
        inputStream.close();

    }

    /**
     * 获取当前任务流程图
     *
     * @param taskId
     * @return
     */
    public InputStream currentProcessInstanceImage(String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        // ID 为 流程定义Key
        org.activiti.bpmn.model.Process process = bpmnModel.getProcessById(processDefinition.getKey());

        //Process process = bpmnModel.getProcessById(processDefinition.getKey());

//        UserTask userTask = (UserTask) process.getFlowElement(task.getTaskDefinitionKey());
        // 流程节点ID
        FlowElement flowElement = process.getFlowElement(task.getTaskDefinitionKey());

        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();


        List<String> highLightedActivities = new ArrayList<>();
        highLightedActivities.add(flowElement.getId());

//     生成流程图
//        InputStream inputStream = generator.generateJpgDiagram(bpmnModel);
//        InputStream inputStream = generator.generatePngDiagram(bpmnModel);
//        InputStream inputStream = generator.generateDiagram(bpmnModel, "jpg", highLightedActivities);

// 生成图片
        InputStream inputStream = generator.generateDiagram(bpmnModel, "jpg", highLightedActivities, Collections.emptyList(), "宋体", "宋体", "宋体", null, 2.0);
        return inputStream;
    }


}