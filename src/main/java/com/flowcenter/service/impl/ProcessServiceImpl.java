package com.flowcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowcenter.domain.dto.request.SaveProcessRequest;
import com.flowcenter.domain.dto.response.*;
import com.flowcenter.domain.entity.ProcessDefinitionEntity;
import com.flowcenter.domain.entity.ProcessVersionEntity;
import com.flowcenter.domain.enums.ProcessStatus;
import com.flowcenter.exception.BizException;
import com.flowcenter.exception.ErrorCode;
import com.flowcenter.repository.ProcessDefinitionRepository;
import com.flowcenter.repository.ProcessVersionRepository;
import com.flowcenter.service.FlowableDeployService;
import com.flowcenter.service.ProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    private static final long DEFAULT_PAGE_NUM = 1L;
    private static final long DEFAULT_PAGE_SIZE = 20L;
    private static final long MAX_PAGE_SIZE = 100L;

    private final ProcessDefinitionRepository definitionRepository;
    private final ProcessVersionRepository versionRepository;
    private final FlowableDeployService deployService;

    public ProcessServiceImpl(ProcessDefinitionRepository definitionRepository, ProcessVersionRepository versionRepository, FlowableDeployService deployService) {
        this.definitionRepository = definitionRepository;
        this.versionRepository = versionRepository;
        this.deployService = deployService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveProcessResponse saveProcess(SaveProcessRequest request) {
        if (request.getId() == null) {
            return createProcess(request);
        }
        return updateProcess(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublishProcessResponse publishProcess(Long id) {
        ProcessDefinitionEntity e = queryForUpdate(id);
        if (!ProcessStatus.DRAFT.name().equals(e.getStatus()) && !ProcessStatus.DISABLED.name().equals(e.getStatus())) {
            throw new BizException(ErrorCode.STATUS_ILLEGAL, "only DRAFT or DISABLED can publish");
        }

        FlowableDeployService.DeployResult r = deployService.deployProcess(e.getProcessKey(), e.getProcessName(), e.getBpmnXml());

        int newVersion = e.getCurrentVersion() + 1;
        ProcessVersionEntity version = new ProcessVersionEntity();
        version.setProcessId(e.getId());
        version.setVersion(newVersion);
        version.setDeploymentId(r.getDeploymentId());
        version.setProcessDefinitionId(r.getProcessDefinitionId());
        version.setBpmnXml(e.getBpmnXml());
        versionRepository.save(version);

        e.setCurrentVersion(newVersion);
        e.setStatus(ProcessStatus.PUBLISHED.name());
        definitionRepository.save(e);

        PublishProcessResponse resp = new PublishProcessResponse();
        resp.setProcessId(e.getId());
        resp.setVersion(newVersion);
        resp.setDeploymentId(version.getDeploymentId());
        resp.setProcessDefinitionId(version.getProcessDefinitionId());
        resp.setStatus(e.getStatus());
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublishProcessResponse disableProcess(Long id) {
        ProcessDefinitionEntity e = queryForUpdate(id);
        if (!ProcessStatus.PUBLISHED.name().equals(e.getStatus())) {
            throw new BizException(ErrorCode.STATUS_ILLEGAL, "only PUBLISHED can disable");
        }
        e.setStatus(ProcessStatus.DISABLED.name());
        definitionRepository.save(e);

        PublishProcessResponse resp = new PublishProcessResponse();
        resp.setProcessId(e.getId());
        resp.setVersion(e.getCurrentVersion());
        resp.setStatus(e.getStatus());
        return resp;
    }

    @Override
    public PageResponse<ProcessListItemResponse> queryProcessPage(Long pageNum, Long pageSize, String processName, String status) {
        long pn = pageNum == null || pageNum < 1 ? DEFAULT_PAGE_NUM : pageNum;
        long ps = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);

        Page<ProcessDefinitionEntity> page = definitionRepository.queryPage(new Page<>(pn, ps), processName, status);
        List<ProcessListItemResponse> records = new ArrayList<>();
        for (ProcessDefinitionEntity e : page.getRecords()) {
            ProcessListItemResponse item = new ProcessListItemResponse();
            item.setId(e.getId());
            item.setProcessKey(e.getProcessKey());
            item.setProcessName(e.getProcessName());
            item.setStatus(e.getStatus());
            item.setCurrentVersion(e.getCurrentVersion());
            item.setUpdatedAt(String.valueOf(e.getUpdatedAt()));
            records.add(item);
        }

        PageResponse<ProcessListItemResponse> resp = new PageResponse<>();
        resp.setTotal(page.getTotal());
        resp.setPageNum(pn);
        resp.setPageSize(ps);
        resp.setRecords(records);
        return resp;
    }

    @Override
    public ProcessDetailResponse queryProcessDetail(Long id) {
        ProcessDefinitionEntity e = definitionRepository.queryById(id);
        if (e == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "process not found");
        }

        List<ProcessVersionItemResponse> versions = new ArrayList<>();
        for (ProcessVersionEntity v : versionRepository.queryByProcessId(id)) {
            ProcessVersionItemResponse item = new ProcessVersionItemResponse();
            item.setVersion(v.getVersion());
            item.setDeploymentId(v.getDeploymentId());
            item.setProcessDefinitionId(v.getProcessDefinitionId());
            item.setCreatedAt(String.valueOf(v.getCreatedAt()));
            versions.add(item);
        }

        ProcessDetailResponse resp = new ProcessDetailResponse();
        resp.setId(e.getId());
        resp.setProcessKey(e.getProcessKey());
        resp.setProcessName(e.getProcessName());
        resp.setBpmnXml(e.getBpmnXml());
        resp.setStatus(e.getStatus());
        resp.setCurrentVersion(e.getCurrentVersion());
        resp.setCreatedAt(String.valueOf(e.getCreatedAt()));
        resp.setUpdatedAt(String.valueOf(e.getUpdatedAt()));
        resp.setVersions(versions);
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProcess(Long id) {
        ProcessDefinitionEntity e = queryForUpdate(id);
        if (!ProcessStatus.DRAFT.name().equals(e.getStatus())) {
            throw new BizException(ErrorCode.STATUS_ILLEGAL, "only DRAFT can delete");
        }
        definitionRepository.deleteById(id);
    }

    private SaveProcessResponse createProcess(SaveProcessRequest request) {
        if (definitionRepository.queryByProcessKey(request.getProcessKey()) != null) {
            throw new BizException(ErrorCode.DUPLICATE_KEY, "processKey exists");
        }
        ProcessDefinitionEntity e = new ProcessDefinitionEntity();
        e.setProcessKey(request.getProcessKey());
        e.setProcessName(request.getProcessName());
        e.setBpmnXml(request.getBpmnXml());
        e.setStatus(ProcessStatus.DRAFT.name());
        e.setCurrentVersion(0);
        definitionRepository.save(e);

        SaveProcessResponse resp = new SaveProcessResponse();
        resp.setProcessId(e.getId());
        resp.setStatus(e.getStatus());
        return resp;
    }

    private SaveProcessResponse updateProcess(SaveProcessRequest request) {
        ProcessDefinitionEntity e = definitionRepository.queryById(request.getId());
        if (e == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "process not found");
        }
        if (!e.getProcessKey().equals(request.getProcessKey())) {
            throw new BizException(ErrorCode.PARAM_ERROR, "processKey is immutable");
        }
        e.setProcessName(request.getProcessName());
        e.setBpmnXml(request.getBpmnXml());
        e.setStatus(ProcessStatus.DRAFT.name());
        definitionRepository.save(e);

        SaveProcessResponse resp = new SaveProcessResponse();
        resp.setProcessId(e.getId());
        resp.setStatus(e.getStatus());
        return resp;
    }

    private ProcessDefinitionEntity queryForUpdate(Long id) {
        ProcessDefinitionEntity e = definitionRepository.queryByIdForUpdate(id);
        if (e == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "process not found");
        }
        return e;
    }
}
