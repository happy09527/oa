package com.example.process.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auth.service.SysUserService;
import com.example.model.process.Process;
import com.example.model.process.ProcessRecord;
import com.example.model.process.ProcessTemplate;
import com.example.model.system.SysUser;
import com.example.process.mapper.ProcessMapper;
import com.example.process.service.ProcessRecordService;
import com.example.process.service.ProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.process.service.ProcessTemplateService;
import com.example.security.custom.LoginUserInfoHelper;
import com.example.vo.process.ApprovalVo;
import com.example.vo.process.ProcessFormVo;
import com.example.vo.process.ProcessQueryVo;
import com.example.vo.process.ProcessVo;

import com.example.wechat.service.MessageService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-11
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {
//    @Resource
//    private ProcessMapper processMapper;
//    @Resource
//    private RepositoryService repositoryService;
//    @Autowired
//    private ProcessTemplateService processTemplateService;
//    @Autowired
//    private SysUserService sysUserService;
//
//    @Autowired
//    private RuntimeService runtimeService;
//    @Autowired
//    private TaskService taskService;
//    @Autowired
//    private ProcessRecordService processRecordService;
//    @Autowired
//    private HistoryService historyService;
//
//    @Override
//    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
//        ProcessQueryVo processQueryVo = new ProcessQueryVo();
//        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
//        IPage<ProcessVo> page = processMapper.selectPage(pageParam, processQueryVo);
//        for (ProcessVo item : page.getRecords()) {
//            item.setTaskId("0");
//        }
//        return page;
//    }
//
//    @Override
//    public IPage<ProcessVo> findProcessed(Page<Process> pageParam) {
//        // 根据当前人的ID查询
//        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
//                .taskAssignee(LoginUserInfoHelper.getUsername()).finished().orderByTaskCreateTime().desc();
//        List<HistoricTaskInstance> list = query.listPage((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()), (int) pageParam.getSize());
//        long totalCount = query.count();
//
//        List<ProcessVo> processList = new ArrayList<>();
//        for (HistoricTaskInstance item : list) {
//            String processInstanceId = item.getProcessInstanceId();
//            Process process = baseMapper.selectOne(new LambdaQueryWrapper<Process>().eq(Process::getProcessInstanceId, processInstanceId));
//            ProcessVo processVo = new ProcessVo();
//            if (process != null){
//                BeanUtils.copyProperties(process, processVo);
//                processVo.setTaskId("0");
//                processList.add(processVo);
//            }
//        }
//        IPage<ProcessVo> page = new Page<ProcessVo>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
//        page.setRecords(processList);
//        return page;
//    }
//
//    @Override
//    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
//        IPage<ProcessVo> page = processMapper.selectPage(pageParam, processQueryVo);
//        return page;
//    }
//
//    @Override
//    public void deployByZip(String deployPath) {
//        // 定义zip输入流
//        InputStream inputStream = this
//                .getClass()
//                .getClassLoader()
//                .getResourceAsStream(deployPath);
//        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
//        // 流程部署
//        Deployment deployment = repositoryService.createDeployment()
//                .addZipInputStream(zipInputStream)
//                .deploy();
//    }
//
//    @Override
//    public void startUp(ProcessFormVo processFormVo) {
//        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
//
//        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());
//        Process process = new Process();
//        BeanUtils.copyProperties(processFormVo, process);
//        String workNo = System.currentTimeMillis() + "";
//        process.setProcessCode(workNo);
//        process.setUserId(LoginUserInfoHelper.getUserId());
//        process.setFormValues(processFormVo.getFormValues());
//        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
//        process.setStatus(1);
//        processMapper.insert(process);
//
//        //绑定业务id
//        String businessKey = String.valueOf(process.getId());
//        //流程参数
//        Map<String, Object> variables = new HashMap<>();
//        //将表单数据放入流程实例中
//        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
//        JSONObject formData = jsonObject.getJSONObject("formData");
//        Map<String, Object> map = new HashMap<>();
//        //循环转换
//        for (Map.Entry<String, Object> entry : formData.entrySet()) {
//            map.put(entry.getKey(), entry.getValue());
//        }
//        variables.put("data", map);
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processTemplate.getProcessDefinitionKey(), businessKey, variables);
//        //业务表关联当前流程实例id
//        String processInstanceId = processInstance.getId();
//        process.setProcessInstanceId(processInstanceId);
//
//        //计算下一个审批人，可能有多个（并行审批）
//        List<Task> taskList = this.getCurrentTaskList(processInstanceId);
//        if (!CollectionUtils.isEmpty(taskList)) {
//            List<String> assigneeList = new ArrayList<>();
//            for (Task task : taskList) {
//                SysUser user = sysUserService.getUserByUserName(task.getAssignee());
//                assigneeList.add(user.getName());
//
//                //推送消息给下一个审批人，后续完善
//            }
//            process.setProcessInstanceId(processInstance.getId());
//            process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
//            // 7 业务和流程关联  更新oa_process数据
//            baseMapper.updateById(process);
//        }
//        processMapper.updateById(process);
//    }
//
//    /**
//     * 获取当前任务列表
//     *
//     * @param processInstanceId
//     * @return
//     */
//    private List<Task> getCurrentTaskList(String processInstanceId) {
//        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
//        return tasks;
//    }
//
//    @Override
//    public IPage<ProcessVo> findPending(Page<Process> pageParam) {
//        TaskQuery query = taskService.createTaskQuery().taskAssignee(LoginUserInfoHelper.getUsername()).orderByTaskCreateTime()
//                .desc();
//        List<Task> list = query.listPage((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()), (int) pageParam.getSize());
//        long totalCount = query.count();
//
//        List<ProcessVo> processList = new ArrayList<>();
//
//        // 根据流程的业务ID查询实体并关联
//        for (Task item : list) {
//            String processInstanceId = item.getProcessInstanceId();
//            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//            if (processInstance == null) {
//                continue;
//            }
//            // 业务key
//            String businessKey = processInstance.getBusinessKey();
//            if (businessKey == null) {
//                continue;
//            }
//            Process process = this.getById(Long.parseLong(businessKey));
//            ProcessVo processVo = new ProcessVo();
//            BeanUtils.copyProperties(process, processVo);
//            processVo.setTaskId(item.getId());
//            processList.add(processVo);
//        }
//        IPage<ProcessVo> page = new Page<ProcessVo>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
//        page.setRecords(processList);
//        return page;
//    }
//
//    @Override
//    public Map<String, Object> show(Long id) {
//        Process process = this.getById(id);
//        List<ProcessRecord> processRecordList = processRecordService.list(new LambdaQueryWrapper<ProcessRecord>().eq(ProcessRecord::getProcessId, id));
//        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
//        Map<String, Object> map = new HashMap<>();
//        map.put("process", process);
//        map.put("processRecordList", processRecordList);
//        map.put("processTemplate", processTemplate);
//        //计算当前用户是否可以审批，能够查看详情的用户不是都能审批，审批后也不能重复审批
//        boolean isApprove = false;
//        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
//        if (!CollectionUtils.isEmpty(taskList)) {
//            for (Task task : taskList) {
//                if (task.getAssignee().equals(LoginUserInfoHelper.getUsername())) {
//                    isApprove = true;
//                }
//            }
//        }
//        map.put("isApprove", isApprove);
//        return map;
//    }
//
//    @Override
//    public void approve(ApprovalVo approvalVo) {
//        Map<String, Object> variables1 = taskService.getVariables(approvalVo.getTaskId());
//        for (Map.Entry<String, Object> entry : variables1.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }
//        String taskId = approvalVo.getTaskId();
//        if (approvalVo.getStatus() == 1) {
//            //已通过
//            Map<String, Object> variables = new HashMap<String, Object>();
//            taskService.complete(taskId, variables);
//        } else {
//            //驳回
//            this.endTask(taskId);
//        }
//        String description = approvalVo.getStatus().intValue() == 1 ? "已通过" : "驳回";
//        processRecordService.record(approvalVo.getProcessId(), approvalVo.getStatus(), description);
//
//        //计算下一个审批人
//        Process process = baseMapper.selectById(approvalVo.getProcessId());
//        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
//        if (!CollectionUtils.isEmpty(taskList)) {
//            List<String> assigneeList = new ArrayList<>();
//            for (Task task : taskList) {
//                SysUser sysUser = sysUserService.getUserByUserName(task.getAssignee());
//                assigneeList.add(sysUser.getName());
//                //推送消息给下一个审批人
//            }
//            process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
//            process.setStatus(1);
//        } else {
//            if (approvalVo.getStatus().intValue() == 1) {
//                process.setDescription("审批完成（同意）");
//                process.setStatus(2);
//            } else {
//                process.setDescription("审批完成（拒绝）");
//                process.setStatus(-1);
//            }
//        }
//        //推送消息给申请人
//        baseMapper.updateById(process);
//    }
//
//    private void endTask(String taskId) {
//        //  当前任务
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
//        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
//        // 并行任务可能为null
//        if (CollectionUtils.isEmpty(endEventList)) {
//            return;
//        }
//        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
//        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
//
//        //  临时保存当前活动的原始方向
//        List originalSequenceFlowList = new ArrayList<>();
//        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
//        //  清理活动方向
//        currentFlowNode.getOutgoingFlows().clear();
//
//        //  建立新方向
//        SequenceFlow newSequenceFlow = new SequenceFlow();
//        newSequenceFlow.setId("newSequenceFlowId");
//        newSequenceFlow.setSourceFlowElement(currentFlowNode);
//        newSequenceFlow.setTargetFlowElement(endFlowNode);
//        List newSequenceFlowList = new ArrayList<>();
//        newSequenceFlowList.add(newSequenceFlow);
//        //  当前节点指向新的方向
//        currentFlowNode.setOutgoingFlows(newSequenceFlowList);
//
//        //  完成当前任务
//        taskService.complete(task.getId());
//    }

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProcessTemplateService processTemplateService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessRecordService processRecordService;

    @Autowired
    private HistoryService historyService;


    @Autowired
    private MessageService messageService;

    //审批管理列表
    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageInfo, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageInfo, processQueryVo);
        return pageModel;
    }

    // 流程定义部署
    @Override
    public void deployByZip(String deployPath) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 部署
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();

        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
    }

    // 启动流程
    @Override
    @Transactional
    public Process startUp(ProcessFormVo processFormVo) {
        // 1 根据当前用户id获取用户信息
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());

        // 2 根据审批模板id把模板信息查询
        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());

        // 3 保存提交审批信息到业务表，oa_process
        Process process = new Process();
        // processFormVo复制到process对象里面
        BeanUtils.copyProperties(processFormVo, process);
        // 其他值
        process.setStatus(1); //审批中
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        baseMapper.insert(process);


        // 4 启动流程实例 - RuntimeService
        // 4.1 流程定义key
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();

        //4.2 业务key  processId
        String businessKey = String.valueOf(process.getId());

        //4.3 流程参数 form表单json数据，转换map集合
        String formValues = processFormVo.getFormValues();
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        // 遍历formData得到的内容，封装map集合
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        Map<String, Object> variable = new HashMap<>();
        variable.put("data", map);

        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variable);


        //计算下一个审批人
        List<Task> taskList = this.getCurrentTaskList(processInstance.getId());
        if (!CollectionUtils.isEmpty(taskList)) {
            List<String> assigneeList = new ArrayList<>();
            for(Task task : taskList) {
                SysUser user = sysUserService.getUserByUserName(task.getAssignee());
                assigneeList.add(user.getName());

                //推送消息给下一个审批人
                messageService.pushPendingMessage(process.getId(), user.getId(), task.getId());
            }
            process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
        }

        baseMapper.updateById(process);
        return process;
    }

    // 查询待处理的列表
    @Override
    public IPage<ProcessVo> findPending(Page<Process> pageParam) {

        //1 封装查询条件，根据当前登录的用户名称
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();

        //2 调用方法分页条件查询，返回list集合，待办任务集合
        //listPage方法有两个参数
        //第一个参数：开始位置  第二个参数：每页显示记录数
        int begin = (int) ((pageParam.getCurrent() - 1) * pageParam.getSize());
        int size = (int) pageParam.getSize();
        List<Task> taskList = taskQuery.listPage(begin, size);
        long totalCount = taskQuery.count();

        //3 封装返回list集合数据 到 List<ProcessVo>里面
        //List<Task> -- List<ProcessVo>
        List<ProcessVo> processVoList = new ArrayList<>();
        if (taskList.size() > 0) {
            for (Task task : taskList) {
                //从task获取流程实例id
                String processInstanceId = task.getProcessInstanceId();
                //根据流程实例id获取实例对象
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId).singleResult();
                //从流程实例对象获取业务key---processId

                String businessKey = processInstance.getBusinessKey();
                if (businessKey == null) {
                    continue;
                }
                Process process = this.getById(Long.parseLong(businessKey));

                //Process对象 复制 ProcessVo对象
                ProcessVo processVo = new ProcessVo();
                if (process != null) {
                    BeanUtils.copyProperties(process, processVo);
                    processVo.setTaskId(task.getId());

                    //放到最终list集合processVoList
                    processVoList.add(processVo);
                }

            }
        }
        //4 封装返回IPage对象
        IPage<ProcessVo> page = new Page<>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
        page.setRecords(processVoList);
        return page;
    }

    //查看审批详情信息
    @Override
    public Map<String, Object> show(Long id) {
        //1 根据流程id获取流程信息Process
        Process process = baseMapper.selectById(id);

        //2 根据流程id获取流程记录信息
        LambdaQueryWrapper<ProcessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId, id);
        List<ProcessRecord> processRecordList = processRecordService.list(wrapper);

        //3 根据模板id查询模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());

        //4 判断当前用户是否可以审批
        //可以看到信息不一定能审批，不能重复审批
        boolean isApprove = false;
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        for (Task task : taskList) {
            //判断任务审批人是否是当前用户
            String username = LoginUserInfoHelper.getUsername();
            if (task.getAssignee().equals(username)) {
                isApprove = true;
            }
        }

        //5 查询数据封装到map集合，返回
        Map<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", processRecordList);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);
        return map;
    }

    //审批
    @Override
    public void approve(ApprovalVo approvalVo) {
        //1 从approvalVo获取任务id，根据任务id获取流程变量
        String taskId = approvalVo.getTaskId();
        Map<String, Object> variables = taskService.getVariables(taskId);
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

        //2 判断审批状态值
        if (approvalVo.getStatus() == 1) {
            //2.1 状态值 =1  审批通过
            Map<String, Object> variable = new HashMap<>();
            taskService.complete(taskId, variable);
        } else {
            //2.2 状态值 = -1 驳回，流程直接结束
            this.endTask(taskId);
        }

        //3 记录审批相关过程信息 oa_process_record
        String description = approvalVo.getStatus().intValue() == 1 ? "已通过" : "驳回";
        processRecordService.record(approvalVo.getProcessId(),
                approvalVo.getStatus(), description);

        //4 查询下一个审批人，更新流程表记录 process表记录
        //计算下一个审批人
        Process process = this.getById(approvalVo.getProcessId());
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if (!CollectionUtils.isEmpty(taskList)) {
            List<String> assigneeList = new ArrayList<>();
            for(Task task : taskList) {
                SysUser sysUser = sysUserService.getUserByUserName(task.getAssignee());
                assigneeList.add(sysUser.getName());

                //推送消息给下一个审批人
                messageService.pushPendingMessage(process.getId(), sysUser.getId(), task.getId());
            }

            process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
            process.setStatus(1);
        } else {
            if(approvalVo.getStatus().intValue() == 1) {
                process.setDescription("审批完成（通过）");
                process.setStatus(2);
            } else {
                process.setDescription("审批完成（驳回）");
                process.setStatus(-1);
            }
        }
        //推送消息给申请人
        messageService.pushProcessedMessage(process.getId(), process.getUserId(), approvalVo.getStatus());
        this.updateById(process);
    }

    //已处理
    @Override
    public IPage<ProcessVo> findProcessed(Page<Process> pageParam) {
        //封装查询条件
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .finished().orderByTaskCreateTime().desc();

        //调用方法条件分页查询，返回list集合
        // 开始位置  和  每页显示记录数
        int begin = (int) ((pageParam.getCurrent() - 1) * pageParam.getSize());
        int size = (int) pageParam.getSize();
        List<HistoricTaskInstance> list = query.listPage(begin, size);
        long totalCount = query.count();

        //遍历返回list集合，封装List<ProcessVo>
        List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricTaskInstance item : list) {
            //流程实例id
            String processInstanceId = item.getProcessInstanceId();
            //根据流程实例id查询获取process信息
            LambdaQueryWrapper<Process> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Process::getProcessInstanceId, processInstanceId);
            Process process = baseMapper.selectOne(wrapper);
            // process -- processVo
            ProcessVo processVo = new ProcessVo();
            if (process != null) {
                BeanUtils.copyProperties(process, processVo);
                processVo.setTaskId("0");
                //放到list
                processVoList.add(processVo);
            }
        }

        //IPage封装分页查询所有数据，返回
        IPage<ProcessVo> pageModel =
                new Page<ProcessVo>(pageParam.getCurrent(), pageParam.getSize(),
                        totalCount);
        pageModel.setRecords(processVoList);
        return pageModel;
    }

    //已发起
    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageParam, processQueryVo);
        for (ProcessVo item : pageModel.getRecords()) {
            item.setTaskId("0");
        }
        return pageModel;
    }

    //结束流程
    private void endTask(String taskId) {
        //1 根据任务id获取任务对象 Task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        //2 获取流程定义模型 BpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //3 获取结束流向节点
        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        if (CollectionUtils.isEmpty(endEventList)) {
            return;
        }
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);

        //4 当前流向节点
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //5 清理当前流动方向
        currentFlowNode.getOutgoingFlows().clear();

        //6 创建新流向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlow");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);

        //7 当前节点指向新方向
        List newSequenceFlowList = new ArrayList();
        newSequenceFlowList.add(newSequenceFlow);
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //8 完成当前任务
        taskService.complete(task.getId());
    }


    private List<Task> getCurrentTaskList(String id) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(id).list();
        return taskList;
    }
}
