package com.flowcenter.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowcenter.domain.entity.ProcessDefinitionEntity;
import com.flowcenter.repository.mapper.ProcessDefinitionMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessDefinitionRepository {
    private final ProcessDefinitionMapper mapper;
    public ProcessDefinitionRepository(ProcessDefinitionMapper mapper) { this.mapper = mapper; }

    public ProcessDefinitionEntity queryById(Long id) { return mapper.selectById(id); }
    public ProcessDefinitionEntity queryByIdForUpdate(Long id) { return mapper.queryByIdForUpdate(id); }

    public ProcessDefinitionEntity queryByProcessKey(String processKey) {
        LambdaQueryWrapper<ProcessDefinitionEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(ProcessDefinitionEntity::getProcessKey, processKey).last("LIMIT 1");
        return mapper.selectOne(qw);
    }

    public int save(ProcessDefinitionEntity entity) { return entity.getId() == null ? mapper.insert(entity) : mapper.updateById(entity); }
    public int deleteById(Long id) { return mapper.deleteById(id); }

    public Page<ProcessDefinitionEntity> queryPage(Page<ProcessDefinitionEntity> page, String processName, String status) {
        LambdaQueryWrapper<ProcessDefinitionEntity> qw = new LambdaQueryWrapper<>();
        qw.like(processName != null && !processName.trim().isEmpty(), ProcessDefinitionEntity::getProcessName, processName);
        qw.eq(status != null && !status.trim().isEmpty(), ProcessDefinitionEntity::getStatus, status);
        qw.orderByDesc(ProcessDefinitionEntity::getUpdatedAt);
        return mapper.selectPage(page, qw);
    }
}
